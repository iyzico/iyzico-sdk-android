package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoCardItem(
    @SerializedName("cardBankName")
    val cardBankName: String,
    @SerializedName("cardToken")
    val cardToken: String,
    @SerializedName("cardType")
    val cardType: String,
    @SerializedName("lastFourDigits")
    val lastFourDigits: String,
    @SerializedName("cardAssociation")
    var cardAssociation: String = "",
    @SerializedName("cardAssociationLogoUrl")
    var cardAssociationLogoUrl: String = "",
    @SerializedName("binNumber")
    var binNumber: String = "",
    var isSelected: Boolean = false
)