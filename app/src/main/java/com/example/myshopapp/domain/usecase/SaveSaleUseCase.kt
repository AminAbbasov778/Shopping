package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleVatEntity
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class SaveSaleUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    suspend operator fun invoke(
        sale: SaleEntity,
        items: List<SaleItemEntity>,
        vat: List<SaleVatEntity>
    ): Result<Unit> {
        return repository.saveSale(sale, items, vat)
    }
}
