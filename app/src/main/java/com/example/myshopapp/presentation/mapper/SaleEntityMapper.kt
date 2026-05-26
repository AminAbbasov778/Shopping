package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleVatEntity
import com.example.myshopapp.presentation.state.PaymentUiState
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.sale.SaleResponse

fun SaleResponse.toSaleEntity(
    shiftOpenTime: String,
    cashierName: String,
    state: PaymentUiState,
): SaleEntity = SaleEntity(
    documentId      = data.documentId,
    shortDocumentId = data.shortDocumentId,
    fullDocumentId  = data.documentId,
    shiftId         = data.shiftDocumentNumber.toString(),
    shiftKey        = shiftOpenTime,
    cashier         = cashierName,
    currency        = CURRENCY,
    total           = state.total,
    cashSum         = state.cash.toDoubleOrNull()       ?: 0.0,
    cardSum         = state.card.toDoubleOrNull()       ?: 0.0,
    bonusSum        = state.bonus.toDoubleOrNull()      ?: 0.0,
    creditSum       = state.credit.toDoubleOrNull()     ?: 0.0,
    prepaymentSum   = state.prepayment.toDoubleOrNull() ?: 0.0,
    incomingSum     = state.cash.toDoubleOrNull()       ?: 0.0,
    changeSum       = state.change,
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
