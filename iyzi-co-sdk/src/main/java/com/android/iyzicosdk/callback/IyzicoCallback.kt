package com.android.iyzicosdk.callback

import androidx.annotation.Keep
import com.android.iyzicosdk.util.enums.ResultCode

interface IyzicoCallback {
    fun message(message: String)
    fun error(code: ResultCode, message: String)
}