package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoPWIRetriveRequest(
    @SerializedName("checkoutToken")
    val checkoutToken: String,
    @SerializedName("locale")
    val locale: String
)