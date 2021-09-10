package com.android.iyzicosdk.util.config

import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.Languages


internal object IyziCoConfig {
    lateinit var BASE_PATH: String

    /**
     * SDK'nın açılma tipini yönetmek için kullanılıyor
     */
    lateinit var IYZI_CO_SDK_TYPE: IyziCoSDKType

    /**
     * Kullanıcının AuthorizationKey bilgisini tutmak amacıyla kullanılıyor
     */
    var IYZI_CO_AUTHORIZATION_KEY: String? = ""

    /**
     * IYZICO CLIENT_ID
     */
    var CLIENT_ID = "qumpara"

    /**
     * IYZICO_SECRET_ID
     */
    var CLIENT_SECRET_ID = "qumparaSecret"

    /**
     * IYZICO_LANGUAGE
     */
    lateinit var LANGUAGE:Languages
    /**
     * IYZICO_CLIENT_IP
     */
    var CLIENT_IP = "qumparaIp"

    /**
     * IYZICO_MERCHANT_API_KEY
     */
    var MERCHANT_API_KEY = ""

    /**
     * IYZICO_MERCHANT_SECRET_KEY
     */
    var MERCHANT_SECRET_KEY = ""

}