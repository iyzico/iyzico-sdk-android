package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoPWIRetriveResponse(
    @SerializedName("checkoutDetail")
    val iyziCoCheckoutDetail: IyziCoCheckoutDetail,
    @SerializedName("memberBalance")
    val iyziCoMemberBalance: IyziCoMemberBalance,
    @SerializedName("memberCards")
    val iyziCoMemberCards: List<IyziCoMemberCard>,
    @SerializedName("memberToken")
    val memberToken: String,
    @SerializedName("memberId")
    val memberId: String

)