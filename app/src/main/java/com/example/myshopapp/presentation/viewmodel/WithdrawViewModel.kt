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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val getLastDocumentUseCase: GetLastDocumentUseCase,
    private val getCashierNameUseCase: GetCashierNameUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(WithdrawUiState())
    val state = _state.asStateFlow()

    fun setAmount(v: String) {
        _state.value = _state.value.copy(amount = v)
    }

    fun withdraw() {
        val amount = _state.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            emitError("Məbləğ daxil edin")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            getLastDocumentUseCase().onSuccess { doc ->
                val cashier = getCashierNameUseCase()
                cashier?.let {
                    val request = WithDrawRequest(
                        cashier       = cashier,
                        currency      = CURRENCY,
                        sum           = amount,
                        prevDocNumber = doc.data.doc.docNumber
                    )

                    withdrawUseCase(request)
                        .onSuccess {
                            _state.value = _state.value.copy(isLoading = false)
                            emitSuccess("Çıxarma uğurlu!")
                        }
                        .onFailure {
                            _state.value = _state.value.copy(isLoading = false)
                            emitError(it.message)
                        }
                }

            }.onFailure{
                _state.value = _state.value.copy(isLoading = false)
                emitError(it.message)
            }



        }
    }
}