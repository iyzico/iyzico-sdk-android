package com.android.iyzicosdk.ui.adapter

import com.android.iyzicosdk.data.model.response.IyziCoBankItem
import com.android.iyzicosdk.data.model.response.IyziCoInstallmentDetail

internal interface IyziCoInstallmentClickListener {
    fun clickInstallment(itemCount: Int,item: IyziCoInstallmentDetail)
}
