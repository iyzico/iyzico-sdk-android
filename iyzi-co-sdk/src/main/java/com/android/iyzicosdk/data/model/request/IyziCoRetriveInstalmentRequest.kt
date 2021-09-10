package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoRetriveInstalmentRequest(
    @SerializedName("binNumber")
    val binNumber: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("price")
    val price: String
)