package com.example.myshopapp.data.remote.model.repsonse.withdraw


import com.google.gson.annotations.SerializedName

data class WithDrawResponse(
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