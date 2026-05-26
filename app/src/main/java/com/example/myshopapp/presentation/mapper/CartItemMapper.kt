package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.remote.model.request.sale.Item
import com.example.myshopapp.presentation.state.CartItem
import com.example.myshopapp.presentation.util.roundTo2
import com.example.myshopapp.presentation.util.roundTo3

fun CartItem.toRequestItem(creditSum: Double, cartSize: Int): Item {

    val price         = discountedPrice.roundTo2()
    val sum           = itemSum.roundTo2()
    val salePrice     = product.salePrice.roundTo2()
    val purchasePrice = product.purchasePrice.roundTo2()
    val quantity      = qty.roundTo3()

    val vatPercent: Double?
    val marginPrice: Double?
    val marginSum: Double?

    when {
        product.isAgro -> {
            vatPercent  = 18.0
            marginPrice = (salePrice - purchasePrice).roundTo2()
            marginSum   = (marginPrice * quantity).roundTo2()
        }
        product.vatPercent == 0.0 -> {
            vatPercent  = null
            marginPrice = null
            marginSum   = null
        }
        else -> {
            vatPercent  = product.vatPercent
            marginPrice = null
            marginSum   = null
        }
    }

    val codeType = when (product.barcode.length) {
        8    -> 1
        13   -> 2
        else -> 0
    }

    return Item(
        itemName          = product.name,
        itemCodeType      = codeType,
        itemCode          = product.code.take(32),
        itemQuantityType  = 0,
        itemQuantity      = quantity,
        itemPrice         = price,
        itemDiscountPrice = if (discount > 0) (salePrice - price).roundTo2() else null,
        itemSum           = sum,
        itemVatPercent    = vatPercent,
        itemMarginPrice   = marginPrice,
        itemMarginSum     = marginSum,
        itemCreditpaySum  = if (creditSum > 0 && cartSize > 1) sum else null,
    )
}