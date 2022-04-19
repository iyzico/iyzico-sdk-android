package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

data class IyziCoPlusInstallmentResponse(
    @SerializedName("campaignId")
    val campaignId: Int,
    @SerializedName("campaignType")
    val campaignType: String,
    @SerializedName("cardBankDtoList")
    val cardBankDtoList: List<CardBankDto>,
    @SerializedName("endDate")
    val endDate: Long,
    @SerializedName("plusInstallment")
    val plusInstallment: String,
    @SerializedName("startDate")
    val startDate: Long
)