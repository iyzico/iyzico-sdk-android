package com.android.iyzicosdk.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.android.iyzicosdk.R
import com.android.iyzicosdk.data.model.response.IyziCoCardItem
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility.Companion.setImageForSvg
import com.android.iyzicosdk.util.enums.IyziCoCardsType
import com.android.iyzicosdk.util.extensions.*
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
            val scale = context.resources.displayMetrics.density;
            val padding_12dp = (12 * scale + 0.5f).toInt()

            if (iyziCoCardItem.isSelected) {
                itemView.iyzico_cell_card_item_selected_button.setImageResource(R.drawable.iyzico_ic_check_button)
                itemView.iyzico_bonus_point_container.setVisible(iyziCoCardItem.bonusAvailable)
                if (iyziCoCardItem.bonusAvailable) {
                    itemView.clrootView.setPadding(0, padding_12dp,0, 0)
                }


            } else {
                itemView.iyzico_cell_card_item_selected_button.setImageResource(R.drawable.iyzico_ic_empty_radio)
                itemView.iyzico_bonus_point_container.gone()
                itemView.clrootView.setPadding(0, padding_12dp, 0, padding_12dp)
            }
            itemView.iyzico_cell_card_item_card_name_textview.apply {
                if (!iyziCoCardItem.isIyziCoCard) {
                    when (iyziCoCardItem.cardType) {
                        IyziCoCardsType.CREDIT_CARD.toString() -> {
                            this.text = IyziCoCardsType.CREDIT_CARD.type
                        }
                        IyziCoCardsType.DEBIT_CARD.toString() -> {
                            this.text = IyziCoCardsType.DEBIT_CARD.type
                        }
                    }

                    if (iyziCoCardItem.isIyzicoVirtualCard) {
                        text = context.getString(R.string.iyzico_virtul_card)
                    }
                } else {
                    text = if (iyziCoCardItem.isIyzicoVirtualCard) {
                        context.getString(R.string.iyzico_virtul_card)
                    } else {
                        context.getString(R.string.iyzico_card)
                    }
                }

            }
            itemView.iyzico_cell_card_bank_name_textView.text = iyziCoCardItem.cardBankName
            itemView.iyzico_cell_card_item_card_number_textview.text = iyziCoCardItem.lastFourDigits


            if (!iyziCoCardItem.passiveIyziCoCard) {
                setImageForSvg(
                    context,
                    iyziCoCardItem.cardAssociationLogoUrl ?: "",
                    itemView.iyzico_cell_card_bank_imageView
                )
            }


            prepareCheckBoxForPoint(iyziCoCardItem)



            with(itemView) {

                if (iyziCoCardItem.bonusAvailable && iyziCoCardItem.isSelected) {

                    iyzico_bonus_point_container.setOnSafeClickListener {
                        iyziCoCardItem.bonusPointSelected = !iyziCoCardItem.bonusPointSelected
                        prepareCheckBoxForPoint(iyziCoCardItem)
                    }




                    if (iyziCoCardItem.useBalance) {

                        val mix = iyziCoCardItem.balance + iyziCoCardItem.bonusPointAmount

                        if (mix > iyziCoCardItem.paidPrice) {
                            val newPointAmount = iyziCoCardItem.paidPrice - iyziCoCardItem.balance

                            if (newPointAmount >= iyziCoCardItem.bonusPointAmount) {
                                iyzico_bonus_point_amount_textview.text =
                                    iyziCoCardItem.bonusPointAmount.toPrice()

                                iyziCoCardItem.useRewardPoint = iyziCoCardItem.bonusPointAmount
                            } else {
                                iyzico_bonus_point_amount_textview.text =
                                    newPointAmount.toPrice()

                                iyzico_bonus_point_total_amount_textview.apply {
                                    show()
                                    val content =
                                        "${context.getString(R.string.iyzico_total_point)}: ${iyziCoCardItem.bonusPointAmount.toPrice()} "
                                    text = content

                                    val firstIndex = content.indexOfFirst { it == ':' }

                                    spannableExtension(
                                        firstIndex,
                                        content.length,
                                        R.color.iyzico_dark_grey,
                                        clickSpan = {}
                                    )
                                }
                                iyziCoCardItem.useRewardPoint = newPointAmount
                            }

                        } else {
                            iyzico_bonus_point_amount_textview.text =
                                iyziCoCardItem.bonusPointAmount.toPrice()

                            iyziCoCardItem.useRewardPoint = iyziCoCardItem.bonusPointAmount
                        }

                    } else {
                        if (iyziCoCardItem.bonusPointAmount > iyziCoCardItem.paidPrice) {

                            iyzico_bonus_point_total_amount_textview.apply {
                                show()
                                val content =
                                    "${context.getString(R.string.iyzico_total_point)}: ${iyziCoCardItem.bonusPointAmount.toPrice()} "
                                text = content


                                val firstIndex = content.indexOfFirst { it == ':' }

                                spannableExtension(
                                    firstIndex,
                                    content.length - 1,
                                    R.color.iyzico_dark_grey,
                                    clickSpan = {}
                                )
                            }

                            iyzico_bonus_point_amount_textview.text =
                                iyziCoCardItem.paidPrice.toPrice()

                            iyziCoCardItem.useRewardPoint = iyziCoCardItem.paidPrice

                        } else {
                            iyzico_bonus_point_amount_textview.text =
                                iyziCoCardItem.bonusPointAmount.toPrice()

                            iyziCoCardItem.useRewardPoint = iyziCoCardItem.bonusPointAmount
                        }
                    }


                }


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

                    if (iyziCoCardItem.passiveIyziCoCard) {
                        passiveCell()
                    }

                    iyzico_card_information_text.setVisible(iyziCoCardItem.showBalanceInfoMessage)

                }
            }
/*
*/
        }

        private fun passiveCell() {
            with(itemView) {
                isEnabled = false
                iyzico_cell_card_bank_imageView.setImageResource(R.drawable.passive_mastercard_color)
                iyzico_cell_card_bank_name_textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.iyzico_dark_grey
                    )
                )
                iyzico_cell_card_item_card_name_textview.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.iyzico_dark_grey
                    )
                )
                dottextview.setTextColor(ContextCompat.getColor(context, R.color.iyzico_dark_grey))
                iyzico_cell_card_item_card_number_textview.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.iyzico_dark_grey
                    )
                )

                alpha = 0.7f
            }
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