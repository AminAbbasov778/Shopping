package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.SaleEntity

data class ShiftSalesUiState(
    val isLoading: Boolean = false,
    val sales: List<SaleEntity> = emptyList(),
)