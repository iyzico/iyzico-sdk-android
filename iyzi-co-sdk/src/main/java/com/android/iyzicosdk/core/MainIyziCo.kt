package com.android.iyzicosdk.core

import android.app.Activity
import android.graphics.Bitmap
import com.android.iyzicosdk.callback.IyziCoCallback
import com.android.iyzicosdk.data.model.request.IyziCoBasketItem
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.constants.TextMessages
import com.android.iyzicosdk.util.enums.*
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.extensions.isInvalidCurrency
import com.android.iyzicosdk.util.extensions.isInvalidDate
import com.android.iyzicosdk.util.extensions.isInvalidLanguage
import com.android.iyzicosdk.util.extensions.isInvalidPaymentGroup

internal class MainIyziCo : IyziCo() {

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

    override fun startPayWithIyziCo(
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
        basketItems: List<IyziCoBasketItem>,
        callback: IyziCoCallback
    ) {
        var bool: Boolean = true

        for (item in basketItems) {
            when {
                item.id.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCTID,
                        TextMessages.INVALID_PRODUCTID
                    )
                }
                item.name.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCTNAME,
                        TextMessages.INVALID_PRODUCTNAME
                    )
                }
                item.category1.isEmpty() -> {
                    bool = false
                    callback.error(
                        ResultCode.MISSING_PRODUCTCATEGORY,
                        TextMessages.INVALID_PRODUCTCATEGORY
                    )
                }
                item.itemType.type.equals(BasketItemType.PHYSICAL.type) -> {
                    if (shippingContactName.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPINGCONTACTNAME,
                            TextMessages.INVALID_SHIPPINGCONTACTNAME
                        )
                    } else if (shippingCity.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPINGCITY,
                            TextMessages.INVALID_SHIPPINGCITY
                        )
                    } else if (shippingCountry.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPINGCOUNTRY,
                            TextMessages.INVALID_SHIPPINGCOUNTRY
                        )
                    } else if (shippingAddress.isEmpty()) {
                        bool = false
                        callback.error(
                            ResultCode.MISSING_SHIPPINGADDRESS,
                            TextMessages.INVALID_SHIPPINGADDRESS
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
                        TextMessages.CLİENT_SECRET_KEY_ERROR_TEXT
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
                    callback.error(ResultCode.MISSING_CLIENTIP, TextMessages.INVALID_CLIENT_IP)
                }
                IyziCoConfig.LANGUAGE.type.isInvalidLanguage() -> {
                    callback.error(ResultCode.MISSING_LANGUAGE, TextMessages.INVALID_LANGUAGE)
                }
                brand.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BRAND, TextMessages.INVALID_BRAND)
                }
                buyerEmail.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYEREMAIL, TextMessages.INVALID_EMAIL_NUMBER)
                }
                buyerPhone.length != 10 -> {
                    callback.error(ResultCode.MISSING_BUYERPHONE, TextMessages.INVALID_PHONE_NUMBER)
                }
                price == null -> {
                    callback.error(ResultCode.MISSING_PRICE, TextMessages.INVALID_PRICE)
                }
                paidPrice == null -> {
                    callback.error(ResultCode.MISSING_PAIDPRICE, TextMessages.INVALID_PAIDPRICE)
                }
                currency.type.isInvalidCurrency() -> {
                    currency.type = Currency.TL.type
                }
                enabledInstallments.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_ENABLEDINSTALLMENTS,
                        TextMessages.INVALID_ENABLEDINSTALLMENTS
                    )
                }
                basketId.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BASKETID, TextMessages.INVALID_BASKETID)
                }
                paymentGroup.type.isInvalidPaymentGroup() -> {
                    paymentGroup.type = PaymentGroup.PRODUCT.type
                }
                urlCallback.isEmpty() -> {
                    callback.error(ResultCode.MISSING_URLCALLBACK, TextMessages.INVALID_URLCALLBACK)
                }
                buyerId.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYERID, TextMessages.INVALID_BUYERID)
                }
                buyerName.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYERNAME, TextMessages.INVALID_BUYERNAME)
                }
                buyerSurname.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERSURNAME,
                        TextMessages.INVALID_BUYERSURNAME
                    )
                }
                buyerIdentityNumber.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERIDENTITYNUMBER,
                        TextMessages.INVALID_BUYERIDENTITYNUMBER
                    )
                }
                buyerIdentityNumber.length != 11 -> {
                    callback.error(
                        ResultCode.MISSING_BUYERIDENTITYNUMBER,
                        TextMessages.INVALID_BUYERIDENTITYNUMBER
                    )
                }
                buyerCity.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYERCITY, TextMessages.INVALID_BUYERCITY)
                }
                buyerCountry.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERCOUNTRY,
                        TextMessages.INVALID_BUYERCOUNTRY
                    )
                }
                buyerIp.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BUYERIP, TextMessages.INVALID_BUYERIP)
                }
                buyerRegistrationAddress.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERREGISTRATIONADDRESS,
                        TextMessages.INVALID_BUYERREGISTRATIONADDRESS
                    )
                }
                buyerZipCode.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERZIPCODE,
                        TextMessages.INVALID_BUYERZIPCODE
                    )
                }
                buyerRegistrationDate.isInvalidDate() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERREGISTRATIONDATE,
                        TextMessages.INVALID_BUYERREGISTRATIONDATE
                    )
                }
                buyerLastLoginDate.isInvalidDate() -> {
                    callback.error(
                        ResultCode.MISSING_BUYERLASTLOGINDATE,
                        TextMessages.INVALID_BUYERLASTLOGINDATE
                    )
                }
                billingContactName.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLINGCONTACTNAME,
                        TextMessages.INVALID_BILLINGCONTACTNAME
                    )
                }
                billingCity.isEmpty() -> {
                    callback.error(ResultCode.MISSING_BILLINGCITY, TextMessages.INVALID_BILLINGCITY)
                }
                billingCountry.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLINGCOUNTRY,
                        TextMessages.INVALID_BILLINGCOUNTRY
                    )
                }
                billingAddress.isEmpty() -> {
                    callback.error(
                        ResultCode.MISSING_BILLINGADDRESS,
                        TextMessages.INVALID_BILLINGADDRESS
                    )
                }
                basketItems.isEmpty() -> {
                    callback.error(ResultCode.MISSING_EMPTYBASKET, TextMessages.INVALID_EMPTYBASKET)
                }
                basketItems.size > 500 -> {
                    callback.error(ResultCode.MISSING_FULLBASKET, TextMessages.INVALID_FULLBASKET)
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
                    IyziCoResourcesConstans.IYZICO_ENABLED_INSTALLMENTS =
                        enabledInstallments
                    IyziCoResourcesConstans.IYZICO_BASKET_ID = basketId
                    IyziCoResourcesConstans.IYZICO_PAYMENT_GROUP = paymentGroup.type
                    IyziCoResourcesConstans.IYZICO_CALLBACK_URL = urlCallback
                    IyziCoResourcesConstans.IYZICO_ID = buyerId
                    IyziCoResourcesConstans.IYZICO_BUYER_IDENTITY_NUMBER =
                        buyerIdentityNumber
                    IyziCoResourcesConstans.IYZICO_BUYER_CITY = buyerCity
                    IyziCoResourcesConstans.IYZICO_BUYER_IP = buyerIp
                    IyziCoResourcesConstans.IYZICO_REGISTRATION_ADRESS =
                        buyerRegistrationAddress
                    IyziCoResourcesConstans.IYZICO_BUYER_REGISTRATION_DATE =
                        buyerRegistrationDate
                    IyziCoResourcesConstans.IYZICI_BUYER_LAST_LOGIN_DATE =
                        buyerLastLoginDate
                    IyziCoResourcesConstans.IYZICO_BILLING_CONTACT_NAME = billingContactName
                    IyziCoResourcesConstans.IYZICO_BILLING_CITY = billingCity
                    IyziCoResourcesConstans.IYZICO_BILLING_COUNTRY = billingCountry
                    IyziCoResourcesConstans.IYZICO_BILLING_ADRESS = billingAddress
                    IyziCoResourcesConstans.IYZICO_SHIPPING_CITY = shippingCity
                    IyziCoResourcesConstans.IYZICO_SHIPPING_COUNTRY = shippingCountry
                    IyziCoResourcesConstans.IYZICO_SHIPPING_ADDRESS = shippingAddress
                    IyziCoResourcesConstans.IYZICO_PRODUCT_ITEM_TYPE = itemType
                    IyziCoResourcesConstans.IYZICO_PRODUCT_NAME = itemName
                    IyziCoResourcesConstans.IYZOCO_PRODUCT_CATEGORY = itemCategory
                    IyziCoResourcesConstans.IYZICO_SHIPPING_CONTACT_NAME =
                        shippingContactName
                    IyziCoResourcesConstans.IYZICO_BUYER_ZIP_CODE = buyerZipCode
                    IyziCoResourcesConstans.IYZICO_BUYER_COUNTRY = buyerCountry
                    IyziCoResourcesConstans.IYZICO_BASKET_ITEM_LIST = basketItems
                    client().callback = callback
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
        callback: IyziCoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLİENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENTIP, TextMessages.INVALID_CLIENT_IP)
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
                callback.error(ResultCode.MISSING_PRICE, TextMessages.INVALID_PRICE)
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
                client().callback = callback
                IyziCoActivity.start(activity)
            }
        }
    }

    override fun startRefund(
        activity: Activity,
        email: String,
        phone: String,
        productId: String,
        name: String?,
        surname: String?,
        callback: IyziCoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLİENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENTIP, TextMessages.INVALID_CLIENT_IP)
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
                /**
                 * SDK'yı initialize etmek için kullanılır
                 */
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
    }

    override fun startSettlement(
        activity: Activity,
        email: String,
        phone: String,
        walletPrice: Double,
        name: String?,
        surname: String?,
        callback: IyziCoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLİENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENTIP, TextMessages.INVALID_CLIENT_IP)
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
                callback.error(ResultCode.MISSING_PRICE, TextMessages.INVALID_PRICE)
            }
            else -> {
                /**
                 * SDK'nın açılacağı tipi belirtmek amacıyla
                 */
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
    }

    override fun startTopUp(
        activity: Activity,
        email: String,
        phone: String,
        brand: String,
        name: String?,
        surname: String?,
        callback: IyziCoCallback
    ) {
        when {
            IyziCoConfig.CLIENT_SECRET_ID.isEmpty() -> {
                callback.error(
                    ResultCode.MISSING_CLIENT_SECRET_KEY,
                    TextMessages.CLİENT_SECRET_KEY_ERROR_TEXT
                )
            }
            IyziCoConfig.CLIENT_ID.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENT_ID, TextMessages.INVALID_API_KEY)
            }
            IyziCoConfig.CLIENT_IP.isEmpty() -> {
                callback.error(ResultCode.MISSING_CLIENTIP, TextMessages.INVALID_CLIENT_IP)
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
                client().callback = callback
                IyziCoActivity.start(activity)
            }
        }
    }
}