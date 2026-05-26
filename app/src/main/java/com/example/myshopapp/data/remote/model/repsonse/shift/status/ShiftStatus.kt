package com.example.myshopapp.data.remote.model.repsonse.shift.status


import com.google.gson.annotations.SerializedName

data class ShiftStatus(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: com.example.myshopapp.data.remote.model.repsonse.shift.status.Data,
    @SerializedName("message")
    val message: String
)