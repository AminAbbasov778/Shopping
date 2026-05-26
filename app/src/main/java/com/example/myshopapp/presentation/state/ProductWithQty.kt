package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.ProductEntity

data class ProductWithQty(
    val product: ProductEntity,
    val qty: Double = 0.0
) {
    val totalAmount: Double get() = product.salePrice * qty
}
