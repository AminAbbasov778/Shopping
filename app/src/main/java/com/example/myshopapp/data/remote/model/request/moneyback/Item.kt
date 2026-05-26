package com.example.myshopapp.data.remote.model.request.moneyback


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemCode")
    val itemCode: String,
    @SerializedName("itemCodeType")
    val itemCodeType: Int,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("itemPrice")
    val itemPrice: Double,
    @SerializedName("itemQuantity")
    val itemQuantity: Double,
    @SerializedName("itemQuantityType")
    val itemQuantityType: Int,
    @SerializedName("itemSum")
    val itemSum: Double,
    @SerializedName("itemVatPercent")
    val itemVatPercent: Double?
)