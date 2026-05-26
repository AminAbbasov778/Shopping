package com.example.myshopapp.presentation.model

import com.example.myshopapp.presentation.state.CartItem

data class CartTotals(
    val calculatedItems: List<CartItem>,
    val subtotal: Double,
    val cartDiscountAmount: Double,
    val total: Double,
    val totalDiscount: Double,
    val vatMap: Map<Double?, Double>,
    )