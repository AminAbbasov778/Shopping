package com.example.myshopapp.presentation.state

data class PaymentUiState(
    val total: Double = 0.0,
    val cash: String = "",
    val card: String = "",
    val bonus: String = "",
    val credit: String = "",
    val prepayment: String = "",

    val paid: Double = 0.0,
    val remaining: Double = 0.0,
    val change: Double = 0.0,

    val isLoading: Boolean = false,
)