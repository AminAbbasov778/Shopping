package com.example.myshopapp.presentation.state

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val cartDiscountPercent: Double = 0.0,
    val cartDiscountAmount: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val vatSummary: Map<Double?, Double> = emptyMap(),
    val total: Double = 0.0,
    val netTotal: Double = 0.0
)
