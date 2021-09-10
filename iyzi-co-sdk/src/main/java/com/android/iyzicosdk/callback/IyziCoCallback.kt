package com.android.iyzicosdk.callback

import com.android.iyzicosdk.util.enums.ResultCode

interface IyziCoCallback {
    fun message(message: String)
    fun error(code: ResultCode, message: String)
}