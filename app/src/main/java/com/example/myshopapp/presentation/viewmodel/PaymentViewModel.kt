package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.domain.usecase.GetCashierNameUseCase
import com.example.myshopapp.domain.usecase.GetShiftUseCase
import com.example.myshopapp.domain.usecase.SaveSaleUseCase
import com.example.myshopapp.domain.usecase.SubmitSaleUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.mapper.toSaleEntity
import com.example.myshopapp.presentation.mapper.toSaleItemEntities
import com.example.myshopapp.presentation.mapper.toVatEntities
import com.example.myshopapp.presentation.state.CartItem
import com.example.myshopapp.presentation.state.PaymentUiState
import com.example.myshopapp.presentation.util.validateFullSaleRequest
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val submitSaleUseCase: SubmitSaleUseCase,
    private val saveSaleUseCase: SaveSaleUseCase,
    private val getShiftUseCase: GetShiftUseCase,
    private val getCashierNameUseCase: GetCashierNameUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(PaymentUiState())
    val state = _state.asStateFlow()

    fun setTotal(total: Double) {
        _state.value = _state.value.copy(total = total)
        recalcalculate()
    }

    fun setCash(cash: String) {
        _state.value = _state.value.copy(cash = cash)
        recalcalculate()
    }

    fun setCard(card: String) {
        _state.value = _state.value.copy(card = card)
        recalcalculate()
    }

    fun setBonus(bonus: String) {
        _state.value = _state.value.copy(bonus = bonus)
        recalcalculate()
    }



    private fun recalcalculate() {
        val state = _state.value
        val cash = state.cash.toDoubleOrNull() ?: 0.0
        val card = state.card.toDoubleOrNull() ?: 0.0
        val bonus = state.bonus.toDoubleOrNull() ?: 0.0

        val paid = cash + card + bonus
        val remaining = (state.total - paid).coerceAtLeast(0.0)
        val change = (paid - state.total).coerceAtLeast(0.0)

        _state.value = state.copy(paid = paid, remaining = remaining, change = change)
    }

    fun submitSale(request: SaleRequest, cartItems: List<CartItem>) {
        val errors = validateFullSaleRequest(
            cashier = "",
            currency = CURRENCY,
            cartItems = cartItems,
            cashSum = request.cashSum,
            cashlessSum = request.cashlessSum,
            bonusSum = request.bonusSum,
            creditSum = request.creditSum,
            prepaymentSum = request.prepaymentSum,
            incomingSum = request.incomingSum,
            changeSum = request.changeSum,
            totalSum = request.sum,
            vatAmounts = request.vatAmounts,

            ).filterNot { it.contains("Kassir") }


        if (errors.isNotEmpty()) {

            emitError(errors.first())
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)

            val name = getCashierNameUseCase() ?: return@launch

            val submitResult = submitSaleUseCase(request.copy(cashier = name))

            if (submitResult.isFailure || submitResult.getOrNull()?.data == null) {
                Log.e("submitSale", "submitSale: ${submitResult.exceptionOrNull()?.message}")

                _state.value = _state.value.copy(isLoading = false)

                emitError(submitResult.exceptionOrNull()?.message)

                emitNavigateBack()


                return@launch
            }

            val response = submitResult.getOrThrow()

            Log.e("submitSale", "submitSalesuccess: ${response.data.documentId}")


            val shiftResult = getShiftUseCase()
            if (shiftResult.isFailure) {

                _state.value = _state.value.copy(isLoading = false)
                emitError(shiftResult.exceptionOrNull()?.message)



                emitNavigateBack()
                return@launch
            }
            val shift = shiftResult.getOrThrow()

            val saleEntity = response.toSaleEntity(shift.data.shiftOpenTime, name, _state.value)
            val items = request.toSaleItemEntities(response.data.documentId)

            val vat = request.toVatEntities(response.data.documentId)

            val saveResult = saveSaleUseCase(saleEntity, items, vat)
            _state.value = _state.value.copy(isLoading = false)

            if (saveResult.isSuccess) {
                emitSuccess("Ödəniş uğurlu!")

                emitNavigateBack()

            } else {
                emitError(saveResult.exceptionOrNull()?.message)
                emitNavigateBack()

            }
        }
    }
}