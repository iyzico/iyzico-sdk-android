package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoShippingAddress(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("contactName")
    val contactName: String,
    @SerializedName("country")
    val country: String
)