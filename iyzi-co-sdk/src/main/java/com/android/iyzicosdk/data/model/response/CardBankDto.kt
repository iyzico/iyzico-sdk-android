package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

data class CardBankDto(
    @SerializedName("cardBankId")
    val cardBankId: Int,
    @SerializedName("cardBankName")
    val cardBankName: String
)