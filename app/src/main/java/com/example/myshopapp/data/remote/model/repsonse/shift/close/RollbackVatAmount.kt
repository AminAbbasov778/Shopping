package com.example.myshopapp.data.remote.model.repsonse.shift.close


import com.google.gson.annotations.SerializedName

data class RollbackVatAmount(
    @SerializedName("vatPercent")
    val vatPercent: Int,
    @SerializedName("vatSum")
    val vatSum: Int
)