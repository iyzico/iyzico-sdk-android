package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoInitializeTransactionRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("transactionType")
    val transactionType: String
)