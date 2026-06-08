package com.example.myshopapp.presentation.util

import com.example.myshopapp.data.local.entity.SaleItemEntity

data class RefundItemDetails(
    val entity: SaleItemEntity,
    val refundQty: Double,
    val refundSum: Double,
    val refundNetUnitPrice: Double,
)

data class RefundTotals(
    val refundItems: List<RefundItemDetails>,
    val remainingItems: List<SaleItemEntity>,
    val totalRefundSum: Double,
    val vatMap: Map<Double?, Double>,
)

object SaleRefundCalculator {

    fun calculateRefund(
        originalItems: List<SaleItemEntity>,
        updatedItems: List<SaleItemEntity>,
        cartDiscountPercent: Double,
    ): RefundTotals {

        val refundItems = mutableListOf<RefundItemDetails>()
        val remainingItems = mutableListOf<SaleItemEntity>()

        var refundBeforeCartDiscount = 0.0

        // 1. Refund və qalan məhsullar
        updatedItems.forEach { updated ->

            val original = originalItems.find { it.id == updated.id } ?: return@forEach

            val refundQty = (original.quantity - updated.quantity).roundTo2()

            if (refundQty > 0) {

                val unitPriceAfterItemDiscount =
                    (updated.salePrice * (1.0 - updated.itemDiscountPercent / 100.0)).roundTo2()

                val refundItemAmount = (unitPriceAfterItemDiscount * refundQty).roundTo2()

                refundBeforeCartDiscount += refundItemAmount

                refundItems.add(
                    RefundItemDetails(
                        entity = updated,
                        refundQty = refundQty,
                        refundSum = refundItemAmount,
                        refundNetUnitPrice = unitPriceAfterItemDiscount
                    )
                )
            }

            val remainingItemAmount =
                (updated.quantity * (updated.salePrice * (1.0 - updated.itemDiscountPercent / 100.0)).roundTo2()).roundTo2()

            remainingItems.add(
                updated.copy(
                    quantity = updated.quantity, sum = remainingItemAmount
                )
            )
        }

        // 2. Cart discount
        val cartDiscountAmount = (refundBeforeCartDiscount * cartDiscountPercent / 100.0).roundTo2()

        val totalRefundAmount = (refundBeforeCartDiscount - cartDiscountAmount).roundTo2()

        val cartDiscountRatio =
            if (refundBeforeCartDiscount > 0) cartDiscountAmount / refundBeforeCartDiscount
            else 0.0

        // 3. VAT map
        val vatMap = mutableMapOf<Double?, Double>()

        val finalRefundItems = refundItems.map { item ->

            val refundNetAmount = (item.refundSum * (1.0 - cartDiscountRatio)).roundTo2()

            val refundNetUnitPrice =
                if (item.refundQty > 0) (refundNetAmount / item.refundQty).roundTo2()
                else item.refundNetUnitPrice

            if (item.entity.isAgro) {


                val purchaseAmount = (item.entity.purchasePrice * item.refundQty).roundTo2()

                val marginAmount = (refundNetAmount - purchaseAmount).roundTo2()

                vatMap[null] = ((vatMap[null] ?: 0.0) + purchaseAmount).roundTo2()

                vatMap[18.0] = ((vatMap[18.0] ?: 0.0) + marginAmount).roundTo2()

            } else {

                val vatKey = if (item.entity.vatPercent == 0.0) null
                else item.entity.vatPercent

                vatMap[vatKey] = ((vatMap[vatKey] ?: 0.0) + refundNetAmount).roundTo2()
            }

            item.copy(
                refundSum = refundNetAmount, refundNetUnitPrice = refundNetUnitPrice
            )
        }

        return RefundTotals(
            refundItems = finalRefundItems,
            remainingItems = remainingItems,
            totalRefundSum = totalRefundAmount,
            vatMap = vatMap
        )
    }
}