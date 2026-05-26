package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.ProductEntity

data class ProductUiState(
    val isLoading: Boolean = false,
    val editProduct: ProductEntity? = null,
    val isFormPopulated: Boolean = false,
    val nameError: String? = null,
    val codeError: String? = null,
    val barcodeError: String? = null,
    val salePriceError: String? = null,
    val vatError: String? = null,
    val purchasePriceError: String? = null
)
