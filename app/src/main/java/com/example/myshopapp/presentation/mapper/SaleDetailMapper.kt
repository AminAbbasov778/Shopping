package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.request.moneyback.Item
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.moneyback.VatAmount as MoneyBackVatAmount
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.rollback.VatAmount as RollbackVatAmount

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
        vatAmounts     = vat.map {
            RollbackVatAmount(vatPercent = it.vatPercent, vatSum = it.vatSum)
        },
    )

fun SaleFull.toMoneyBackRequest(): MoneyBackRequest =
    MoneyBackRequest(
        cashier        = sale.cashier,
        currency       = sale.currency,
        sum            = sale.total,
        cashSum        = sale.cashSum,
        cashlessSum    = sale.cardSum,
        bonusSum       = sale.bonusSum,
        creditSum      = sale.creditSum,
        prepaymentSum  = sale.prepaymentSum,
        incomingSum    = sale.incomingSum,
        moneyBackType  = 0,
        parentDocument = sale.fullDocumentId,
        items = items.map {
            Item(
                itemCode         = it.itemCode,
                itemName         = it.itemName,
                itemCodeType     = 0,
                itemQuantity     = it.quantity,
                itemQuantityType = 0,
                itemPrice        = it.price,
                itemSum          = it.sum,
                itemVatPercent   = it.vatPercent,
            )
        },
        vatAmounts = vat.map {
            MoneyBackVatAmount(vatPercent = it.vatPercent, vatSum = it.vatSum)
        },
    )