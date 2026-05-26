package com.example.myshopapp.data.remote.model.repsonse.print


import com.google.gson.annotations.SerializedName

data class PrintResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)