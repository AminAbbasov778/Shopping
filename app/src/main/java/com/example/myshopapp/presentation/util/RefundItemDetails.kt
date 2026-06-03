package com.example.myshopapp.presentation.util

import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.presentation.util.roundTo2

data class RefundItemDetails(
    val entity: SaleItemEntity,
    val refundQty: Double,
    val refundSum: Double,
    val effectivePrice: Double
)

data class RefundTotals(
    val refundItems: List<RefundItemDetails>,
    val remainingItems: List<SaleItemEntity>,
    val totalRefundSum: Double,
    val vatMap: Map<Double?, Double>
)

object SaleRefundCalculator {

    fun calculateRefund(
        originalItems: List<SaleItemEntity>,
        updatedItems: List<SaleItemEntity>,
        cartDiscountPercent: Double
    ): RefundTotals {
        
        val refundItemsList = mutableListOf<RefundItemDetails>()
        val remainingItemsList = mutableListOf<SaleItemEntity>()
        
        var totalRefundSubtotal = 0.0

        // 1. Qaytarılan və Qalan miqdarları ayırırıq, ilkin cəmləri hesablayırıq
        updatedItems.forEach { updated ->
            val original = originalItems.find { it.id == updated.id } ?: return@forEach
            val refundQty = (original.quantity - updated.quantity).roundTo2()

            // Əgər bu məhsuldan geri qaytarılan miqdar varsa
            if (refundQty > 0) {
                val discountedPrice = (updated.salePrice * (1.0 - updated.itemDiscountPercent / 100.0)).roundTo2()
                val refundSum = (discountedPrice * refundQty).roundTo2()
                totalRefundSubtotal += refundSum

                refundItemsList.add(
                    RefundItemDetails(
                        entity = updated,
                        refundQty = refundQty,
                        refundSum = refundSum,
                        effectivePrice = discountedPrice
                    )
                )
            }

            // Room-da qalacaq yeni siyahı (yenilənmiş sum ilə)
            val newRemainingSum = (updated.quantity * (updated.salePrice * (1.0 - updated.itemDiscountPercent / 100.0)).roundTo2()).roundTo2()
            remainingItemsList.add(
                updated.copy(
                    quantity = updated.quantity,
                    sum = newRemainingSum
                )
            )
        }

        // 2. Kassa endiriminin proporsional nisbəti
        val cartDiscountAmount = (totalRefundSubtotal * cartDiscountPercent / 100.0).roundTo2()
        val totalRefundSum = (totalRefundSubtotal - cartDiscountAmount).roundTo2()
        val discountRatio = if (totalRefundSubtotal > 0) cartDiscountAmount / totalRefundSubtotal else 0.0

        // 3. Fiskal ƏDV (VAT) xəritəsinin hesablanması
        val vatMap = mutableMapOf<Double?, Double>()

        val finalRefundItems = refundItemsList.map { item ->
            val effectiveSum = (item.refundSum * (1.0 - discountRatio)).roundTo2()
            val finalEffectivePrice = if (item.refundQty > 0) (effectiveSum / item.refundQty).roundTo2() else item.effectivePrice

            if (item.entity.isAgro) {
                // Agro məhsul: Alış qiyməti hissəsi ƏDV-siz (null), qalan marja hissəsi 18% ƏDV
                val purchaseRatio = if (item.refundSum > 0) {
                    (item.entity.purchasePrice * item.refundQty).roundTo2() / item.refundSum
                } else 0.0

                val purchasePart = (effectiveSum * purchaseRatio).roundTo2()
                val marginPart = (effectiveSum - purchasePart).roundTo2()

                vatMap[null] = ((vatMap[null] ?: 0.0) + purchasePart).roundTo2()
                vatMap[18.0] = ((vatMap[18.0] ?: 0.0) + marginPart).roundTo2()
            } else {
                // Normal məhsul
                val key = if (item.entity.vatPercent == 0.0) null else item.entity.vatPercent
                vatMap[key] = ((vatMap[key] ?: 0.0) + effectiveSum).roundTo2()
            }

            item.copy(effectivePrice = finalEffectivePrice, refundSum = effectiveSum)
        }

        return RefundTotals(
            refundItems = finalRefundItems,
            remainingItems = remainingItemsList,
            totalRefundSum = totalRefundSum,
            vatMap = vatMap
        )
    }
}