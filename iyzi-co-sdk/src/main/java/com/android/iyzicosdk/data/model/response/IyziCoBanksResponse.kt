package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoBanksResponse(
    @SerializedName("items")
    val items: List<IyziCoBankItem>,
    @SerializedName("referenceCode")
    val referenceCode: String

)