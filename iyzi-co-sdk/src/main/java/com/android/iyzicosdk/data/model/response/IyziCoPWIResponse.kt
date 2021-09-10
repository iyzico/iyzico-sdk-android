package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal class IyziCoPWIResponse(
    @SerializedName("checkoutToken", alternate = ["token"])
    val checkoutToken: String? = null,
    @SerializedName("checkoutTokenExpireTime")
    val checkoutTokenExpireTime: Int,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("systemTime")
    val systemTime: Long? = null,
    @SerializedName("callbackUrl")
    var callbackUrl: String? = null,
    @SerializedName("locale")
    var locale: String? = null,
    @SerializedName("conversationId")
    var conversationId: String? = null,
    @SerializedName("threeDSHtmlContent")
    var threeDSHtmlContent: String? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("bankTransferPaymentId")
    var bankTransferPaymentId: String? = null,
    @SerializedName("memberExist")
    var memberExist: Boolean? = false,
    @SerializedName("errorMessage")
    val errorMessage: String?=null,
    @SerializedName("errorCode")
    val errorCode: Int?=null


)