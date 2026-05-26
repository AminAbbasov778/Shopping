package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.SaleRepository
import com.example.myshopapp.presentation.util.SaleStatus
import javax.inject.Inject

class UpdateSaleStatusUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    suspend operator fun invoke(
        documentId: String,
        status: SaleStatus
    ) = repository.updateStatus(documentId, status)

}