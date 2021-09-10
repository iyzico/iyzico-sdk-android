package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class MobileDeviceInfoDto(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("operatingSystemVersion")
    val operatingSystemVersion: String,
    @SerializedName("sdkVersion")
    val sdkVersion: String
)