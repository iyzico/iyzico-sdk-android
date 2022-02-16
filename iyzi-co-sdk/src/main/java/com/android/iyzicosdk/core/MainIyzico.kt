package com.android.iyzicosdk.core

import android.app.Activity
import com.android.iyzicosdk.callback.IyzicoCallback
import com.android.iyzicosdk.data.model.request.IyzicoBasketItem
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.constants.TextMessages
import com.android.iyzicosdk.util.enums.*
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.extensions.isInvalidCurrency
import com.android.iyzicosdk.util.extensions.isInvalidLanguage
import com.android.iyzicosdk.util.extensions.isInvalidPaymentGroup

open class MainIyzico : Iyzico() {

    override fun initialize(
        clientIp: String,
        clientId: String,
        clientSecretKey: String,
        baseUrl: String,
        language: Languages,
        merchant_secret_key: String?,
        merchant_api_key: String?

    ) {
        IyziCoConfig.apply {
            CLIENT_IP = clientIp
            CLIENT_SECRET_ID = clientSecretKey
            CLIENT_ID = clientId
            BASE_PATH = baseUrl
            LANGUAGE = language
            MERCHANT_SECRET_KEY = merchant_secret_key ?: ""
            MERCHANT_API_KEY = merchant_api_key ?: ""
        }
    }



