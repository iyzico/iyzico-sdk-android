package com.android.iyzicosdk.ui.adapter

import com.android.iyzicosdk.data.model.response.IyziCoBankItem

internal interface IyziCoBankClickListener {
    fun clickBank(itemCount: Int,item:IyziCoBankItem)
}