package com.android.iyzicosdk.core

import android.app.Activity
import android.graphics.Bitmap
import com.android.iyzicosdk.callback.IyziCoCallback
import com.android.iyzicosdk.data.model.request.IyziCoBasketItem
import com.android.iyzicosdk.util.enums.Currency
import com.android.iyzicosdk.util.enums.Languages
import com.android.iyzicosdk.util.enums.PaymentGroup

abstract class IyziCo {

    internal var callback: IyziCoCallback? = null

    abstract fun initialize(
        clientIp: String,
        clientId: String,
        clientSecretKey: String,
        baseUrl: String,
        language: Languages,
        merchant_secret_key: String? = "",
        merchant_api_key: String? = ""

    )

    abstract fun startPayWithIyziCo(
        activity: Activity,
        brand: String,
        price: Double,
        paidPrice: Double,
        currency: Currency,
        enabledInstallments: Array<Int>,
        basketId: String,
        paymentGroup: PaymentGroup,
        urlCallback: String,
        buyerId: String,
        buyerName: String,
        buyerSurname: String,
        buyerIdentityNumber: String,
        buyerCity: String,
        buyerCountry: String,
        buyerEmail: String,
        buyerPhone: String,
        buyerIp: String,
        buyerRegistrationAddress: String,
        buyerZipCode: String,
        buyerRegistrationDate: String,
        buyerLastLoginDate: String,
        billingContactName: String,
        billingCity: String,
        billingCountry: String,
        billingAddress: String,
        shippingContactName: String,
        shippingCity: String,
        shippingCountry: String,
        shippingAddress: String,
        itemType: String,
        itemName: String,
        itemCategory: String,
        productImage: Bitmap?,
        productId: String?,
        addressDescription: String?,
        basketItemList: List<IyziCoBasketItem>,
        callback:IyziCoCallback
    )


    abstract fun startCashOut(
        activity: Activity,
        email: String,//
        phone: String,
        walletPrice: Double,
        name: String? = "",
        surname: String? = "",
        callback: IyziCoCallback
    )

    abstract fun startRefund(
        activity: Activity,
        email: String,
        phone: String,
        productId: String,
        name: String? = "",
        surname: String? = "",
        callback: IyziCoCallback
    )

    abstract fun startSettlement(
        activity: Activity,
        email: String,
        phone: String,
        walletPrice: Double,
        name: String? = "",
        surname: String? = "",
        callback: IyziCoCallback
    )

    abstract fun startTopUp(
        activity: Activity,
        email: String,
        phone: String,
        brand: String,
        name: String? = "",
        surname: String? = "",
        callback: IyziCoCallback
    )

    //TODO: Business sorulduğunda güncellenecek

    companion object {
        private lateinit var iyzico: IyziCo

        @JvmStatic
        @Synchronized
        fun client(): IyziCo {
            if (!this::iyzico.isInitialized) {
                iyzico = MainIyziCo()
            }
            return iyzico
        }
    }
}

