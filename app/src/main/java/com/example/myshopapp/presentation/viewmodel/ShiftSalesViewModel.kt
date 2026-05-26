package com.example.myshopapp.presentation.viewmodel

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftSalesViewModel @Inject constructor(
    private val getShiftSalesUseCase: GetShiftSalesUseCase,
    private val getShiftUseCase: GetShiftUseCase,
    private val reprintReceiptUseCase: ReprintReceiptUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(ShiftSalesUiState())
    val state = _state.asStateFlow()

    fun loadShiftSales() {
        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            val shiftResult = getShiftUseCase()

            shiftResult.onSuccess { shift ->
                if(shift.code == 0){

                    getShiftSalesUseCase(shift.data.shiftOpenTime).collect { result ->
                        result.onSuccess { list ->



                            _state.update {

                                it.copy(
                                    isLoading = false,
                                    sales = list.map { it.sale }
                                )
                            }
                        }

                        result.onFailure {

                            _state.update { it.copy(isLoading = false) }
                            emitError(it.message)
                        }
                    }
                }else{
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    emitError(shift.message)
                }

            }

            shiftResult.onFailure {
                _state.update {
                    it.copy(isLoading = false)
                }
                emitError(it.message)
            }
        }
    }

    fun reprintReceipt(fiscalId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = reprintReceiptUseCase(PrintRequest(fiscalId))
            result.onSuccess {

                if(it.code == 0){
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    emitSuccess("Çap uğurlu!")
                }else{
                    _state.update {

                        it.copy(isLoading = false)
                    }

                    emitError(it.message)
                }



            }.onFailure {
                _state.update {

                    it.copy(isLoading = false)
                }

                emitError(it.message)
            }
        }
    }
}