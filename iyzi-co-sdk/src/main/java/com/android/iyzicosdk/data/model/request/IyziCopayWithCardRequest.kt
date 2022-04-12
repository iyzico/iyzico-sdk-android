package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCopayWithCardRequest(
    @SerializedName("buyerProtected")
    val buyerProtected: Boolean,
    @SerializedName("gsmNumber")
    val gsmNumber: String,
    @SerializedName("installment")
    val installment: Int,
    @SerializedName("memberId")
    val memberId: String,
    @SerializedName("memberToken")
    val memberToken: String,
    @SerializedName("paidPrice")
    val paidPrice: String,
    @SerializedName("paymentCard")
    val paymentCard: IyziCoPaymentCard,
    @SerializedName("paymentChannel")
    val paymentChannel: String,
    @SerializedName("reward")
    val rewardRequest: RewardRequest?
)