package com.example.myshopapp.presentation.mapper

import android.util.Log
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.request.moneyback.Item
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.moneyback.VatAmount
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.presentation.util.CartCalculator
import com.example.myshopapp.presentation.util.RefundTotals
import com.example.myshopapp.presentation.util.roundTo2

fun SaleFull.toRollbackRequest(): RollbackRequest {

    val vatMap = mutableMapOf<Double?, Double>()

    items.forEach { item ->
        if (item.isAgro) {
            val purchasePart = (item.purchasePrice * item.quantity).roundTo2()
            val marginPart   = (item.sum - purchasePart).roundTo2()

            vatMap[null] = ((vatMap[null] ?: 0.0) + purchasePart).roundTo2()
            vatMap[18.0] = ((vatMap[18.0] ?: 0.0) + marginPart).roundTo2()
        } else {
            val vatKey = if (item.vatPercent == 0.0) null else item.vatPercent
            vatMap[vatKey] = ((vatMap[vatKey] ?: 0.0) + item.sum).roundTo2()
        }
    }

    return RollbackRequest(
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
        vatAmounts    = vatMap.map { (vatPercent, vatSum) ->
            com.example.myshopapp.data.remote.model.request.rollback.VatAmount(vatPercent = vatPercent, vatSum = vatSum)
        },
    )
}

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
                itemCode = refundItem.entity.itemCode,
                itemName = refundItem.entity.itemName,
                itemCodeType = 0,
                itemQuantity = refundItem.refundQty,
                itemQuantityType = 0,
                itemPrice = refundItem.refundNetUnitPrice,
                itemSum = refundItem.refundSum,
                itemMarginSum = if(refundItem.entity.isAgro)  (refundItem.refundNetUnitPrice -  refundItem.entity.purchasePrice) * refundItem.refundQty else 0.0,
                itemMarginPrice = if(refundItem.entity.isAgro) refundItem.refundNetUnitPrice -  refundItem.entity.purchasePrice   else 0.0 ,
                itemVatPercent = if (refundItem.entity.vatPercent == 0.0 && refundItem.entity.isAgro) 18.0 else if (refundItem.entity.vatPercent == 0.0) null else refundItem.entity.vatPercent,
            )
        },

        vatAmounts = refundTotals.vatMap.map { (vatPercent, vatSum) ->

            if (vatPercent == 0.0) {
                VatAmount(
                    vatPercent = null,
                    vatSum = vatSum
                )
            } else {
                VatAmount(
                    vatPercent = vatPercent,
                    vatSum = vatSum
                )
            }
        },
    )
}