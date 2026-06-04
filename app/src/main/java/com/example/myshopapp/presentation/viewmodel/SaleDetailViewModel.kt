package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.domain.usecase.GetSaleFullByQrUseCase
import com.example.myshopapp.domain.usecase.GetShiftUseCase
import com.example.myshopapp.domain.usecase.MoneyBackUseCase
import com.example.myshopapp.domain.usecase.ReprintReceiptUseCase
import com.example.myshopapp.domain.usecase.RollbackUseCase
import com.example.myshopapp.domain.usecase.UpdateQuantityUseCase
import com.example.myshopapp.domain.usecase.UpdateSaleStatusUseCase
import com.example.myshopapp.domain.usecase.UpdateSaleTotalsUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.mapper.toMoneyBackRequest
import com.example.myshopapp.presentation.mapper.toRollbackRequest
import com.example.myshopapp.presentation.state.SaleDetailUiState
import com.example.myshopapp.presentation.util.SaleRefundCalculator
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.presentation.util.roundTo2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleDetailViewModel @Inject constructor(
    private val getSaleFullByQr: GetSaleFullByQrUseCase,
    private val getShiftUseCase: GetShiftUseCase,
    private val rollbackUseCase: RollbackUseCase,
    private val moneyBackUseCase: MoneyBackUseCase,
    private val reprintUseCase: ReprintReceiptUseCase,
    private val updateSaleStatusUseCase: UpdateSaleStatusUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase,
    private val updateSaleTotalsUseCase: UpdateSaleTotalsUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(SaleDetailUiState())
    val state = _state.asStateFlow()

    private var originalRoomItems: List<SaleItemEntity> = emptyList()

    fun load(documentId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val saleResult = getSaleFullByQr(documentId)
            if (saleResult.isFailure) {
                _state.update { it.copy(isLoading = false) }
                emitError(saleResult.exceptionOrNull()?.message)
                return@launch
            }



            val saleFull = saleResult.getOrNull() ?: run {
                _state.update { it.copy(isLoading = false) }
                emitError("Satış tapılmadı")
                return@launch
            }

            // Room-da olan cari sayları ilkin istinad (referens) kimi saxlayırıq
            originalRoomItems = saleFull.items.map { it.copy() }

            _state.update {
                it.copy(
                    isLoading = false,
                    saleFull = saleFull,
                    documentId = documentId,
                    updatedItems = saleFull.items.map { item -> item.copy() },
                    isCashless = saleFull.sale.cardSum > 0
                )
            }
            getStatus()
        }
    }

    // Ekranda say artırılır (Lakin ilkin orijinal say həddindən çox ola bilməz)
    fun plus(item: SaleItemEntity) {
        val originalItem = originalRoomItems.find { it.id == item.id } ?: return
        _state.update { currentState ->
            currentState.copy(
                updatedItems = currentState.updatedItems.map {
                    if (it.id == item.id) {
                        val newQty = (it.quantity + 1.0).coerceIn(0.0, originalItem.quantity)
                        it.copy(quantity = newQty, sum = (newQty * it.salePrice).roundTo2())
                    } else {
                        it
                    }
                }
            )
        }
    }

    // Ekranda say azaldılır (0.0-dan aşağı düşə bilməz)
    fun minus(item: SaleItemEntity) {
        _state.update { currentState ->
            currentState.copy(
                updatedItems = currentState.updatedItems.map {
                    if (it.id == item.id) {
                        val newQty = (it.quantity - 1.0).coerceIn(0.0, it.quantity)
                        it.copy(quantity = newQty, sum = (newQty * it.salePrice).roundTo2())
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun getStatus() {
        viewModelScope.launch {
            val currentShiftKey = getShiftUseCase().getOrNull()?.data?.shiftOpenTime
            val sale = state.value.saleFull?.sale
            val sameShift = currentShiftKey != null && sale?.shiftKey == currentShiftKey

            _state.update {
                it.copy(
                    canRollback = sameShift,
                    canRefund = !sameShift
                )
            }
        }
    }

    fun rollbackOrRefund() {
        if (state.value.isCashless) {
            if (state.value.canRollback) rollback() else refund()
        } else {
            refund()
        }
    }

    fun rollback() {
        val saleFull = _state.value.saleFull ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rollbackUseCase(saleFull.toRollbackRequest())
                .onSuccess { response ->
                    if (response.code == 0) {
                        updateSaleStatusUseCase(state.value.documentId, SaleStatus.ROLLED_BACK).onSuccess {
                            _state.update { s ->
                                s.copy(
                                    isLoading = false,
                                    saleFull = saleFull.copy(sale = saleFull.sale.copy(status = SaleStatus.ROLLED_BACK))
                                )
                            }
                            emitSuccess("Ləğv uğurlu!")
                            emitNavigateBack()
                        }.onFailure { e ->
                            _state.update { it.copy(isLoading = false) }
                            emitError(e.message)
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        emitError(response.message)
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }

    fun refund() {
        val saleFull = _state.value.saleFull ?: return
        val updatedItems = _state.value.updatedItems

        // 1. Hər hansı bir məhsulun sayının azaldılıb-azaldılmadığını yoxlayırıq
        val isAnyChanged = updatedItems.any { updatedItem ->
            val originalItem = originalRoomItems.find { it.id == updatedItem.id }
            originalItem != null && updatedItem.quantity < originalItem.quantity
        }

        if (!isAnyChanged) {
            emitError("Zəhmət olmasa qaytarmaq üçün məhsul sayını azaldın")
            return
        }

        // 2. Kompleks refund hesablama obyektimizi çağırırıq
        val refundTotals = SaleRefundCalculator.calculateRefund(
            originalItems = originalRoomItems,
            updatedItems = updatedItems,
            cartDiscountPercent = saleFull.sale.cartDiscount
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // 3. API Sorğusuna yalnız geri qaytarılan (azaldılmış) məhsulların hesabatını ötürürük
            moneyBackUseCase(saleFull.toMoneyBackRequest(refundTotals))
                .onSuccess { response ->
                    if (response.code != 0) {
                        _state.update { it.copy(isLoading = false) }
                        emitError(response.message)
                        return@onSuccess
                    }
                    Log.d("refund", "refundUseCase success")

                    // 4. Statusun bazada yenilənməsi
                    updateSaleStatusUseCase(state.value.documentId, SaleStatus.REFUNDED).onSuccess {

                        // 5. Room bazasında məhsulların yeni (qalan) miqdar + sum yenilənir
                        updateQuantityUseCase(refundTotals.remainingItems, saleFull.sale.documentId)

                        // 6. Hər ödəniş metodunu orijinal nisbətə proporsional azaldırıq
                        val refundRatio      = if (saleFull.sale.total > 0) refundTotals.totalRefundSum / saleFull.sale.total else 0.0
                        val newTotal         = (saleFull.sale.total        - refundTotals.totalRefundSum).roundTo2()
                        val newCashSum       = (saleFull.sale.cashSum      - (saleFull.sale.cashSum      * refundRatio).roundTo2()).roundTo2()
                        val newCardSum       = (saleFull.sale.cardSum      - (saleFull.sale.cardSum      * refundRatio).roundTo2()).roundTo2()
                        val newBonusSum      = (saleFull.sale.bonusSum     - (saleFull.sale.bonusSum     * refundRatio).roundTo2()).roundTo2()
                        val newCreditSum     = (saleFull.sale.creditSum    - (saleFull.sale.creditSum    * refundRatio).roundTo2()).roundTo2()
                        val newPrepaySum     = (saleFull.sale.prepaymentSum- (saleFull.sale.prepaymentSum* refundRatio).roundTo2()).roundTo2()

                        updateSaleTotalsUseCase(
                            documentId    = saleFull.sale.documentId,
                            total         = newTotal,
                            cashSum       = newCashSum,
                            cardSum       = newCardSum,
                            bonusSum      = newBonusSum,
                            creditSum     = newCreditSum,
                            prepaymentSum = newPrepaySum
                        )

                        // 7. UI State sinxronlaşdırılır
                        _state.update { s ->
                            s.copy(
                                isLoading = false,
                                saleFull = saleFull.copy(
                                    sale = saleFull.sale.copy(
                                        status        = SaleStatus.REFUNDED,
                                        total         = newTotal,
                                        cashSum       = newCashSum,
                                        cardSum       = newCardSum,
                                        bonusSum      = newBonusSum,
                                        creditSum     = newCreditSum,
                                        prepaymentSum = newPrepaySum
                                    ),
                                    items = refundTotals.remainingItems
                                ),
                                updatedItems = refundTotals.remainingItems
                            )
                        }
                        emitSuccess("Geri qaytarılma uğurla tamamlandı!")
                        emitNavigateBack()
                    }.onFailure { e ->
                        _state.update { it.copy(isLoading = false) }
                        emitError(e.message)
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }

    fun reprint() {
        val sale = _state.value.saleFull?.sale ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            reprintUseCase(PrintRequest(sale.fullDocumentId))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    emitSuccess("Çap uğurlu!")
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }
}