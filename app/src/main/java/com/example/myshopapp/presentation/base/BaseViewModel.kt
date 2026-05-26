package com.example.myshopapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.presentation.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    protected fun emitError(message: String?) {
        if (!message.isNullOrBlank()) {
            viewModelScope.launch { _events.send(UiEvent.ShowError(message)) }
        }
    }

    protected fun emitSuccess(message: String = "") {
        viewModelScope.launch { _events.send(UiEvent.ShowSuccess(message)) }
    }

    protected fun emitNavigateBack() {
        viewModelScope.launch { _events.send(UiEvent.NavigateBack) }
    }
}
