package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoBankItem(
    @SerializedName("bankName", alternate = ["bank"])
    val bankName: String,
    @SerializedName("iban")
    val iban: String,
    @SerializedName("legalCompanyTitle")
    val legalCompanyTitle: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("bankId")
    val bankId: String,
    @SerializedName("bankLogoUrl")
    var bankLogoUrl: String=""
)