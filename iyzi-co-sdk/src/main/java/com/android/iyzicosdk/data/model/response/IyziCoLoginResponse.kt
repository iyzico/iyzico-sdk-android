package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginResponse(
    @SerializedName("expireDurationInSeconds")
    var expireDurationInSeconds: Int,
    @SerializedName("userUuid")
    var memberUserId: String,
    @SerializedName("referenceCode")
    var referenceCode: String,
    @SerializedName("gsmVerified")
    var gsmVerified: Boolean,
    @SerializedName("maskedGsmNumber")
    var maskedGsmNumber: String,
    @SerializedName("gsmNumber")
    var gsmNumber: String?

)