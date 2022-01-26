package com.android.iyzicosdk.util.enums

import androidx.annotation.Keep


internal enum class IyziCoPaymentType {
    NULL, BALANCE, CARD_WITH_BALANCE, CARD_WITHOUT_BALANCE, NEW_CARD_WITH_BALANCE, NEW_CARD_WITHOUT_BALANCE, NEW_CARD, CARD
}

internal enum class IyziCoInfoScreenType {
    CASHOUT_WAİT, TOPUP_WAİT, TRANSFER, PAYMENT, TRANSFER_CONFIRMATION, ERROR, THREE_D, TRANSFER_CONFIRMATION_TO_TOPUP, SETTLEMENT_SUCCESS, REFAUND_SUCCESS
}

internal enum class IyziCoLoginScreenType {
    NORMAL,
    CHANGE_PHONE,
    TRY_AGAIN,
    CHANGE_MAIL,
}

internal enum class IyziCoCardTypes {
    AMEX,
    VISA,
    MASTER,
    MASTER2,
    MASTER3,
    MASTER4,
    TROY,
    OTHERS
}

internal enum class IyziCoDepositStatus {
    WAITING_FOR_PROVISION,
    APPROVED
}

internal enum class IyziCoSDKType {
    PAY_WITH_IYZI_CO,
    CASH_OUT,
    REFUND,
    TOPUP,
    SETTLEMENT
}

internal enum class IyziCoLoginType {
    NORMAL,
    PHONE_CHANGE,
}

internal enum class IyziCoPopupType {
    CLOSE_OR_CONTINUE_PAGE,
    ERROR_POPUP,
    TIMEOUT_POPUP
}

internal enum class IyziCoBottomSheetType {
    TRANSFER_DETAIL,
    TRANSFER_SEE_INFO,
    TRANSFER_TOP_UP
}

internal enum class IyziCoButtonType {
    BLUE_FILL,
    EMPTY,
    RED_FILL,
    EMPTY_GRAY_BORDER
}

internal enum class IyziCoSupportScreenType {
    SUPPORT,
    USER_AGREEMENT,
    PRIVACY_POLICY,
    COMMUNICATION,
    KVKK_PWI,
    USER_AGREMENT_PWI,
    THREED_PAYMENT
}

internal enum class IyziCoLoginChannelType(var type: String) {
    THIRD_PARTY_APP("THIRD_PARTY_APP")
}

internal enum class IyziCoCurrentType(var type: String) {
    TRY("TRY")
}

internal enum class IyziCoCardsType(var type: String) {
    CREDIT_CARD("Kredi Kartı"),
    DEBIT_CARD("Banka Kartı")
}

internal enum class IyziCoTransactionType(var type: String) {
    DEPOSIT("DEPOSIT")
}

internal enum class IyziCoInstallmentType(var type: String) {
    NORMAL("NORMAL"),
    NEW_CARD("NEW_CARD")
}

enum class Languages(var type: String) {
    TURKISH("tr"),
    ENGLISH("en")
}

enum class Currency(var type: String) {
    TRY("TRY"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    IRR("IRR")
}

enum class PaymentGroup(var type: String) {
    PRODUCT("PRODUCT"),
    LISTING("LISTING"),
    SUBSCRIPTION("SUBSCRIPTION")
}

enum class BasketItemType(var type: String) {
    PHYSICAL("PHYSICAL"),
    VIRTUAL("VIRTUAL")
}
