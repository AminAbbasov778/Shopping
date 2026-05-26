package com.example.shopapp.data.remote.model.response.sale


import com.google.gson.annotations.SerializedName

data class SaleResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("rrn")
    val rrn: String,
    @SerializedName("uuid")
    val uuid: String
)