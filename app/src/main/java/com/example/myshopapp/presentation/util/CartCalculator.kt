package com.example.myshopapp.presentation.util

import com.example.myshopapp.presentation.model.CartTotals
import com.example.myshopapp.presentation.state.CartItem

object CartCalculator {

    fun calculate(items: List<CartItem>, cartDiscountPercent: Double): CartTotals {

        val itemsWithItemDiscount = items.map { item ->

            val unitPrice =
                (item.product.salePrice * (1.0 - item.discount / 100.0)).roundTo2()

            val itemTotal =
                (unitPrice * item.qty).roundTo2()

            item.copy(
                discountedPrice = unitPrice,
                itemSum = itemTotal
            )
        }

        val totalBeforeCartDiscount =
            itemsWithItemDiscount.sumOf { it.itemSum }.roundTo2()

        val cartDiscountAmount =
            (totalBeforeCartDiscount * cartDiscountPercent / 100.0).roundTo2()

        val total =
            (totalBeforeCartDiscount - cartDiscountAmount).roundTo2()

        val totalDiscount =
            (totalBeforeCartDiscount - total).roundTo2()

        val cartDiscountRatio =
            if (totalBeforeCartDiscount > 0)
                cartDiscountAmount / totalBeforeCartDiscount
            else 0.0

        val vatMap = mutableMapOf<Double?, Double>()

        val finalItems = itemsWithItemDiscount.map { item ->

            val itemTotalAfterCartDiscount =
                (item.itemSum * (1.0 - cartDiscountRatio)).roundTo2()

            val unitPriceAfterCartDiscount =
                (item.discountedPrice * (1.0 - cartDiscountRatio)).roundTo2()

            if (item.product.isAgro) {

                val purchaseRatio =
                    if (item.itemSum > 0)
                        (item.product.purchasePrice * item.qty).roundTo2() /
                                item.itemSum
                    else 0.0

                val purchasePart =
                    (itemTotalAfterCartDiscount * purchaseRatio).roundTo2()

                val marginPart =
                    (itemTotalAfterCartDiscount - purchasePart).roundTo2()

                vatMap[null] =
                    ((vatMap[null] ?: 0.0) + purchasePart).roundTo2()

                vatMap[18.0] =
                    ((vatMap[18.0] ?: 0.0) + marginPart).roundTo2()

            } else {

                val vatKey =
                    if (item.product.vatPercent == 0.0)
                        null
                    else
                        item.product.vatPercent

                vatMap[vatKey] =
                    ((vatMap[vatKey] ?: 0.0) + itemTotalAfterCartDiscount).roundTo2()
            }

            item.copy(
                discountedPrice = unitPriceAfterCartDiscount,
                itemSum = itemTotalAfterCartDiscount
            )
        }

        return CartTotals(
            calculatedItems = finalItems,
            subtotal = totalBeforeCartDiscount,
            cartDiscountAmount = cartDiscountAmount,
            total = total,
            totalDiscount = totalDiscount,
            vatMap = vatMap
        )
    }
}