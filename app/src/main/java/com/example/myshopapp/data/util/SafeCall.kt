package com.example.myshopapp.data.util

import android.util.Log
import retrofit2.Response

suspend fun <T> safeApiCall(
    block: suspend () -> Response<T>,
): Result<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Log.d("TAG", "safeApiCall: $body")


                Result.success(body)
            } else {
                Log.d("TAG", "safeApiCall: Response body boşdur")

                Result.failure(Exception("Response body boşdur"))
            }
        } else {
            Log.d("TAG", "safeApiCall: Xəta kodu: ${response.code()} - ${response.message()}")
            Result.failure(Exception("Xəta kodu: ${response.code()} - ${response.message()}"))
        }
    } catch (e: Exception) {
        Log.d("TAG", "safeApiCall: $e")
        Result.failure(e)
    }
}

suspend fun <T> safeDbCall(
    block: suspend () -> T,
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}