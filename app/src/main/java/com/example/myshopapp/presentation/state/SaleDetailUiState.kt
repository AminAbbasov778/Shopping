package com.example.myshopapp.presentation.state

import com.example.myshopapp.data.local.entity.SaleFull

data class SaleDetailUiState(
    val saleFull: SaleFull? = null,

    val canRollback: Boolean = false,
    val canRefund: Boolean = false,
    val documentId: String = "",

    val isLoading: Boolean = false,
)