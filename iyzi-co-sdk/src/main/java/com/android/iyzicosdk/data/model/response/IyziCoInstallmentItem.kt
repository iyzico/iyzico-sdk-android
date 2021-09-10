package com.android.iyzicosdk.data.model.response

import com.google.gson.annotations.SerializedName


internal data class IyziCoInstallmentItem(
    @SerializedName("piece")
    var piece: String,
    @SerializedName("pieceAmount")
    var pieceAmount: Double,
    @SerializedName("totalAmount")
    var totalAmount: Double,
    var isChecked: Boolean
)