package com.android.iyzicosdk.ui.adapter

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.data.model.response.IyziCoCardItem
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility.Companion.imageLoaderWithCacheFit
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility.Companion.setImageForSvg
import com.android.iyzicosdk.util.SvgSoftwareLayerSetter
import com.android.iyzicosdk.util.enums.IyziCoCardsType
import com.android.iyzicosdk.util.extensions.*
import com.android.iyzicosdk.util.extensions.changeBackground
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.setOnSafeClickListener
import com.android.iyzicosdk.util.extensions.show

import kotlinx.android.synthetic.main.iyzico_cell_card_item.view.*
import kotlinx.android.synthetic.main.iyzico_cell_card_item.view.iyzico_double_border_square_check_imageview
import kotlinx.android.synthetic.main.iyzico_double_border.view.*


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
                itemView.iyzico_bonus_point_container.setVisible(iyziCoCardItem.bonusAvailable)

            } else {
                itemView.iyzico_cell_card_item_selected_button.setImageResource(R.drawable.iyzico_ic_empty_radio)
                itemView.iyzico_bonus_point_container.gone()
            }
            itemView.iyzico_cell_card_item_card_name_textview.apply {
                when (iyziCoCardItem.cardType) {
                    IyziCoCardsType.CREDIT_CARD.toString() -> {
                        this.text = IyziCoCardsType.CREDIT_CARD.type
                    }
                    IyziCoCardsType.DEBIT_CARD.toString() -> {
                        this.text = IyziCoCardsType.DEBIT_CARD.type
                    }
                }
            }
            itemView.iyzico_cell_card_bank_name_textView.text = iyziCoCardItem.cardBankName
            itemView.iyzico_cell_card_item_card_number_textview.text = iyziCoCardItem.lastFourDigits


            setImageForSvg(
                context,
                iyziCoCardItem.cardAssociationLogoUrl ?: "",
                itemView.iyzico_cell_card_bank_imageView
            )

            prepareCheckBoxForPoint(iyziCoCardItem)



            with(itemView) {
                iyzico_double_border_square_check_imageview.setOnSafeClickListener {
                    iyziCoCardItem.bonusPointSelected = !iyziCoCardItem.bonusPointSelected
                    prepareCheckBoxForPoint(iyziCoCardItem)
                }

                iyzico_bonus_point_amount_textview.text = iyziCoCardItem.bonusPointAmount?.toPrice()
                if (iyziCoCardItem.isIyziCoCard) {
                    with(iyzico_cell_card_bank_name_textView) {
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.iyzico_secondary_dark_blue
                            )
                        )
                        textSize = 15f
                    }
                }
            }

/*
*/
        }

        private fun prepareCheckBoxForPoint(iyziCoCardItem: IyziCoCardItem) {
            with(itemView) {
                if (iyziCoCardItem.bonusPointSelected) {
                    iyzico_double_border_square_check_imageview.background = null
                    iyzico_double_border_square_check_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_button)
                } else {
                    iyzico_double_border_square_check_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_clear_button)
                    iyzico_double_border_square_check_imageview.changeBackground(R.drawable.iyzico_empty_radio_button_unchecked)
                }
            }
        }
    }
}