package com.android.iyzicosdk.core

import android.app.Activity
import androidx.annotation.Keep
import com.android.iyzicosdk.callback.IyzicoCallback
import com.android.iyzicosdk.data.model.request.IyzicoBasketItem
import com.android.iyzicosdk.util.enums.Currency
import com.android.iyzicosdk.util.enums.Languages
import com.android.iyzicosdk.util.enums.PaymentGroup


abstract class Iyzico {

    internal var callback: IyzicoCallback? = null

    abstract fun initialize(
        clientIp: String,
        clientId: String,
        clientSecretKey: String,
        baseUrl: String,
        language: Languages,
        merchant_secret_key: String? = "",
        merchant_api_key: String? = ""

    )

    abstract fun startPayWithIyzico(
        activity: Activity,
        brand: String,
        price: Double,
        paidPrice: Double,
        currency: Currency,
        enabledInstallments: Array<Int>,
        basketId: String,
        paymentGroup: PaymentGroup?,
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
        billingContactName: String,
        billingCity: String,
        billingCountry: String,
        billingAddress: String,
        shippingContactName: String,
        shippingCity: String,
        shippingCountry: String,
        shippingAddress: String,
        basketItemList: List<IyzicoBasketItem>,
        callback: IyzicoCallback
    )


    abstract fun startCashOut(
        activity: Activity,
        email: String,//
        phone: String,
        walletPrice: Double,
        name: String? = "",
        surname: String? = "",
        callback: IyzicoCallback
    )

   /* abstract fun startRefund(
        activity: Activity,
        email: String,
        phone: String,
        productId: String,
        name: String? = "",
        surname: String? = "",
        callback: IyzicoCallback
    )*/

   /* abstract fun startSettlement(
        activity: Activity,
        email: String,
        phone: String,
        walletPrice: Double,
        name: String? = "",
        surname: String? = "",
        callback: IyzicoCallback
    )*/

    abstract fun startTopUp(
        activity: Activity,
        email: String,
        phone: String,
        brand: String,
        name: String? = "",
        surname: String? = "",
        callback: IyzicoCallback
    )

    companion object {
        private lateinit var iyzico: Iyzico

        @JvmStatic
        @Synchronized
        fun client(): Iyzico {
            if (!this::iyzico.isInitialized) {
                iyzico = MainIyzico()
            }
            return iyzico
        }
    }
}

