package com.example.shopapp.data.remote.model.request.sale


import com.example.myshopapp.data.remote.model.request.sale.Item
import com.example.myshopapp.data.remote.model.request.sale.VatAmount
import com.google.gson.annotations.SerializedName

data class SaleRequest(
    @SerializedName("bonusSum")
    val bonusSum: Double,
    @SerializedName("cashSum")
    val cashSum: Double,
    @SerializedName("cashier")
    val cashier: String,
    @SerializedName("cashlessSum")
    val cashlessSum: Double,
    @SerializedName("changeSum")
    val changeSum: Double,
    @SerializedName("creditSum")
    val creditSum: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("incomingSum")
    val incomingSum: Double,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("prepaymentSum")
    val prepaymentSum: Double,

    @SerializedName("sum")
    val sum: Double,
    @SerializedName("useTerminalBank")
    val useTerminalBank: Boolean,
    @SerializedName("vatAmounts")
    val vatAmounts: List<VatAmount>
)