package com.android.iyzicosdk.core

import com.android.iyzicosdk.BuildConfig
import com.android.iyzicosdk.data.service.IyziCoApi
import com.android.iyzicosdk.util.CryptoManager
import com.android.iyzicosdk.util.config.HttpConfig
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


internal class IyziCoHttp {

    private var retrofit: Retrofit =
        Retrofit.Builder().baseUrl(IyziCoConfig.BASE_PATH).client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()

    private fun getCertificate(): CertificatePinner? {
        var certificatePinner: CertificatePinner? = null
        certificatePinner = CertificatePinner.Builder()
            .add(IyziCoConfig.BASE_PATH, "sha256/9pfml4d3n7mXEa4UXu0k6jTHoVVPYLwsVWJbY1kn7kM=")
            .build()
        return certificatePinner
    }
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .certificatePinner(getCertificate()!!)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    var url = request.url().newBuilder()
                        .addQueryParameter("locale", IyziCoConfig.LANGUAGE.type).build()
                    if (request.method().equals("GET")) {
                        url = url.newBuilder().addQueryParameter(
                            "channelType",
                            IyziCoLoginChannelType.THIRD_PARTY_APP.type
                        ).addQueryParameter("locale", IyziCoConfig.LANGUAGE.type).build()
                    } else {
                        url = request.url().newBuilder()
                            .addQueryParameter("locale", IyziCoConfig.LANGUAGE.type).build()
                    }
                    if (!IyziCoResourcesConstans.IyziCOxTokenUse) {
                        request = request.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader(
                                "Authorization",
                                CryptoManager.generateWithOutBearerAuthorizationKey(request)
                            )
                            .url(url)
                            .build()
                        return chain.proceed(request)
                    } else {
                        IyziCoResourcesConstans.IyziCOxTokenUse = false
                        request = request.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader(
                                "Authorization",
                                CryptoManager.generateWithOutBearerAuthorizationKey(request)
                            )
                            .addHeader("X-IYZI-TOKEN", IyziCoResourcesConstans.IyziCoXToken)
                            .url(url)
                            .build()
                        return chain.proceed(request)

                    }

                }
            }
            )

            .addInterceptor(getLoggingInterceptor())

            .build()
    }


    private fun getLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return loggingInterceptor
    }

    fun getApi(): IyziCoApi {
        return retrofit.create(IyziCoApi::class.java)
    }


}