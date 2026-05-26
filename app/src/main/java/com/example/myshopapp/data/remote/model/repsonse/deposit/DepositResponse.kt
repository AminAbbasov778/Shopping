package com.example.myshopapp.data.remote.model.repsonse.deposit


import com.google.gson.annotations.SerializedName

data class DepositResponse(
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