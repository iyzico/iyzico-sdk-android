package com.android.iyzicosdk.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.android.iyzicosdk.R
import com.android.iyzicosdk.data.model.response.IyziCoBankItem
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility
import kotlinx.android.synthetic.main.iyzico_cell_bank_item.view.*

internal class IyziCoBankAdapter(
    private val bankClickListener: IyziCoBankClickListener,
    context: Context
) :
    IyziCoBaseAdapter<IyziCoBankItem>() {
    var cont = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IyziCoBaseViewHolder<IyziCoBankItem> {
        onItemClick {
            bankClickListener.clickBank(itemCount, it)
        }
        return CardViewHolder(parent, cont)
    }

    class CardViewHolder(parent: ViewGroup, context: Context) :
        IyziCoBaseViewHolder<IyziCoBankItem>(parent, R.layout.iyzico_cell_bank_item) {
        var context = context
        override fun bind(item: IyziCoBankItem) {
            itemView.iyzico_cell_bank_item_bank_name_textView.setText(item.bankName)
            IyziCoImageLoaderUtility.setImageForSvg(
                context,
                item.bankLogoUrl?:"",
                itemView.iyzico_cell_bank_imageView
            )
            itemView.setOnClickListener {

            }
        }
    }
}