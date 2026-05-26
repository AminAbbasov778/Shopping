package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.ProductEntity

data class CartItem(
    val product: ProductEntity,
    val qty: Double = 1.0,
    val discount: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val itemSum: Double = 0.0,
)