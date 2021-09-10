package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class BankTransferPaymenNotifyRequest(
    @SerializedName("bankTransferPaymentId")
    val bankTransferPaymentId: Int
)