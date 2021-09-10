package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoInstallmentPrice(
    @SerializedName("installmentNumber")
    val installmentNumber: Int,
    @SerializedName("installmentPrice")
    val installmentPrice: Double,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    var isChecked: Boolean = false
)