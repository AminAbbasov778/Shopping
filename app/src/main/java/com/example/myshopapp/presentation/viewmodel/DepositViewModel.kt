package com.example.myshopapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.myshopapp.domain.usecase.DepositUseCase
import com.example.myshopapp.domain.usecase.GetCashierNameUseCase
import com.example.myshopapp.domain.usecase.GetLastDocumentUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.DepositUiState
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.deposit.DepositRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val getCashierNameUseCase: GetCashierNameUseCase,
    private val getLastDocumentUseCase: GetLastDocumentUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(DepositUiState())
    val state = _state.asStateFlow()

    fun setAmount(v: String) {
        _state.update { it.copy(amount = v) }
    }

    fun deposit() {
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

            val name = getCashierNameUseCase()
            if (name == null) {
                _state.update { it.copy(isLoading = false) }
                emitError("Kassir adı tapılmadı")
                return@launch
            }

            val request = DepositRequest(
                cashier = name,
                currency = CURRENCY,
                sum = amount,
                prevDocNumber = docResponse.data.doc.docNumber
            )

            val depositResult = depositUseCase(request)
            val depositResponse = depositResult.getOrNull()

            if (depositResult.isFailure || depositResponse?.data == null || depositResponse.code != 0) {
                _state.update { it.copy(isLoading = false) }
                emitError(depositResponse?.message ?: depositResult.exceptionOrNull()?.message)
                return@launch
            }

            _state.update { it.copy(isLoading = false) }
            emitSuccess("Depozit uğurlu!")
        }
    }
}