package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class Metadata(
    @SerializedName("cancelUrl")
    val cancelUrl: String
)