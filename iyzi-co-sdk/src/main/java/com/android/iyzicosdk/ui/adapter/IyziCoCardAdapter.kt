package com.android.iyzicosdk.ui.adapter

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.view.ViewGroup
import com.android.iyzicosdk.R
import com.android.iyzicosdk.data.model.response.IyziCoCardItem
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility.Companion.imageLoaderWithCacheFit
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility.Companion.setImageForSvg
import com.android.iyzicosdk.util.SvgSoftwareLayerSetter
import com.android.iyzicosdk.util.enums.IyziCoCardsType

import kotlinx.android.synthetic.main.iyzico_cell_card_item.view.*


internal class IyziCoCardAdapter(context: Context) : IyziCoBaseAdapter<IyziCoCardItem>() {
    var cont = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IyziCoBaseViewHolder<IyziCoCardItem> {
        return CardViewHolder(parent, cont)
    }

    class CardViewHolder(parent: ViewGroup, context: Context) :
        IyziCoBaseViewHolder<IyziCoCardItem>(parent, R.layout.iyzico_cell_card_item) {
        var context = context
        override fun bind(iyziCoCardItem: IyziCoCardItem) {
            if (iyziCoCardItem.isSelected) {
                itemView.iyzico_cell_card_item_selected_button.setImageResource(R.drawable.iyzico_ic_check_button)
            } else {
                itemView.iyzico_cell_card_item_selected_button.setImageResource(R.drawable.iyzico_ic_empty_radio)
            }
            itemView.iyzico_cell_card_item_card_name_textview.apply {
                when (iyziCoCardItem.cardType) {
                    IyziCoCardsType.CREDIT_CARD.toString() -> {
                        this.setText(IyziCoCardsType.CREDIT_CARD.type)
                    }
                    IyziCoCardsType.DEBIT_CARD.toString() -> {
                        this.setText(IyziCoCardsType.DEBIT_CARD.type)
                    }
                }
            }
            itemView.iyzico_cell_card_bank_name_textView.text = iyziCoCardItem.cardBankName
            itemView.iyzico_cell_card_item_card_number_textview.text =
                "* * * " + iyziCoCardItem.lastFourDigits


            setImageForSvg(
                context,
                iyziCoCardItem.cardAssociationLogoUrl?:"",
                itemView.iyzico_cell_card_bank_imageView
            )
/*
*/


        }
    }
}