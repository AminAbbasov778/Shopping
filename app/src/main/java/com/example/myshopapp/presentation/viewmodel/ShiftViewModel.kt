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
            _state.value = _state.value.copy(isLoading = true)


            val result = getShiftUseCase()
            result.onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    shift = it
                )
            }.onFailure {

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }


    fun openShift() {
        viewModelScope.launch {
            val result = openShiftUseCase()
            result.onSuccess {

                loadShift()
            }.onFailure { throwable ->

                _state.update { it.copy(error = throwable.message) }
            }
        }
    }

    fun closeShift() {
        viewModelScope.launch {
            val result = closeShiftUseCase()
            result.onSuccess {

                loadShift()
            }.onFailure { throwable ->

                _state.update { it.copy(error = throwable.message) }
            }
        }
    }

}
