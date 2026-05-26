package com.example.myshopapp.data.remote.model.request.rollback


import com.google.gson.annotations.SerializedName

data class RollbackRequest(
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
    @SerializedName("parentDocument")
    val parentDocument: String,
    @SerializedName("prepaymentSum")
    val prepaymentSum: Double,
    @SerializedName("rrn")
    val rrn: String?,
    @SerializedName("sum")
    val sum: Double,
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("vatAmounts")
    val vatAmounts: List<VatAmount>

)