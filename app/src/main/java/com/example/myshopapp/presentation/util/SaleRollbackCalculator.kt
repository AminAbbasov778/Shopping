package com.example.myshopapp.presentation.util

import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.local.entity.SaleItemEntity

data class RollbackDetails(
    val entity: SaleItemEntity,
    val rollbackQty: Double,
    val refundSum: Double,
    val refundNetUnitPrice: Double,
)

object SaleRollbackCalculator {
    fun calculate(sale : SaleFull){
        sale.items.forEach { item ->
            val discountedPrice = item.salePrice * (1 - item.itemDiscountPercent / 100.0)
            val itemSum = discountedPrice * item.quantity
        }

    }
}