package com.android.iyzicosdk.util

import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.extensions.createSignature
import com.android.iyzicosdk.util.extensions.encode
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import java.io.IOException
import java.util.*

internal object CryptoManager {

    fun generateWithOutBearerAuthorizationKey(request: Request): String {
        var salt = UUID.randomUUID().toString()
        var payload = ""
        when (request.method()) {
            "GET" -> {
                payload = "${
                    "/" + request.url().toString()
                        .substring(IyziCoConfig.BASE_PATH.length, request.url().toString().length)
                }"
            }
            "POST" -> {
                payload = "${
                    "/" + request.url().toString()
                        .substring(IyziCoConfig.BASE_PATH.length, request.url().toString().length)
                }" + bodyToString(
                    request.body()!!
                )
            }
        }
        var dataToEncrypted = "${salt}${payload}"
        var encryted = dataToEncrypted.createSignature()
        var authorizationString = ""
        if (IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY.isNullOrEmpty()) {
            authorizationString =
                "clientId:${IyziCoConfig.CLIENT_ID}&clientSecret:${IyziCoConfig.CLIENT_SECRET_ID}&salt:${salt}&scope:fund&signature:" + encryted;
        } else {
            authorizationString =
                "clientId:${IyziCoConfig.CLIENT_ID}&clientSecret:${IyziCoConfig.CLIENT_SECRET_ID}&salt:${salt}&signature:" + encryted + "&bearerToken:" + IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY;
        }

        if (IyziCoConfig.MERCHANT_API_KEY.isNotEmpty() && IyziCoConfig.MERCHANT_SECRET_KEY.isNotEmpty()) {
            authorizationString =
                "clientId:${IyziCoConfig.CLIENT_ID}&clientSecret:${IyziCoConfig.CLIENT_SECRET_ID}&merchantApiKey:${IyziCoConfig.MERCHANT_API_KEY}&merchantSecretKey:${IyziCoConfig.MERCHANT_SECRET_KEY}&salt:${salt}&scope:fund&signature:" + encryted;
            if (!IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY.isNullOrEmpty()) {
                authorizationString += "&bearerToken:" + IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY;
            }
        }
        return "IYZ-TP-v1 ${authorizationString.encode()}"
    }


    private fun bodyToString(request: RequestBody): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}