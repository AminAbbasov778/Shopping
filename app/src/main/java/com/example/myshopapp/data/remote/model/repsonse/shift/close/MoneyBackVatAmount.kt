package com.example.myshopapp.data.remote.model.repsonse.shift.close


import com.google.gson.annotations.SerializedName

data class MoneyBackVatAmount(
    @SerializedName("vatPercent")
    val vatPercent: Double,
    @SerializedName("vatSum")
    val vatSum: Double
)