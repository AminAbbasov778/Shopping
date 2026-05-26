package com.example.myshopapp.data.remote.model.repsonse.shift.close


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("createdAtUtc")
    val createdAtUtc: String,
    @SerializedName("currencies")
    val currencies: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.Currency>,
    @SerializedName("docCountToSend")
    val docCountToSend: Int,
    @SerializedName("document_id")
    val documentId: String,
    @SerializedName("firstDocNumber")
    val firstDocNumber: Int,
    @SerializedName("lastDocNumber")
    val lastDocNumber: Int,
    @SerializedName("reportNumber")
    val reportNumber: Int,
    @SerializedName("shiftCloseAtUtc")
    val shiftCloseAtUtc: String,
    @SerializedName("shiftOpenAtUtc")
    val shiftOpenAtUtc: String
)