package com.android.iyzicosdk.util.enums

enum class ResultCode(var code: Int) {
    OK(1),
    FAIL(2),
    TIME_OUT(3),
    MISSING_PHONE(4),
    MISSING_MAIL(5),
    MISSING_BRAND(6),
    MISSING_PRICE(7),
    MISSING_PRODUCT(8),

    INVALID_WALLET_PRICE(9),//settlementte aldığımız walletPice kontrolü cashoutda da var
    MISSING_CLIENT_IP(10),
    MISSING_CLIENT_ID(11),
    MISSING_CLIENT_SECRET_KEY(12),
    MISSING_BASE_URL(13),
    MISSING_MERCHANT_API(14),
    MISSING_MERCHANT_SECRET(15),
    MISSING_PAID_PRICE(16),
    MISSING_URL_CALLBACK(17),
    MISSING_ENABLED_INSTALLMENTS(18),
    MISSING_BASKET_ID(19),
    MISSING_BUYER_ID(20),
    MISSING_BUYER_NAME(21),
    MISSING_BUYER_SURNAME(22),
    MISSING_BUYER_IDENTITY_NUMBER(23),
    MISSING_BUYER_CITY(24),
    MISSING_BUYER_COUNTRY(25),
    MISSING_BUYER_EMAIL(26),
    MISSING_BUYER_PHONE(27),
    MISSING_BUYER_IP(28),
    MISSING_BUYER_REGISTRATION_ADDRESS(29),
    MISSING_BUYER_ZIP_CODE(30),
    MISSING_BUYER_REGISTRATION_DATE(31),
    MISSING_BUYER_LAST_LOGIN_DATE(32),
    MISSING_BILLING_CONTACT_NAME(33),
    MISSING_BILLING_CITY(34),
    MISSING_SHIPPING_COUNTRY(35),
    MISSING_SHIPPING_ADDRESS(36),
    MISSING_EMPTY_BASKET(37),
    MISSING_FULL_BASKET(38),
    MISSING_PRODUCT_ID(39),
    MISSING_PRODUCT_NAME(40),
    MISSING_PRODUCT_CATEGORY(41),
    MISSING_BILLING_ADDRESS(42),
    MISSING_SHIPPING_CONTACT_NAME(43),
    MISSING_SHIPPING_CITY(44),
    MISSING_BILLING_COUNTRY(45),
    MISSING_LANGUAGE(46),
    CLOSED_TRANSACTION(47),
    BASKET_PRODUCT_PRICE_ERROR(48),
    BASKET_PRUDUCT_ITEM_TYPE_ERROR(49)


}