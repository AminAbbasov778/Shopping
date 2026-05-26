package com.example.myshopapp.presentation.state

data class WithdrawUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
)