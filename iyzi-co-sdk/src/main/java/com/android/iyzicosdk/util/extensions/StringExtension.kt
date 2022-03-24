package com.android.iyzicosdk.util.extensions

import android.util.Base64
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.enums.Currency
import com.android.iyzicosdk.util.enums.IyziCoCardTypes
import com.android.iyzicosdk.util.enums.Languages
import com.android.iyzicosdk.util.enums.PaymentGroup
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Hex
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.regex.Matcher
import java.util.regex.Pattern

internal val DateStringPattern = "yyyy-MM-dd HH:mm:ss"

/**
 *Girilen email pattern e uygun mu diye kontrol eden method
 */
internal fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 *Girilen telefon numarasını noktalı hale çevirir
 */

internal fun String.convertServiceMaskedPhoneNumber(): String {
    var string = this.replace("*", "•")
    return string.substring(0, 3) + "(" + string.substring(3, 6) + ") " + string.substring(
        6,
        9
    ) + " " + string.substring(9, 11) + " " + string.substring(11, 13)
}

internal fun String.defaultButton(): String {
    return this + "," + "00"
}

//girilen dilin geçerli olup olmadığının kontrolünü yapar
internal fun String.isInvalidLanguage(): Boolean {
    when (this) {
        Languages.ENGLISH.type -> {
            return false
        }
        Languages.TURKISH.type -> {
            return false
        }
        else -> {
            return true
        }
    }
}

//girilen para biriminin geçerli olup olmadığının kontrolünü yapar
internal fun String.isInvalidCurrency(): Boolean {
    when (this) {
        Currency.TRY.type -> {
            return false
        }
        Currency.USD.type -> {
            return false
        }
        Currency.EUR.type -> {
            return false
        }
        Currency.GBP.type -> {
            return false
        }
        Currency.IRR.type -> {
            return false
        }
        else -> {
            return true
        }
    }
}

internal fun String.isAmex(): IyziCoCardTypes {
    val visaRegex = "^4\\d{0,16}\$".toRegex()
    val masterRegex = "^5[1-5]\\d{0,14}\$".toRegex()
    val amexRegex = "((^3[47])((\\d{0,11}\$)|(\\d{0,13}\$))|^311111111111117\$)".toRegex()
    val master2Regex = "^6[7]\\d{0,14}\$".toRegex()
    val master3Regex = "^5[8]\\d{0,14}\$".toRegex()
    val master4Regex = "^222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720\\d{0,12}\$".toRegex()
    var troyRegex = "(^9\\d{0,15}\$)|(^65\\d{0,14}\$)".toRegex()
    // val regex = "^3[47]\$".toRegex()
    //return regex.containsMatchIn(this)
    if (visaRegex.containsMatchIn(this)) {
        return IyziCoCardTypes.VISA
    } else if (masterRegex.containsMatchIn(this)) {
        return IyziCoCardTypes.MASTER
    } else if (master2Regex.containsMatchIn(this)) {
        return IyziCoCardTypes.MASTER2
    } else if (amexRegex.containsMatchIn(this)) {
        return IyziCoCardTypes.AMEX
    } else if (master3Regex.containsMatchIn(this)) {
        return IyziCoCardTypes.MASTER3
    } else if (master4Regex.containsMatchIn(this)) {
        return IyziCoCardTypes.MASTER4
    } else if (troyRegex.containsMatchIn(this)) {
        return IyziCoCardTypes.TROY
    } else {
        return IyziCoCardTypes.OTHERS
    }

}

//girilen ödeme grupunun geçerli olup olmadığını kontrol eder
internal fun String.isInvalidPaymentGroup(): Boolean {
    when (this) {
        PaymentGroup.PRODUCT.type -> {
            return false
        }
        PaymentGroup.LISTING.type -> {
            return false
        }
        PaymentGroup.SUBSCRIPTION.type -> {
            return false
        }
        else -> {
            return true
        }
    }
}

//telefon numarasını servis için uygun hale getirir
internal fun String.transformPhoneforService(): String {
    var phoneNumber = "+90"
    phoneNumber += this.substring(0, 3) + this.substring(4, 7) + this.substring(
        8,
        10
    ) + this.substring(11, 13)
    return phoneNumber
}

