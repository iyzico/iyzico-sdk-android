package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

data class RewardRequest(
    @SerializedName("rewardAmount")
    val rewardAmount: Double,
    @SerializedName("rewardUsage")
    val rewardUsage: Int
)