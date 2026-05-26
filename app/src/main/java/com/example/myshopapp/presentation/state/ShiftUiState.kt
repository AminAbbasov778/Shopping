package com.example.myshopapp.presentation.state


import com.example.myshopapp.data.remote.model.repsonse.shift.status.ShiftStatus

data class ShiftUiState(
    val isLoading: Boolean = false,
    val shift: ShiftStatus? = null,
    val error: String? = null
)