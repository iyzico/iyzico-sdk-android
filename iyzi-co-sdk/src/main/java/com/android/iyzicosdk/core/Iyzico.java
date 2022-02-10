package com.android.iyzicosdk.core;

import android.app.Activity;

import androidx.annotation.Keep;

import com.android.iyzicosdk.callback.IyzicoCallback;
import com.android.iyzicosdk.data.model.request.IyzicoBasketItem;
import com.android.iyzicosdk.util.enums.Currency;
import com.android.iyzicosdk.util.enums.Languages;
import com.android.iyzicosdk.util.enums.PaymentGroup;

import java.util.List;

import kotlin.jvm.Synchronized;

@Keep
abstract public class Iyzico {

    private static Iyzico iyzico = null;
    public static IyzicoCallback iyzicoCallback = null;


    public abstract void initialize(
            String clientIp,
            String clientId,
            String clientSecretKey,
            String baseUrl,
            Languages language,
            String merchant_secret_key,
            String merchant_api_key

    );

    public abstract void startPayWithIyzico(
            Activity activity,
            String brand,
            Double price,
            Double paidPrice,
            Currency currency,
            List<Integer> enabledInstallments,
            String basketId,
            PaymentGroup paymentGroup,
            String urlCallback,
            String buyerId,
            String buyerName,
            String buyerSurname,
            String buyerIdentityNumber,
            String buyerCity,
            String buyerCountry,
            String buyerEmail,
            String buyerPhone,
            String buyerIp,
            String buyerRegistrationAddress,
            String billingContactName,
            String billingCity,
            String billingCountry,
            String billingAddress,
            String shippingContactName,
            String shippingCity,
            String shippingCountry,
            String shippingAddress,
            List<IyzicoBasketItem> basketItemList,
            IyzicoCallback callback
    );


    public abstract void startCashOut(
            Activity activity,
            String email,//
            String phone,
            Double walletPrice,
            String name,
            String surname,
            IyzicoCallback callback
    );

    /* abstract fun startRefund(
         activity Activity,
         email String,
         phone String,
         productId String,
         name String? = ,
         surname String? = ,
         callback IyzicoCallback
     )*/

    /* abstract fun startSettlement(
         activity Activity,
         email String,
         phone String,
         walletPrice Double,
         name String? = ,
         surname String? = ,
         callback IyzicoCallback
     )*/

    public abstract void startTopUp(
            Activity activity,
            String email,
            String phone,
            String brand,
            String name,
            String surname,
            IyzicoCallback callback
    );

    @Keep
    @Synchronized
    public static Iyzico client() {
        if (iyzico == null) {
            iyzico = new MainIyzico();
        }
        return iyzico;
    }
}

