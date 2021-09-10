package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoBankTransferInitiaalize(
    @SerializedName("bankId")
    val bankId: Int,
    @SerializedName("buyerProtected")
    val buyerProtected: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("gsmNumber")
    val gsmNumber: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("paymentChannel")
    val paymentChannel: String
)