    override fun startPayWithIyzico(
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
        billingContactName: String,
        billingCity: String,
        billingCountry: String,
        billingAddress: String,
        shippingContactName: String,
        shippingCity: String,
        shippingCountry: String,
        shippingAddress: String,
        basketItemList: MutableList<IyzicoBasketItem>,
        callback: IyzicoCallback
    ) {
        var bool: Boolean = true

        for (item in basketItemList) {
            when {
                item.id.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCT_ID,
                        TextMessages.INVALID_PRODUCT_ID
                    )
                }
                item.name.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCT_NAME,
                        TextMessages.INVALID_PRODUCT_NAME
                    )
                }
                item.category1.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCT_CATEGORY,
                        TextMessages.INVALID_PRODUCT_CATEGORY
                    )
                }
                item.price.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.BASKET_PRODUCT_PRICE_ERROR,
                        TextMessages.INVALID_PRODUCT_PRICE_ERROR
                    )
                }
                item.itemType.type.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.BASKET_PRUDUCT_ITEM_TYPE_ERROR,
                        TextMessages.INVALID_PRODUCT_TYPE
                    )
                }
                item.itemType.type == BasketItemType.PHYSICAL.type -> {
                    if (shippingContactName.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPING_CONTACT_NAME,
                            TextMessages.INVALID_SHIPPING_CONTACT_NAME
                        )
                    } else if (shippingCity.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPING_CITY,
                            TextMessages.INVALID_SHIPPING_CITY
                        )
                    } else if (shippingCountry.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPING_COUNTRY,
                            TextMessages.INVALID_SHIPPING_COUNTRY
                        )
                    } else if (shippingAddress.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPING_ADDRESS,
                            TextMessages.INVALID_SHIPPING_ADDRESS
                        )
                    }
                }
            }
        }
        if (bool) {
            when {
                IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_CLIENT_SECRET_KEY,
                        TextMessages.CLIENT_SECRET_KEY_ERROR_TEXT
                    )
                }
                IyziCoConfig.MERCHANT_SECRET_KEY.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_MERCHANT_SECRET,
                        TextMessages.INVALID_MERCHANT_SECRET_KEY
                    )
                }
                IyziCoConfig.MERCHANT_API_KEY.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_MERCHANT_API,
                        TextMessages.INVALID_MERCHANT_API_KEY
                    )
                }
                IyziCoConfig.CLIENT_ID.isEmpty() -> {
                    callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
                }
                IyziCoConfig.CLIENT_IP.isEmpty() -> {
                    callback.error(ResultCode.MISSING_CLIENT_IP, TextMessages.INVALID_CLIENT_IP)
                }
                IyziCoConfig.BASE_PATH.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BASE_URL, TextMessages.INVALID_BASE_URL)
                }
                IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                    callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
                }
                brand.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BRAND, TextMessages.INVALID_BRAND)
                }
                buyerEmail.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_EMAIL,
                        TextMessages.INVALID_EMAIL_NUMBER
                    )
                }
                buyerPhone?.length != 10 -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_PHONE,
                        TextMessages.INVALID_PHONE_NUMBER
                    )
                }
                price == null -> {
                    callback.error(ResultCode.MISSING_PRICE, TextMessages.INVALID_PRICE)
                }
                paidPrice == null -> {
                    callback.error(ResultCode.MISSING_PAID_PRICE, TextMessages.INVALID_PAID_PRICE)
                }
                currency.type.isInvalidCurrency() -> {
                    currency.type = Currency.TRY.type
                }
                enabledInstallments.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_ENABLED_INSTALLMENTS,
                        TextMessages.INVALID_ENABLED_INSTALLMENTS
                    )
                }
                basketId.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BASKET_ID, TextMessages.INVALID_BASKET_ID)
                }
                paymentGroup?.type?.isInvalidPaymentGroup() == true -> {
                    paymentGroup?.type = PaymentGroup.PRODUCT.type
                }
                urlCallback.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_URL_CALLBACK,
                        TextMessages.INVALID_URL_CALLBACK
                    )
                }
                buyerId.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYER_ID, TextMessages.INVALID_BUYER_ID)
                }
                buyerName.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYER_NAME, TextMessages.INVALID_BUYER_NAME)
                }
                buyerSurname.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_SURNAME,
                        TextMessages.INVALID_BUYER_SURNAME
                    )
                }
                buyerIdentityNumber.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_IDENTITY_NUMBER,
                        TextMessages.INVALID_BUYER_IDENTITY_NUMBER
                    )
                }
                buyerIdentityNumber.length != 11 -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_IDENTITY_NUMBER,
                        TextMessages.INVALID_BUYER_IDENTITY_NUMBER
                    )
                }
                buyerCity.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYER_CITY, TextMessages.INVALID_BUYER_CITY)
                }
                buyerCountry.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_COUNTRY,
                        TextMessages.INVALID_BUYER_COUNTRY
                    )
                }
                buyerIp.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYER_IP, TextMessages.INVALID_BUYER_IP)
                }
                buyerRegistrationAddress.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYER_REGISTRATION_ADDRESS,
                        TextMessages.INVALID_BUYER_REGISTRATION_ADDRESS
                    )
                }

                billingContactName.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLING_CONTACT_NAME,
                        TextMessages.INVALID_BILLING_CONTACT_NAME
                    )
                }
                billingCity.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLING_CITY,
                        TextMessages.INVALID_BILLING_CITY
                    )
                }
                billingCountry.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLING_COUNTRY,
                        TextMessages.INVALID_BILLING_COUNTRY
                    )
                }
                billingAddress.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLING_ADDRESS,
                        TextMessages.INVALID_BILLING_ADDRESS
                    )
                }
                basketItemList.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_EMPTY_BASKET,
                        TextMessages.INVALID_EMPTY_BASKET
                    )
                }
                basketItemList.size > 500 -> {
                    callback.error(ResultCode.MISSING_FULL_BASKET, TextMessages.INVALID_FULL_BASKET)
                }

                else -> {
                    IyziCoConfig.IYZI_CO_SDK_TYPE = IyziCoSDKType.PAY_WITH_IYZI_CO
                    IyziCoResourcesConstans.IyziCoProductPrice = price.toString()
                    IyziCoResourcesConstans.IyziCoBrand = brand
                    IyziCoResourcesConstans.IyziCoEmail = buyerEmail.toLowerCase()
                    IyziCoResourcesConstans.IyziPhoneNumber = buyerPhone
                    IyziCoResourcesConstans.IyziCoUserName = buyerName
                    IyziCoResourcesConstans.IyziCoUserSurName = buyerSurname
                    IyziCoResourcesConstans.IYZICO_PAID_PRICE = paidPrice
                    IyziCoResourcesConstans.IYZICO_CURRECY = currency.type
                    IyziCoResourcesConstans.IYZICO_ENABLED_INSTALLMENTS = enabledInstallments

                    IyziCoResourcesConstans.IYZICO_BASKET_ID = basketId
                    IyziCoResourcesConstans.IYZICO_PAYMENT_GROUP = paymentGroup?.type
                    IyziCoResourcesConstans.IYZICO_CALLBACK_URL = urlCallback
                    IyziCoResourcesConstans.IYZICO_ID = buyerId
                    IyziCoResourcesConstans.IYZICO_BUYER_IDENTITY_NUMBER =
                        buyerIdentityNumber
                    IyziCoResourcesConstans.IYZICO_BUYER_CITY = buyerCity
                    IyziCoResourcesConstans.IYZICO_BUYER_IP = buyerIp
                    IyziCoResourcesConstans.IYZICO_REGISTRATION_ADRESS =
                        buyerRegistrationAddress
                    /*  IyziCoResourcesConstans.IYZICO_BUYER_REGISTRATION_DATE =
                          buyerRegistrationDate
                      IyziCoResourcesConstans.IYZICI_BUYER_LAST_LOGIN_DATE =
                          buyerLastLoginDate*/
                    IyziCoResourcesConstans.IYZICO_BILLING_CONTACT_NAME = billingContactName
                    IyziCoResourcesConstans.IYZICO_BILLING_CITY = billingCity
                    IyziCoResourcesConstans.IYZICO_BILLING_COUNTRY = billingCountry
                    IyziCoResourcesConstans.IYZICO_BILLING_ADRESS = billingAddress
                    IyziCoResourcesConstans.IYZICO_SHIPPING_CITY = shippingCity
                    IyziCoResourcesConstans.IYZICO_SHIPPING_COUNTRY = shippingCountry
                    IyziCoResourcesConstans.IYZICO_SHIPPING_ADDRESS = shippingAddress
                    IyziCoResourcesConstans.IYZICO_SHIPPING_CONTACT_NAME = shippingContactName
                    IyziCoResourcesConstans.IYZICO_BUYER_COUNTRY = buyerCountry
                    IyziCoResourcesConstans.iyzicoBasketItemList =  basketItemList
                    Iyzico.iyzicoCallback = callback
                    IyziCoActivity.start(activity)
                }
            }

        }


    }


    override fun startCashOut(
        activity: Activity,
        email: String,//
        phone: String,
        walletPrice: Double,
        name: String?,
        surname: String?,
        callback: IyzicoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLIENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_IP, TextMessages.INVALID_CLIENT_IP)
            }
            IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
            }
            email.isEmpty() -> {
                callback.error(ResultCode.MISSING_MAIL, TextMessages.INVALID_EMAIL_NUMBER)
            }
            phone.isEmpty() -> {
                callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
            }
            phone.length != 10 -> {
                callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
            }
            walletPrice == null -> {
                callback.error(ResultCode.INVALID_WALLET_PRICE, TextMessages.INVALID_PRICE)
            }
            else -> {
                /**
                 * SDK'yı initialize etmek için kullanılır
                 */
                IyziCoConfig.IYZI_CO_SDK_TYPE = IyziCoSDKType.CASH_OUT
                IyziCoResourcesConstans.IyziCoEmail = email.toLowerCase()
                IyziCoResourcesConstans.IyziCoWalletPrice = walletPrice.toString()
                IyziCoResourcesConstans.IyziPhoneNumber = phone
                IyziCoResourcesConstans.IyziCoUserName = name ?: ""
                IyziCoResourcesConstans.IyziCoUserSurName = surname ?: ""
                Iyzico.iyzicoCallback = callback
                IyziCoActivity.start(activity)
            }
        }
    }

    /* override fun startRefund(
         activity: Activity,
         email: String,
         phone: String,
         productId: String,
         name: String?,
         surname: String?,
         callback: IyzicoCallback
     ) {
         when {
             IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                 callback.error(
                     ResultCode.MISSING_CLIENT_SECRET_KEY,
                     TextMessages.CLIENT_SECRET_KEY_ERROR_TEXT
                 )
             }
             IyziCoConfig.CLIENT_ID.isEmpty() -> {
                 callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
             }
             IyziCoConfig.CLIENT_IP.isEmpty() -> {
                 callback.error(ResultCode.MISSING_CLIENT_IP, TextMessages.INVALID_CLIENT_IP)
             }
             IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                 callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
             }
             email.isEmpty() -> {
                 callback.error(ResultCode.MISSING_MAIL, TextMessages.INVALID_EMAIL_NUMBER)
             }
             phone.isEmpty() -> {
                 callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
             }
             phone.length != 10 -> {
                 callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
             }
             productId.isEmpty() -> {
                 callback.error(ResultCode.MISSING_PRODUCT, TextMessages.INVALID_PRODUCT_ID)
             }
             else -> {
                 */
    /**
     * SDK'yı initialize etmek için kullanılır
     *//*
                IyziCoConfig.IYZI_CO_SDK_TYPE = IyziCoSDKType.REFUND
                IyziCoResourcesConstans.IYZICO_PRODUCT_ID = productId
                IyziCoResourcesConstans.IyziCoEmail = email.toLowerCase()
                IyziCoResourcesConstans.IyziPhoneNumber = phone
                IyziCoResourcesConstans.IyziCoUserName = name ?: ""
                IyziCoResourcesConstans.IyziCoUserSurName = surname ?: ""
                client().callback = callback
                IyziCoActivity.start(activity)
            }
        }
    }*/

    /* override fun startSettlement(
         activity: Activity,
         email: String,
         phone: String,
         walletPrice: Double,
         name: String?,
         surname: String?,
         callback: IyzicoCallback
     ) {
         when {
             IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                 callback.error(
                     ResultCode.MISSING_CLIENT_SECRET_KEY,
                     TextMessages.CLIENT_SECRET_KEY_ERROR_TEXT
                 )
             }
             IyziCoConfig.CLIENT_ID.isEmpty() -> {
                 callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
             }
             IyziCoConfig.CLIENT_IP.isEmpty() -> {
                 callback.error(ResultCode.MISSING_CLIENT_IP, TextMessages.INVALID_CLIENT_IP)
             }
             IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                 callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
             }
             email.isEmpty() -> {
                 callback.error(ResultCode.MISSING_MAIL, TextMessages.INVALID_EMAIL_NUMBER)
             }
             phone.isEmpty() -> {
                 callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
             }
             phone.length != 10 -> {
                 callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
             }
             walletPrice == null -> {
                 callback.error(ResultCode.INVALID_WALLET_PRICE, TextMessages.INVALID_PRICE)
             }
             else -> {
                 */
    /**
     * SDK'nın açılacağı tipi belirtmek amacıyla
     *//*
                IyziCoConfig.IYZI_CO_SDK_TYPE = IyziCoSDKType.SETTLEMENT
                IyziCoResourcesConstans.IyziCoWalletPrice = walletPrice.toString()
                IyziCoResourcesConstans.IyziCoEmail = email.toLowerCase()
                IyziCoResourcesConstans.IyziPhoneNumber = phone
                IyziCoResourcesConstans.IyziCoUserName = name ?: ""
                IyziCoResourcesConstans.IyziCoUserSurName = surname ?: ""
                client().callback = callback
                IyziCoActivity.start(activity)
            }
        }
    }*/

    override fun startTopUp(
        activity: Activity,
        email: String,
        phone: String,
        brand: String,
        name: String?,
        surname: String?,
        callback: IyzicoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLIENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_IP, TextMessages.INVALID_CLIENT_IP)
            }
            IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
            }
            brand.isEmpty() -> {
                callback.error(ResultCode.MISSING_BRAND, TextMessages.INVALID_BRAND)
            }
            email.isEmpty() -> {
                callback.error(ResultCode.MISSING_MAIL, TextMessages.INVALID_EMAIL_NUMBER)
            }
            phone.isEmpty() -> {
                callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
            }
            phone.length != 10 -> {
                callback.error(ResultCode.MISSING_PHONE, TextMessages.INVALID_PHONE_NUMBER)
            }
            else -> {
                /**
                 * SDK'nın açılacağı tipi belirtmek amacıyla
                 */
                IyziCoConfig.IYZI_CO_SDK_TYPE = IyziCoSDKType.TOPUP
                IyziCoResourcesConstans.IyziCoEmail = email.toLowerCase()
                IyziCoResourcesConstans.IyziPhoneNumber = phone
                IyziCoResourcesConstans.IyziCoUserName = name ?: ""
                IyziCoResourcesConstans.IyziCoUserSurName = surname ?: ""
                IyziCoResourcesConstans.IyziCoBrand = brand
                Iyzico.iyzicoCallback = callback
                IyziCoActivity.start(activity)
            }
        }
    }
}
