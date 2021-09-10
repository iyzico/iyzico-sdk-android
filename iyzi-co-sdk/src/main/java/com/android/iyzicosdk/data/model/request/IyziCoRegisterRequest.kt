package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoRegisterRequest(
    @SerializedName("name")
    var name: String?,
    @SerializedName("surname")
    var surname: String?,
    @SerializedName("email")
    var email: String,
    @SerializedName("phoneNumber")
    var phoneNumber: String,
    @SerializedName("registerChannel")
    var registerChannel: String,
    @SerializedName("outlineAgreementStatus")
    var outlineAgreementStatus: String?="",
    @SerializedName("pdppPermission")
    var pdppPermission: String?="",
    @SerializedName("communicationsPermission")
    var communicationsPermission: String?=""
    )
