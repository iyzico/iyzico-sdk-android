package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoPWIİnitializeRequest(
    @SerializedName("basketId")
    val basketId: String,
    @SerializedName("basketItems")
    val basketItems: List<IyzicoBasketItem>,
    @SerializedName("billingAddress")
    val billingAddress: IyziCoBillingAddress,
    @SerializedName("buyer")
    val buyer:IyziCoBuyer,
    @SerializedName("callbackUrl")
    val callbackUrl: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("enabledInstallments")
    val enabledInstallments: List<Int>,
    @SerializedName("mobileDeviceInfoDto")
    val mobileDeviceInfoDto: MobileDeviceInfoDto,
    @SerializedName("paidPrice")
    val paidPrice: String,
    @SerializedName("paymentGroup")
    val paymentGroup: String?,
    @SerializedName("paymentSource")
    val paymentSource: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("shippingAddress")
    val shippingAddress: IyziCoShippingAddress
)