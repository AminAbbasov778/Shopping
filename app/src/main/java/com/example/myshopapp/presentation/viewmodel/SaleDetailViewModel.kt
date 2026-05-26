package com.example.myshopapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.domain.usecase.GetSaleFullByQrUseCase
import com.example.myshopapp.domain.usecase.GetShiftUseCase
import com.example.myshopapp.domain.usecase.MoneyBackUseCase
import com.example.myshopapp.domain.usecase.ReprintReceiptUseCase
import com.example.myshopapp.domain.usecase.RollbackUseCase
import com.example.myshopapp.domain.usecase.UpdateSaleStatusUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.mapper.toMoneyBackRequest
import com.example.myshopapp.presentation.mapper.toRollbackRequest
import com.example.myshopapp.presentation.state.SaleDetailUiState
import com.example.myshopapp.presentation.util.SaleStatus
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
) : BaseViewModel() {

    private val _state = MutableStateFlow(SaleDetailUiState())
    val state = _state.asStateFlow()

    fun load(documentId: String) {
        viewModelScope.launch {

            _state.update {

                it.copy(isLoading = true)
            }

            val saleResult = getSaleFullByQr(documentId)
            if (saleResult.isFailure) {
                _state.update {

                    it.copy(isLoading = false)
                }
                emitError(saleResult.exceptionOrNull()?.message)
                return@launch
            }

            val saleFull = saleResult.getOrNull() ?: run {
                _state.update {
                    it.copy(isLoading = false)
                }
                emitError("Satış tapılmadı")
                return@launch
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    saleFull = saleFull,
                    documentId = documentId,
                )
            }

            getStatus()
        }
    }

    fun getStatus() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val currentShiftKey = getShiftUseCase().getOrNull()?.data?.shiftOpenTime
            val sameShift =
                currentShiftKey != null && state.value.saleFull?.sale?.shiftKey == currentShiftKey

            _state.update {
                it.copy(
                    isLoading = false,
                    canRollback = sameShift,
                    canRefund = !sameShift,
                )
            }
        }
    }

    fun rollbackOrRefund() {
        if (_state.value.canRollback) rollback() else refund()
    }

    private fun rollback() {
        val saleFull = _state.value.saleFull ?: return

        viewModelScope.launch {


            _state.update {
                it.copy(isLoading = true)
            }

            rollbackUseCase(saleFull.toRollbackRequest())
                .onSuccess {
                    if (it.code == 0) {

                        updateSaleStatusUseCase(
                            state.value.documentId,
                            SaleStatus.ROLLED_BACK
                        ).onSuccess {


                            _state.update { state ->
                                state.copy(
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
                        emitError(it.message)
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }

    private fun refund() {
        val saleFull = _state.value.saleFull ?: return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            moneyBackUseCase(saleFull.toMoneyBackRequest())
                .onSuccess {

                    if(it.code != 0){
                        _state.update { it.copy(isLoading = false) }
                        emitError(it.message)
                        return@onSuccess
                    }

                    updateSaleStatusUseCase(state.value.documentId, SaleStatus.REFUNDED).onSuccess {
                        _state.update { state ->

                            state.copy(
                                isLoading = false,
                                saleFull = saleFull.copy(sale = saleFull.sale.copy(status = SaleStatus.REFUNDED))
                            )
                        }
                        emitSuccess("Ləğv uğurlu!")
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
            _state.update {
                it.copy(isLoading = true)
            }

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