package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.ProductEntity

data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<ProductEntity> = emptyList(),
    val filteredProducts: List<ProductEntity> = emptyList(),
)