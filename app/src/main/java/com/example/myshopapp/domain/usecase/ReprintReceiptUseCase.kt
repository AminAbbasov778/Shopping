package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class ReprintReceiptUseCase @Inject constructor(
    private val repo: SaleRepository
) {
    suspend operator fun invoke(printRequest: PrintRequest) =
        repo.reprintReceipt(printRequest)
}