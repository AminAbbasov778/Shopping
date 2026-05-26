package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.SaleRepository
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.sale.SaleResponse
import javax.inject.Inject

class SubmitSaleUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    suspend operator fun invoke(request: SaleRequest): Result<SaleResponse> {
        return repository.submitSale(request)
    }
}
