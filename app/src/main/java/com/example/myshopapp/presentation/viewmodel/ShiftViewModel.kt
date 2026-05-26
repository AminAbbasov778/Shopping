package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.domain.usecase.CloseShiftUseCase
import com.example.myshopapp.domain.usecase.GetShiftUseCase
import com.example.myshopapp.domain.usecase.OpenShiftUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.ShiftUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val getShiftUseCase: GetShiftUseCase,
    private val openShiftUseCase: OpenShiftUseCase,
    private val closeShiftUseCase: CloseShiftUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(ShiftUiState())
    val state = _state.asStateFlow()

    fun loadShift() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true)
            }

            val result = getShiftUseCase()
            result.onSuccess { shift ->
                if(shift.code != 0){
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = shift.message
                        )
                    }
                    return@onSuccess
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        shift = shift
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun openShift() {
        viewModelScope.launch {
            val result = openShiftUseCase()
            result.onSuccess {
                if(it.code != 0){
                    _state.update { it.copy(error = it.error) }
                    return@onSuccess
                }

                loadShift()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun closeShift() {
        viewModelScope.launch {
            val result = closeShiftUseCase()
            result.onSuccess {
                if(it.code != 0){
                    _state.update { it.copy(error = it.error) }
                    return@onSuccess
                }

                loadShift()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}