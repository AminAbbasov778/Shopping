package com.example.myshopapp.data.remote.model.request.moneyback


import com.google.gson.annotations.SerializedName

data class MoneyBackRequest(
    @SerializedName("bonusSum")
    val bonusSum: Double,
    @SerializedName("cashSum")
    val cashSum: Double,
    @SerializedName("cashier")
    val cashier: String,
    @SerializedName("cashlessSum")
    val cashlessSum: Double,
    @SerializedName("creditSum")
    val creditSum: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("incomingSum")
    val incomingSum: Double,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("moneyBackType")
    val moneyBackType: Int,
    @SerializedName("parentDocument")
    val parentDocument: String,
    @SerializedName("prepaymentSum")
    val prepaymentSum: Double,

    @SerializedName("sum")
    val sum: Double,

    @SerializedName("vatAmounts")
    val vatAmounts: List<VatAmount>
)