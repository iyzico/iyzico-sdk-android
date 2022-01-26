package com.android.iyzicosdk.data.model.request


import com.android.iyzicosdk.util.enums.BasketItemType
import com.google.gson.annotations.SerializedName

data class IyzicoBasketItem(
    @SerializedName("category1")
    val category1: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("itemType")
    val itemType: BasketItemType,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("subMerchantKey")
    val subMerchantKey: String?,
    @SerializedName("subMerchantPrice")
    val subMerchantPrice: String?
)
