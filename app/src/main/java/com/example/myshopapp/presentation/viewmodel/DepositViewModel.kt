package com.example.myshopapp.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val getCashierNameUseCase: GetCashierNameUseCase,
    private val getLastDocumentUseCase: GetLastDocumentUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(DepositUiState())
    val state = _state.asStateFlow()

    fun setAmount(v: String) {
        _state.value = _state.value.copy(amount = v)
    }

    fun deposit( ) {
        val amount = _state.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            emitError("Məbləğ daxil edin")
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            _state.value = _state.value.copy(isLoading = true)

            Log.d("DepositViewModel", "deposit: ${_state.value.amount}")

            getLastDocumentUseCase().onSuccess {doc->

                val name = getCashierNameUseCase()
                Log.d("DepositViewModel", "depositDocsuccess: ${doc.data.doc.docNumber}${name}")

                name?.let {
                    val request = DepositRequest(
                        cashier = name,
                        currency = CURRENCY,
                        sum = amount,
                        prevDocNumber = doc.data.doc.docNumber)

                    depositUseCase(request)
                        .onSuccess {
                            Log.d("DepositViewModel", "deposit: success")
                            _state.value = _state.value.copy(isLoading = false)
                            emitSuccess("Depozit uğurlu!")
                        }
                        .onFailure {
                            _state.value = _state.value.copy(isLoading = false)
                            emitError(it.message)
                        }

                }
            }.onFailure {
                Log.d("DepositViewModel", "depositDocerrir: ${it.message}")

                _state.value = _state.value.copy(isLoading = false)
                emitError(it.message)

            }



        }
    }
}