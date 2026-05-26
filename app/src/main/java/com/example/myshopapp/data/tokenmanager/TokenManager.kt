package com.example.shopapp.data.remote.manager

import android.util.Log
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object TokenManager {

    private const val key = "key_for_test"

    private var cachedToken: String? = null
    private var lastTime: Long = 0

    fun getToken(): Triple<String, String, String> {

        val currentTime = System.currentTimeMillis()


        if (cachedToken != null && (currentTime - lastTime) < 50_000) {

            return Triple(cachedToken!!, lastDt, lastNonce)
        }

        val dt = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            .format(Date())


        val nonce = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .take(10)

        val token = generateToken(dt, nonce, key)

        cachedToken = token
        lastTime = currentTime

        lastDt = dt
        lastNonce = nonce

        return Triple(token, dt, nonce)
    }

    private var lastDt = ""
    private var lastNonce = ""

    private fun generateToken(dt: String, nonce: String, key: String): String {
        val first = sha256(dt)
        return sha256("$first:$nonce:$key")
    }

    private fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}