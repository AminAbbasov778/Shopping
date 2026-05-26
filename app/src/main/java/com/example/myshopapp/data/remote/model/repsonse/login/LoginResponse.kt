package com.example.myshopapp.data.remote.model.repsonse.login


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: com.example.myshopapp.data.remote.model.repsonse.login.LoginData,
    @SerializedName("message")
    val message: String
)