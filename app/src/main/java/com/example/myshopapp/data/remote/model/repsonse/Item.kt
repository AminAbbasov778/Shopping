package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemCode")
    val itemCode: String,
    @SerializedName("itemCodeType")
    val itemCodeType: Int,
    @SerializedName("itemCreditpaySum")
    val itemCreditpaySum: Double,
    @SerializedName("itemDiscountPrice")
    val itemDiscountPrice: Double,
    @SerializedName("itemMarginPrice")
    val itemMarginPrice: Double,
    @SerializedName("itemMarginSum")
    val itemMarginSum: Double,
    @SerializedName("itemMarkingCode")
    val itemMarkingCode: String,
    @SerializedName("itemMarkingCodes")
    val itemMarkingCodes: List<String>,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("itemPrice")
    val itemPrice: Double,
    @SerializedName("itemQuantity")
    val itemQuantity: Int,
    @SerializedName("itemQuantityType")
    val itemQuantityType: Int,
    @SerializedName("itemSum")
    val itemSum: Double,
    @SerializedName("itemVatPercent")
    val itemVatPercent: Double
)