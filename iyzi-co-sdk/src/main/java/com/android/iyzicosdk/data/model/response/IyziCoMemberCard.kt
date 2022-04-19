package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoMemberCard(
    @SerializedName("binNumber")
    val binNumber: String,
    @SerializedName("cardAssociation")
    val cardAssociation: String,
    @SerializedName("cardAssociationLogoUrl")
    val cardAssociationLogoUrl: String,
    @SerializedName("cardBankName")
    val cardBankName: String,
    @SerializedName("cardToken")
    val cardToken: String,
    @SerializedName("cardType")
    val cardType: String,
    @SerializedName("lastFourDigits")
    val lastFourDigits: String,
    @SerializedName("iyzicoVirtualCard")
    val iyzicoVirtualCard: Boolean,
    @SerializedName("iyzicoCard")
    val iyzicoCard: Boolean,
    @SerializedName("threeDSVerified")
    val threeDSVerified: Boolean
)