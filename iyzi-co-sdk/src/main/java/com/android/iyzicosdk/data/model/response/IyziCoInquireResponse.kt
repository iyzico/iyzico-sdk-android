package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

data class IyziCoInquireResponse(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("cardBank")
    val cardBank: String,
    @SerializedName("cardFamily")
    val cardFamily: String,
    @SerializedName("conversationId")
    val conversationId: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("points")
    val points: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("systemTime")
    val systemTime: Long
)