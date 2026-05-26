package com.example.shopapp.data.remote.manager

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class TokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val (token, dt, nonce) = TokenManager.getToken()

        val request = chain.request().newBuilder()
            .addHeader("dt", dt)
            .addHeader("nonce", nonce)
            .addHeader("token", token)
            .build()

        return chain.proceed(request)
    }




}



