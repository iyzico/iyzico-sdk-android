package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoInitResponse(
    @SerializedName("initialRequestId")
    var initialRequestId: String,
    @SerializedName("memberExist")
    var memberExist: Boolean
)