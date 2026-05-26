package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class GasStationInfo(
    @SerializedName("fdp")
    val fdp: String,
    @SerializedName("gun")
    val gun: String,
    @SerializedName("pump")
    val pump: String,
    @SerializedName("reservoir")
    val reservoir: String
)