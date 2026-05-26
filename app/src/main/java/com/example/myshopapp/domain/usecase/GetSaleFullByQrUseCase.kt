package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class GetSaleFullByQrUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    suspend operator fun invoke(qr: String): Result<SaleFull?> {
        return repository.getSaleFullByQr(qr)
    }
}
