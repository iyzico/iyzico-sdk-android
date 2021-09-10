package com.android.iyzicosdk.data.repository.base

import com.google.gson.annotations.SerializedName

internal class IyziCoBaseResponse<T> {

    var status: String = ""

    @SerializedName("data", alternate = ["thirdPartyRegisterDto"])
    var data: T? = null
        private set

    var systemTime: Long? = null
        private set

}