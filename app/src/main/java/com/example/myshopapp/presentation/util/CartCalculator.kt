package com.example.myshopapp.presentation.util

import com.example.myshopapp.presentation.state.CartItem
import com.example.myshopapp.util.round2

object CartCalculator {

    data class CartTotals(
        val calculatedItems: List<CartItem>,
        val subtotal: Double,
        val cartDiscountAmount: Double,
        val total: Double,
        val totalDiscount: Double,
        val vatMap: Map<Double?, Double>,
    )

    fun calculate(items: List<CartItem>, cartDiscountPercent: Double): CartTotals {

        val calculated = items.map { item ->
            val discountedPrice = (item.product.salePrice * (1.0 - item.discount / 100.0)).round2()

            val itemSum         = (discountedPrice * item.qty).round2()

            item.copy(discountedPrice = discountedPrice, itemSum = itemSum)

        }

        val subtotal           = calculated.sumOf { it.itemSum }.round2()
        val cartDiscountAmount = (subtotal * cartDiscountPercent / 100.0).round2()

        val total              = (subtotal - cartDiscountAmount).round2()

        val rawSubtotal        = calculated.sumOf { it.product.salePrice * it.qty }.round2()

        val totalDiscount      = (rawSubtotal - total).round2()

        val discountRatio = if (subtotal > 0) cartDiscountAmount / subtotal else 0.0
        val vatMap = mutableMapOf<Double?, Double>()

        calculated.forEach { item ->
            val effectiveSum = (item.itemSum * (1.0 - discountRatio)).round2()

            if (item.product.isAgro) {
                val purchaseRatio = if (item.itemSum > 0)
                    (item.product.purchasePrice * item.qty).round2() / item.itemSum
                else 0.0

                val purchasePart = (effectiveSum * purchaseRatio).round2()

                val marginPart   = (effectiveSum - purchasePart).round2()

                vatMap[null] = ((vatMap[null] ?: 0.0) + purchasePart).round2()
                vatMap[18.0] = ((vatMap[18.0] ?: 0.0) + marginPart).round2()
            } else {
                val key = if (item.product.vatPercent == 0.0) null else item.product.vatPercent
                vatMap[key] = ((vatMap[key] ?: 0.0) + effectiveSum).round2()
            }
        }

        return CartTotals(
            calculatedItems    = calculated,
            subtotal           = subtotal,
            cartDiscountAmount = cartDiscountAmount,
            total              = total,
            totalDiscount      = totalDiscount,
            vatMap             = vatMap,
        )
    }
}