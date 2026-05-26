package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.domain.usecase.LoginUseCase
import com.example.myshopapp.domain.usecase.SaveSessionUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) : BaseViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _navigate = MutableSharedFlow<Unit>()
    val navigate = _navigate.asSharedFlow()

    fun login(cashierName: String) {

        viewModelScope.launch {

            _loading.update { true }

            loginUseCase()
                .onSuccess { response ->

                    saveSessionUseCase(
                        response.data.accessToken,
                        cashierName
                    )

                    _navigate.emit(Unit)
                }
                .onFailure {
                    Log.e("LoginViewModel", "login: ${it.message}")
                    _loading.update { false }
                    emitError(it.message)
                }
        }
    }
}