//telefon numarasını edittexte uygun hale getirir
internal fun String.editPhoneNumber(): String {
    return this.substring(0, 3) + " " + this.substring(3, 6) + " " + this.substring(
        6,
        8
    ) + " " + substring(8, 10)
}

//girilen fiyat alanının değerini kontrol eder
internal fun String.isInvalidPrice(): Boolean {
    return !(this.isNullOrEmpty() || this == "₺0,00")
}

//telefon numarasının geçerli olup olmadığını kontrol eder
internal fun String.isNotPhoneNumber(): Boolean {
    return (this == null || this.length != 13)
}

//amount değerini 2+2 hale getirir
internal fun String.convertAmount(): String {
    try {
        if (this.length >= 7) {
            return this.substring(0, this.length - 6)
        } else {
            return this
        }
    } catch (e: Exception) {
        return BundleConstans.ZERO_MONEY
    }
}

internal fun String.convertForDouble(): Double {
    return this.replace(",", ".").toDouble()
}

//başına tl ikonu ekler
internal fun String.addTlIcon(): String {
    return try {
        if (this.substring(this.length - 2).contains(".")) {
            "₺" + this.replace(".", ",") + "0"
        } else {
            "₺" + this.replace(".", ",")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "₺$this,00"
    }

}

//Stringi encode etmek için kullanıldı
internal fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
}

//girilen değerlerdeki noktaları düzeltir
internal fun String.setWalletPrice(): String {
    return this.replace(".", ",")
}


internal fun String.invalidCardNumber(isAmex: Boolean): Boolean {
    if (isAmex) {
        return this.length != 17

    } else return this.length != 19
}

internal fun String.invalidCvc(isAmex: Boolean): Boolean {
    if (isAmex) {
        return this.length != 4

    } else return this.length != 3
}

//tarihin güncel olup olmadığını kontrol ettik
internal fun String.invalidDexpirationDate(currentMount: Int, currentYear: Int): Boolean {
    if (this.length == 5) {
        var input = this.split("/")

        if (input[0].toInt() > 12 || input[0].toInt() < 1) {
            return true
        }
        if ((((input[1].toInt() + 2000) * 12) + input[0].toInt()) >= ((currentYear * 12) + currentMount)) {
            return false
        } else {
            return true
        }
    } else return true
}

internal fun String.clearFunNumber(): String {
    return this.substring(0, 3) + this.substring(5, 8) + this.substring(10, 13) + this.substring(
        14,
        16
    ) + this.substring(17, this.length)
}

internal fun String.withDrawnCreditCard(text: String): String {
    return text.replace("XXX", this.addTlIcon())
}

/**
 * Requesti şifrelemek için kullanıldı
 */
internal fun String.createSignature(): String {
    try {
        val sha256_HMAC = Mac.getInstance("HmacSHA256")
        if (IyziCoConfig.MERCHANT_SECRET_KEY.isNotEmpty() && IyziCoConfig.MERCHANT_API_KEY.isNotEmpty()) {
            var secret_key = SecretKeySpec(
                "${IyziCoConfig.CLIENT_SECRET_ID}:${IyziCoConfig.MERCHANT_SECRET_KEY}".toByteArray(
                    charset("UTF-8")
                ), "HmacSHA256"
            )
            sha256_HMAC.init(secret_key)
            return String(Hex.encodeHex(sha256_HMAC.doFinal(this.toByteArray(charset("UTF-8")))))

        } else {
            var secret_key = SecretKeySpec(
                IyziCoConfig.CLIENT_SECRET_ID.toByteArray(charset("UTF-8")),
                "HmacSHA256"
            )
            sha256_HMAC.init(secret_key)
            return String(Hex.encodeHex(sha256_HMAC.doFinal(this.toByteArray(charset("UTF-8")))))

        }
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }

    return ""
}

/**
 * Resource'e eşitlemek için kullanıldı
 */
internal fun String.clearSpace(): String {
    return this.replace(" ", "")
}

//Card numarasındaki boşlukları siler
internal fun String.cardtobinNumber(): String {
    return this.replace(" ", "")
}

//Date formatı istenilene uygun mu diye kontrol eder
internal fun String.isInvalidDate(): Boolean {
    var pattern: Pattern = Pattern.compile(DateStringPattern)
    var matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}
