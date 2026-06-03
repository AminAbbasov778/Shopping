package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.presentation.state.CartItem
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.sale.SaleResponse

fun SaleResponse.toSaleEntity(
    shiftOpenTime: String,
    cashierName: String,
    request: SaleRequest,
    cartDiscount: Double,
): SaleEntity = SaleEntity(
    documentId = data.documentId,
    shortDocumentId = data.shortDocumentId,
    fullDocumentId = data.documentId,
    shiftId = data.shiftDocumentNumber.toString(),
    shiftKey = shiftOpenTime,
    cashier = cashierName,
    currency = CURRENCY,
    total = request.sum,
    cashSum = request.cashSum,
    cardSum = request.cashlessSum,
    bonusSum = request.bonusSum,
    creditSum = request.creditSum,
    prepaymentSum = request.prepaymentSum,
    incomingSum = request.incomingSum,
    changeSum = request.changeSum,
    rrn = rrn,
    uuid = uuid,
    status = SaleStatus.COMPLETED,
    cartDiscount = cartDiscount,
)

fun CartItem.toSaleItemEntities(documentId: String):SaleItemEntity {

    val item = this
    return SaleItemEntity(
        saleDocumentId = documentId,
        itemCode = item.product.code,
        itemName = item.product.name,
        quantity = item.qty,
        salePrice = item.product.salePrice,
        sum = item.itemSum,
        vatPercent = item.product.vatPercent,
        isAgro = item.product.isAgro,
        purchasePrice = item.product.purchasePrice,
        itemDiscountPercent = item.discount,
    )

}
