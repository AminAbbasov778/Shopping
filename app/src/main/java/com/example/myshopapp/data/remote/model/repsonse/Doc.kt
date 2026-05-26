package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("bonusCardNumber")
    val bonusCardNumber: String,
    @SerializedName("bonusSum")
    val bonusSum: Double,
    @SerializedName("carNumber")
    val carNumber: String,
    @SerializedName("cashSum")
    val cashSum: Double,
    @SerializedName("cashier")
    val cashier: String,
    @SerializedName("cashlessSum")
    val cashlessSum: Double,
    @SerializedName("changeSum")
    val changeSum: Double,
    @SerializedName("committentInfo")
    val committentInfo: CommittentInfo,
    @SerializedName("createdAtUtc")
    val createdAtUtc: String,
    @SerializedName("creditContract")
    val creditContract: String,
    @SerializedName("creditPayer")
    val creditPayer: String,
    @SerializedName("creditSum")
    val creditSum: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("docNumber")
    val docNumber: Int,
    @SerializedName("gasStationInfo")
    val gasStationInfo: GasStationInfo,
    @SerializedName("guestsCount")
    val guestsCount: Int,
    @SerializedName("incomingSum")
    val incomingSum: Double,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("parents")
    val parents: List<String>,
    @SerializedName("positionInShift")
    val positionInShift: Int,
    @SerializedName("prepaymentSum")
    val prepaymentSum: Double,
    @SerializedName("rrn")
    val rrn: String,
    @SerializedName("sum")
    val sum: Int,
    @SerializedName("vatAmounts")
    val vatAmounts: List<VatAmount>
)