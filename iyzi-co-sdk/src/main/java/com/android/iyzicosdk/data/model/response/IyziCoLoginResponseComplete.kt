package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginResponseComplete(
    @SerializedName("accessToken")
    val accessToken: String
)