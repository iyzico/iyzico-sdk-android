package com.android.iyzicosdk.ui.adapter

import android.content.Context
import android.content.res.Resources
import android.view.ViewGroup
import com.android.iyzicosdk.R
import com.android.iyzicosdk.data.model.response.IyziCoInstallmentDetail
import com.android.iyzicosdk.data.model.response.IyziCoInstallmentPrice
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.toPrice
import kotlinx.android.synthetic.main.iyzico_cell_installement_item.view.*
import kotlinx.android.synthetic.main.iyzico_double_border.view.*

internal class IyziCoInstallmentAdapter(context: Context) :
    IyziCoBaseAdapter<IyziCoInstallmentPrice>() {
    var context=context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IyziCoBaseViewHolder<IyziCoInstallmentPrice> {
        return InstallmentViewHolder(parent, itemCount,context)
    }

    class InstallmentViewHolder(parent: ViewGroup, size: Int,context:Context) :
        IyziCoBaseViewHolder<IyziCoInstallmentPrice>(
            parent,
            R.layout.iyzico_cell_installement_item
        ) {
        var context = context
        var listSize = size
        override fun bind(item: IyziCoInstallmentPrice) {
            itemView.iyzico_cell_installment_piece_textView.text = item.installmentNumber.toString()
            if (item.installmentPrice > 0) {
                itemView.iyzico_cell_installment_piece_amount_textView.text =
                    "x ${item.installmentPrice.toPrice()}"
            } else {
                itemView.iyzico_cell_installment_piece_amount_textView.text = ""
            }
            itemView.iyzico_cell_installment_total_amount_textView.text = item.totalPrice.toPrice()

            if (item.isChecked) {
                itemView.iyzico_cell_installment_checked_imageView.setImageResource(R.drawable.iyzico_ic_check_button)
            } else {
                itemView.iyzico_cell_installment_checked_imageView.setImageResource(R.drawable.iyzico_ic_empty_radio)
            }
            //Taksit seçeneği yoksa
            if (item.installmentNumber == 1){
                itemView.iyzico_cell_installment_piece_textView.text = context.getString(R.string.iyzico_one_installment_one_shot)
                itemView.iyzico_cell_installment_piece_amount_textView.text = ""
            }

            if (adapterPosition == listSize - 1) {
                itemView.iyzico_cell_installment_seperator_view.gone()
            }
        }
    }
}
