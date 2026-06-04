package com.example.myshopapp.presentation.mapper

import android.util.Log
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.request.moneyback.Item
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.moneyback.VatAmount as MoneyBackVatAmount
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.rollback.VatAmount as RollbackVatAmount
import com.example.myshopapp.presentation.util.RefundTotals
import com.example.myshopapp.presentation.util.roundTo2

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

fun SaleFull.toMoneyBackRequest(refundTotals: RefundTotals): MoneyBackRequest {
    // Orijinal ödəniş metodlarına proporsional olaraq refund məbləğini böləcəyik.
    // refundRatio = qaytarılan məbləğ / orijinal cəm
    val refundRatio = if (sale.total > 0) refundTotals.totalRefundSum / sale.total else 0.0



    val refundCash        = (sale.cashSum       * refundRatio).roundTo2()
    val refundCard        = (sale.cardSum        * refundRatio).roundTo2()
    val refundBonus       = (sale.bonusSum       * refundRatio).roundTo2()
    val refundCredit      = (sale.creditSum      * refundRatio).roundTo2()
    val refundPrepayment  = (sale.prepaymentSum  * refundRatio).roundTo2()

    return MoneyBackRequest(
        cashier        = sale.cashier,
        currency       = sale.currency,
        sum            = refundTotals.totalRefundSum,
        cashSum        = refundCash,
        cashlessSum    = refundCard,
        bonusSum       = refundBonus,
        creditSum      = refundCredit,
        prepaymentSum  = refundPrepayment,
        incomingSum    = refundTotals.totalRefundSum,
        moneyBackType  = 0,
        parentDocument = sale.fullDocumentId,
        items = refundTotals.refundItems.map { refundItem ->
            Log.d("MoneyBackRequest", "refundItem: $refundItem  ,check ${if(refundItem.entity.vatPercent == 0.0) null  else refundItem.entity.vatPercent}")

            Item(
                itemCode         = refundItem.entity.itemCode,
                itemName         = refundItem.entity.itemName,
                itemCodeType     = 0,
                itemQuantity     = refundItem.refundQty,
                itemQuantityType = 0,
                itemPrice        = refundItem.effectivePrice,
                itemSum          = refundItem.refundSum,
                itemVatPercent   = if(refundItem.entity.vatPercent == 0.0 && refundItem.entity.isAgro) 18.0  else if(refundItem.entity.vatPercent == 0.0) null else refundItem.entity.vatPercent  ,
            )
        },

        vatAmounts = refundTotals.vatMap.map { (vatPercent, vatSum) ->

            if (vatPercent == 0.0) {
                MoneyBackVatAmount(
                    vatSum = vatSum
                )
            } else {
                MoneyBackVatAmount(
                    vatPercent = vatPercent,
                    vatSum = vatSum
                )
            }
        },
    )
}