package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoRetrieveMemberCardsResponse(
    @SerializedName("items")
    val iyziCoCardItems: List<IyziCoCardItem>
)