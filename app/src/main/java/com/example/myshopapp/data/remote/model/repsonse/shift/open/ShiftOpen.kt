package com.example.myshopapp.data.remote.model.repsonse.shift.open


import com.google.gson.annotations.SerializedName

data class ShiftOpen(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)