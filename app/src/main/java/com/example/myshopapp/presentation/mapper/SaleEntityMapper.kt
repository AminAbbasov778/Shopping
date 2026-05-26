package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleVatEntity
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.sale.SaleResponse

fun SaleResponse.toSaleEntity(
    shiftOpenTime: String,
    cashierName: String,
    request: SaleRequest,
): SaleEntity = SaleEntity(
    documentId      = data.documentId,
    shortDocumentId = data.shortDocumentId,
    fullDocumentId  = data.documentId,
    shiftId         = data.shiftDocumentNumber.toString(),
    shiftKey        = shiftOpenTime,
    cashier         = cashierName,
    currency        = CURRENCY,
    total           = request.sum,
    cashSum         = request.cashSum,
    cardSum         = request.cashlessSum,
    bonusSum        = request.bonusSum,
    creditSum       = request.creditSum,
    prepaymentSum   = request.prepaymentSum,
    incomingSum     = request.incomingSum,
    changeSum       = request.changeSum,
    rrn             = rrn,
    uuid            = uuid,
    status          = SaleStatus.COMPLETED,
)

fun SaleRequest.toSaleItemEntities(documentId: String): List<SaleItemEntity> =
    items.map { item ->
        SaleItemEntity(
            saleDocumentId = documentId,
            itemCode       = item.itemCode,
            itemName       = item.itemName,
            quantity       = item.itemQuantity,
            price          = item.itemPrice,
            sum            = item.itemSum,
            vatPercent     = item.itemVatPercent,
        )
    }

fun SaleRequest.toVatEntities(documentId: String): List<SaleVatEntity> =
    vatAmounts.map { vat ->
        SaleVatEntity(
            saleDocumentId = documentId,
            vatPercent     = vat.vatPercent,
            vatSum         = vat.vatSum,
        )
    }