package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.SaleEntity

data class QrScanUiState(
    val sale: SaleEntity? = null,
    val isLoading: Boolean = false,
)