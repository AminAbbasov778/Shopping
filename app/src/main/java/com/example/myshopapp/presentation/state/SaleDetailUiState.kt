package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.local.entity.SaleItemEntity

data class SaleDetailUiState(
    val isLoading: Boolean = false,
    val saleFull: SaleFull? = null,
    val documentId: String = "",
    val updatedItems: List<SaleItemEntity> = emptyList(),
    val canRollback: Boolean = false,
    val canRefund: Boolean = false,
    val isCashless: Boolean = false
)