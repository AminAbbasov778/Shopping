package com.example.myshopapp.presentation.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundTo2(): Double =
    BigDecimal(this.toString()).setScale(2, RoundingMode.DOWN).toDouble()

fun Double.roundTo3(): Double =
    BigDecimal(this.toString()).setScale(3, RoundingMode.DOWN).toDouble()