package com.example.myshopapp.presentation.util

object Util {
    fun formatDate(time: Long): String {

        return java.text.SimpleDateFormat(
            "dd.MM.yyyy HH:mm",
            java.util.Locale.getDefault()
        ).format(java.util.Date(time))
    }
}