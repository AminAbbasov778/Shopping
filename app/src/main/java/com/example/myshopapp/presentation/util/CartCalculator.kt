package com.example.myshopapp.presentation.util

import com.example.myshopapp.presentation.model.CartTotals
import com.example.myshopapp.presentation.state.CartItem

object CartCalculator {

    fun calculate(items: List<CartItem>, cartDiscountPercent: Double): CartTotals {

        val calculated = items.map { item ->
            val discountedPrice = (item.product.salePrice * (1.0 - item.discount / 100.0)).roundTo2()
            val itemSum         = (discountedPrice * item.qty).roundTo2()
            item.copy(discountedPrice = discountedPrice, itemSum = itemSum)
        }

        val subtotal           = calculated.sumOf { it.itemSum }.roundTo2()
        val cartDiscountAmount = (subtotal * cartDiscountPercent / 100.0).roundTo2()
        val total              = (subtotal - cartDiscountAmount).roundTo2()
        val rawSubtotal        = calculated.sumOf { it.product.salePrice * it.qty }.roundTo2()
        val totalDiscount      = (rawSubtotal - total).roundTo2()

        val discountRatio = if (subtotal > 0) cartDiscountAmount / subtotal else 0.0
        val vatMap = mutableMapOf<Double?, Double>()

        val finalItems = calculated.map { item ->
            val effectiveSum   = (item.itemSum * (1.0 - discountRatio)).roundTo2()
            val effectivePrice = (item.discountedPrice * (1.0 - discountRatio)).roundTo2()

            if (item.product.isAgro) {
                val purchaseRatio = if (item.itemSum > 0)
                    (item.product.purchasePrice * item.qty).roundTo2() / item.itemSum
                else 0.0

                val purchasePart = (effectiveSum * purchaseRatio).roundTo2()
                val marginPart   = (effectiveSum - purchasePart).roundTo2()

                vatMap[null] = ((vatMap[null] ?: 0.0) + purchasePart).roundTo2()
                vatMap[18.0] = ((vatMap[18.0] ?: 0.0) + marginPart).roundTo2()
            } else {
                val key = if (item.product.vatPercent == 0.0) null else item.product.vatPercent
                vatMap[key] = ((vatMap[key] ?: 0.0) + effectiveSum).roundTo2()
            }

            item.copy(discountedPrice = effectivePrice, itemSum = effectiveSum)
        }

        return CartTotals(
            calculatedItems    = finalItems,
            subtotal           = subtotal,
            cartDiscountAmount = cartDiscountAmount,
            total              = total,
            totalDiscount      = totalDiscount,
            vatMap             = vatMap,
        )
    }
}