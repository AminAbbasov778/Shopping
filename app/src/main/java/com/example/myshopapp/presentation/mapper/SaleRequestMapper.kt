package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.remote.model.request.sale.VatAmount
import com.example.myshopapp.presentation.state.CartUiState
import com.example.myshopapp.presentation.state.PaymentUiState
import com.example.myshopapp.util.Constants.CURRENCY
import com.example.shopapp.data.remote.model.request.sale.SaleRequest

fun buildSaleRequest(cart: CartUiState, payment: PaymentUiState): SaleRequest {

    val cash       = payment.cash.toDoubleOrNull()       ?: 0.0
    val card       = payment.card.toDoubleOrNull()       ?: 0.0
    val bonus      = payment.bonus.toDoubleOrNull()      ?: 0.0
    val credit     = payment.credit.toDoubleOrNull()     ?: 0.0
    val prepayment = payment.prepayment.toDoubleOrNull() ?: 0.0

    val totalSum    = cart.total
    val nonCashSum  = card + bonus + credit + prepayment
    val cashSum     = if (cash > 0) maxOf(0.0, totalSum - nonCashSum) else 0.0
    val incomingSum = if (cash > 0) cash else 0.0
    val changeSum   = if (cash > 0) maxOf(0.0, incomingSum - cashSum) else 0.0

    val cartSize   = cart.items.size
    val items      = cart.items.map { it.toRequestItem(credit, cartSize) }
    val vatAmounts = buildVatAmountsFromSummary(cart.vatSummary)

    return SaleRequest(
        cashier         = "",
        currency        = CURRENCY,
        sum             = totalSum,
        cashSum         = cashSum,
        cashlessSum     = card,
        bonusSum        = bonus,
        creditSum       = credit,
        prepaymentSum   = prepayment,
        incomingSum     = incomingSum,
        changeSum       = changeSum,
        useTerminalBank = false,
        items           = items,
        vatAmounts      = vatAmounts,
    )
}

fun buildVatAmountsFromSummary(vatSummary: Map<Double?, Double>): List<VatAmount> =
    vatSummary.map { (percent, amount) ->
        VatAmount(vatSum = amount, vatPercent = percent)
    }