package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoInstallmentDetail(
    @SerializedName("bankCode")
    val bankCode: Int,
    @SerializedName("bankName")
    val bankName: String,
    @SerializedName("binNumber")
    val binNumber: String,
    @SerializedName("cardAssociation")
    var cardAssociation: String?,
    @SerializedName("cardFamilyName")
    val cardFamilyName: String,
    @SerializedName("cardType")
    val cardType: String,
    @SerializedName("commercial")
    val commercial: Int,
    @SerializedName("force3ds")
    val force3ds: Int,
    @SerializedName("forceCvc")
    val forceCvc: Int,
    @SerializedName("installmentPrices")
    val iyziCoInstallmentPrices: List<IyziCoInstallmentPrice>,
    @SerializedName("price")
    val price: Double
)