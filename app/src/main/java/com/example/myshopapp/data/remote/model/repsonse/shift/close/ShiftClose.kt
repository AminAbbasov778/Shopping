package com.example.myshopapp.data.remote.model.repsonse.shift.close


import com.google.gson.annotations.SerializedName

data class ShiftClose(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: com.example.myshopapp.data.remote.model.repsonse.shift.close.Data,
    @SerializedName("message")
    val message: String
)