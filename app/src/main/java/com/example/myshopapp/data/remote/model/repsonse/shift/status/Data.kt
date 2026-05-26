package com.example.myshopapp.data.remote.model.repsonse.shift.status


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("shift_open")
    val shiftOpen: Boolean,
    @SerializedName("shift_open_time")
    val shiftOpenTime: String
)