package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.util.safeDbCall
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject
import kotlin.collections.forEach

class UpdateQuantityUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
     suspend operator fun invoke (items: List<SaleItemEntity>, saleDocumentId: String): Result<Unit> = saleRepository.updateItemsQuantities(items,saleDocumentId)

}