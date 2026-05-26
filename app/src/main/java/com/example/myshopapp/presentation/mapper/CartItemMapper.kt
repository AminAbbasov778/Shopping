package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.remote.model.request.sale.Item
import com.example.myshopapp.presentation.state.CartItem

fun CartItem.toRequestItem(creditSum: Double, cartSize: Int): Item {

    val vatPercent: Double?
    val marginPrice: Double?
    val marginSum: Double?

    when {
        product.isAgro -> {
            vatPercent  = 18.0
            marginPrice = product.salePrice - product.purchasePrice
            marginSum   = marginPrice * qty
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
        itemQuantity      = qty,
        itemPrice         = discountedPrice,
        itemDiscountPrice = if (discount > 0) product.salePrice - discountedPrice else null,
        itemSum           = itemSum,
        itemVatPercent    = vatPercent,
        itemMarginPrice   = marginPrice,
        itemMarginSum     = marginSum,
        itemCreditpaySum  = if (creditSum > 0 && cartSize > 1) itemSum else null,
    )
}