package com.android.iyzicosdk.util.constants

import android.graphics.Bitmap
import com.android.iyzicosdk.data.model.request.IyziCoBasketItem
import java.util.*

internal object IyziCoResourcesConstans {


    /**
     * İndirim, vergi, taksit komisyonları gibi değerlerin dahil edildiği tutar.
     */
    var IYZICO_PAID_PRICE: Double = 0.00

    /**
     * Para birimi. Default değeri TL’dir. Kullanılabilen diğer değerler ise USD, EUR, GBP ve IRR’dir
     */
    var IYZICO_CURRECY: String = ""

    /**
     * Taksit bilgisi, tek çekim için 1 gönderilmelidir. Geçerli değerler: 1, 2, 3, 6, 9.
     */
    lateinit var IYZICO_ENABLED_INSTALLMENTS: Array<Int>

    lateinit var IYZICO_BASKET_ITEM_LIST: List<IyziCoBasketItem>

    /**
     * üye işyeri sepet id’si.
     */
    var IYZICO_BASKET_ID: String = ""

    /**
     *Ödeme grubu, varsayılan PRODUCT. Geçerli değerler enum içinde sunulmaktadır: PRODUCT, LISTING, SUBSCRIPTION
     */
    var IYZICO_PAYMENT_GROUP: String? = null

    /**
     * MOBILE_SDK
     */
    var IYZICO_PAYMENT_SOURCE: String = "MOBILE_SDK"

    /**
     * Ödeme akışında üye işyerine başarılı ve hatalı sonucu bildirmek üzere alınan URL adresi. Geçerli bir ssl sertifikasına sahip olmalıdır.
     */
    var IYZICO_CALLBACK_URL: String = ""

    /**
     * Üye işyeri tarafındaki alıcıya ait id.
     */
    var IYZICO_ID: String = ""


    /**
     *Üye işyeri tarafındaki alıcıya ait kimlik (TCKN) numarası.
     */
    var IYZICO_BUYER_IDENTITY_NUMBER: String = ""

    /**
     *alıcı şehri
     */
    var IYZICO_BUYER_CITY: String = ""

    /**
     * alıcının ülkesi
     */
    var IYZICO_BUYER_COUNTRY: String = ""

    /**
     * alıcı ip adresi
     */
    var IYZICO_BUYER_IP: String = ""

    /**
     *Üye işyeri tarafındaki alıcıya ait kayıt adresi.
     */
    var IYZICO_REGISTRATION_ADRESS: String = ""

    /**
     * Üye işyeri tarafındaki alıcıya ait posta kodu.
     */
    var IYZICO_BUYER_ZIP_CODE: String = ""

    /**
     * Üye işyeri tarafındaki alıcıya ait kayıt tarihi
     */
    var IYZICO_BUYER_REGISTRATION_DATE: String = ""

    /**
     * Üye işyeri tarafındaki alıcıya ait son giriş tarihi.
     */
    var IYZICI_BUYER_LAST_LOGIN_DATE: String = ""

    /**
     * Üye işyeri tarafındaki fatura adresi, ad, soyad bilgisi.
     */
    var IYZICO_BILLING_CONTACT_NAME: String = ""

    /**
     * Üye işyeri tarafındaki fatura adresi şehir bilgisi
     */
    var IYZICO_BILLING_CITY: String = ""

    /**
     * Üye işyeri tarafındaki fatura adresi ülke bilgisi
     */
    var IYZICO_BILLING_COUNTRY: String = ""

    /**
     * Üye işyeri tarafındaki fatura adresi.
     */
    var IYZICO_BILLING_ADRESS: String = ""

    /**
     *Üye işyeri tarafındaki teslimat adresi ad soyad bilgisi
     */
    var IYZICO_SHIPPING_CONTACT_NAME: String = ""

    /**
     * Üye işyeri tarafındaki teslimat adresi şehir bilgisi
     */
    var IYZICO_SHIPPING_CITY: String = ""

    /**
     * Üye işyeri tarafındaki teslimat adresi ülke bilgisi
     */
    var IYZICO_SHIPPING_COUNTRY: String = ""

    /**
     * Üye işyeri tarafındaki teslimat adresi
     */
    var IYZICO_SHIPPING_ADDRESS: String = ""

    /**
     * Ürün id si
     */
    var IYZICO_PRODUCT_ID: String = ""

    /**
     * Ürün değeri
     */
    var IyziCoProductPrice: String = "0.00"

    /**
     * Cüzdan miktarı
     */
    var IyziCoWalletPrice: String = "20.00"

    /**
     * E-mail bilgisi
     */
    var IyziCoEmail: String = ""

    /**
     * Marka bilgisi
     */
    var IyziCoBrand: String = ""

    /**
     * Telefon bilgisi
     */
    var IyziPhoneNumber: String=""

    /**
     * ad  bilgisi
     */
    var IyziCoUserName: String? = null

    /**
     * soyad  bilgisi
     */
    var IyziCoUserSurName: String? = null

    /**
     * ödeme için gerekli X_IyziToken
     */
    var IyziCoXToken:String=""

    /**
     * token kullanılıp kullanılmayacağını ayarlıyor.
     */
    var IyziCOxTokenUse=false

}