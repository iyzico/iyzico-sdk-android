package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoBuyer(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gsmNumber")
    val gsmNumber: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("identityNumber")
    val identityNumber: String,
    @SerializedName("ip")
    val ip: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("registrationAddress")
    val registrationAddress: String,
    @SerializedName("surname")
    val surname: String
)