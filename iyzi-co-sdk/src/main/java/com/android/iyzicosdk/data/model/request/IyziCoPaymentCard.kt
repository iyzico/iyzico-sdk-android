package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoPaymentCard(
    //bu kayıtlı kart için kullanılacak
    @SerializedName("cardToken")
    var cardToken: String? = null,
    @SerializedName("cardHolderName")
    var cardHolderName: String? = null,
    @SerializedName("cardNumber")
    var cardNumber: String? = null,
    @SerializedName("cvc")
    var cvc: String? = null,
    @SerializedName("expireMonth")
    var expireMonth: String? = null,
    @SerializedName("expireYear")
    var expireYear: String? = null,
    @SerializedName("registerCard")
    var registerCard: Int? = null,
    @SerializedName("registerConsumerCard")
    var registerConsumerCard: Boolean? = null
)