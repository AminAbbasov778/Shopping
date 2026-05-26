package com.example.myshopapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.myshopapp.data.remote.model.request.withdraw.WithDrawRequest
import com.example.myshopapp.domain.usecase.GetCashierNameUseCase
import com.example.myshopapp.domain.usecase.GetLastDocumentUseCase
import com.example.myshopapp.domain.usecase.WithdrawUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.WithdrawUiState
import com.example.myshopapp.util.Constants.CURRENCY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val getLastDocumentUseCase: GetLastDocumentUseCase,
    private val getCashierNameUseCase: GetCashierNameUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(WithdrawUiState())
    val state = _state.asStateFlow()

    fun setAmount(v: String) {
        _state.update { it.copy(amount = v) }
    }

    fun withdraw() {
        val amount = _state.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            emitError("Məbləğ daxil edin")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val docResult = getLastDocumentUseCase()
            val docResponse = docResult.getOrNull()

            if (docResult.isFailure || docResponse?.data == null || docResponse.code != 0) {
                _state.update { it.copy(isLoading = false) }
                emitError(docResponse?.message ?: docResult.exceptionOrNull()?.message)
                return@launch
            }

            val cashier = getCashierNameUseCase()
            if (cashier == null) {
                _state.update { it.copy(isLoading = false) }
                emitError("Kassir adı tapılmadı")
                return@launch
            }

            val request = WithDrawRequest(
                cashier = cashier,
                currency = CURRENCY,
                sum = amount,
                prevDocNumber = docResponse.data.doc.docNumber
            )

            val withdrawResult = withdrawUseCase(request)
            val withdrawResponse = withdrawResult.getOrNull()

            if (withdrawResult.isFailure || withdrawResponse?.data == null || withdrawResponse.code != 0) {
                _state.update { it.copy(isLoading = false) }
                emitError(withdrawResponse?.message ?: withdrawResult.exceptionOrNull()?.message)
                return@launch
            }

            _state.update { it.copy(isLoading = false) }
            emitSuccess("Çıxarma uğurlu!")
        }
    }
}