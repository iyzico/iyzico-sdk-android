package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoPwiRetrieveInstalmentsResponse(
    @SerializedName("conversationId")
    val conversationId: String,
    @SerializedName("installmentDetails")
    val iyziCoInstallmentDetails: List<IyziCoInstallmentDetail>,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("systemTime")
    val systemTime: Long,
    @SerializedName("errorMessage")
    val errorMessage: String?=null,
    @SerializedName("errorCode")
    val errorCode: Int?=null

)