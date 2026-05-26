package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.domain.usecase.GetShiftSalesUseCase
import com.example.myshopapp.domain.usecase.GetShiftUseCase
import com.example.myshopapp.domain.usecase.ReprintReceiptUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.ShiftSalesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftSalesViewModel @Inject constructor(
    private val getShiftSalesUseCase: GetShiftSalesUseCase,
    private val getShiftUseCase: GetShiftUseCase,
    private val reprintReceiptUseCase: ReprintReceiptUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(ShiftSalesUiState())
    val state = _state.asStateFlow()


    fun loadShiftSales() {
        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true)

            val shiftResult = getShiftUseCase()

            shiftResult.onSuccess { shift ->


                getShiftSalesUseCase(shift.data.shiftOpenTime).collect { result ->

                    result.onSuccess { list ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            sales = list.map { it.sale }
                        )
                    }

                    result.onFailure {
                        _state.value = _state.value.copy(isLoading = false)
                        emitError(it.message)
                    }
                }
            }

            shiftResult.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                emitError(it.message)
            }
        }
    }

    fun reprintReceipt(fiscalId : String){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result =  reprintReceiptUseCase(PrintRequest(fiscalId))
            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                emitSuccess("Çap uğurlu!")
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                emitError(it.message)
            }
        }
    }
}