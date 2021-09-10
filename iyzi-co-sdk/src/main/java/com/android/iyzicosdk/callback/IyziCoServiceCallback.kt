package com.android.iyzicosdk.callback

internal interface IyziCoServiceCallback<T> {
    fun onSuccess(data: T?)
    fun onError(code: Int, message: String)
}