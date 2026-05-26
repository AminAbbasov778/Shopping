package com.example.myshopapp.data.remote.model.repsonse.login


import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("access_token")
    val accessToken: String
)