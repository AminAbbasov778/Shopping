package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.request.moneyback.Item
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.moneyback.VatAmount as MoneyBackVatAmount
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.rollback.VatAmount as RollbackVatAmount
import com.example.myshopapp.presentation.util.RefundTotals

fun SaleFull.toRollbackRequest(): RollbackRequest =
    RollbackRequest(
        cashier        = sale.cashier,
        currency       = sale.currency,
        sum            = sale.total,
        cashSum        = sale.cashSum,
        cashlessSum    = sale.cardSum,
        bonusSum       = sale.bonusSum,
        creditSum      = sale.creditSum,
        prepaymentSum  = sale.prepaymentSum,
        incomingSum    = sale.incomingSum,
        parentDocument = sale.fullDocumentId,
        rrn            = sale.rrn,
        uuid           = sale.uuid,
        vatAmounts     = emptyList() // Əgər rollback API-si vat siyahısı istəyirsə bura ötürülə bilər
    )

fun SaleFull.toMoneyBackRequest(refundTotals: RefundTotals): MoneyBackRequest =
    MoneyBackRequest(
        cashier        = sale.cashier,
        currency       = sale.currency,
        sum            = refundTotals.totalRefundSum,
        cashSum        = if (sale.cardSum > 0) 0.0 else refundTotals.totalRefundSum, // Ödəniş metoduna uyğun pul qaytarma
        cashlessSum    = if (sale.cardSum > 0) refundTotals.totalRefundSum else 0.0,
        bonusSum       = 0.0,
        creditSum      = 0.0,
        prepaymentSum  = 0.0,
        incomingSum    = refundTotals.totalRefundSum,
        moneyBackType  = 0,
        parentDocument = sale.fullDocumentId,
        items = refundTotals.refundItems.map { refundItem ->
            Item(
                itemCode         = refundItem.entity.itemCode,
                itemName         = refundItem.entity.itemName,
                itemCodeType     = 0,
                itemQuantity     = refundItem.refundQty,
                itemQuantityType = 0,
                itemPrice        = refundItem.effectivePrice,
                itemSum          = refundItem.refundSum,
                itemVatPercent   = refundItem.entity.vatPercent,
            )
        },
        vatAmounts = refundTotals.vatMap.map { (vatPercent, vatSum) ->
            MoneyBackVatAmount(vatPercent = vatPercent, vatSum = vatSum)
        },
    )