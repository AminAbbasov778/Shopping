package com.example.myshopapp.data.remote.model.request.moneyback


import com.google.gson.annotations.SerializedName

data class VatAmount(
    @SerializedName("vatPercent")
    val vatPercent: Double?,
    @SerializedName("vatSum")
    val vatSum: Double
)