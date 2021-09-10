package com.android.iyzicosdk.util.extensions

import java.util.concurrent.TimeUnit


internal fun Double.toPrice(): String {
    val price = this

    val priceString = price.toString()

    when {
        priceString.contains(",") -> {
            val list = priceString.split(",")

            var digit = ""
            digit = if (list[1].length == 1) {
                list[1] + "0"
            } else {
                list[1]
            }
            return " ₺"+list[0] + "," + digit.take(2)
        }
        priceString.contains(".") -> {
            val list = priceString.split(".")

            var digit = ""
            digit = if (list[1].length == 1) {
                list[1] + "0"
            } else {
                list[1]
            }
            return " ₺"+list[0] + "," + digit.take(2)
        }
        else -> {
            return "₺$this,00"
        }
    }
}

internal fun Long.toTimer(finishHandler: () -> Unit):String {
    if(this == 0L){
        finishHandler.invoke()
        return ""
    }
    return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    );
}

