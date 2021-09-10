package com.android.iyzicosdk.ui.settlement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.extensions.addTlIcon
import com.android.iyzicosdk.util.extensions.convertAmount
import kotlinx.android.synthetic.main.iyzico_fragment_settlement.view.*
@Keep
internal class IyziCoSettlementFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_settlement
    private var controller=IyziCoSettlementFragmnetController.newInstance(this)

    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)

        root.iyzico_fragment_settlement_balance_textView.setText(IyziCoResourcesConstans.IyziCoWalletPrice.toString().addTlIcon())
        clearStack()
        getRetriverBalance()
    }

    override fun clickListener() {
        super.clickListener()
        root.iyzico_fragment_settlement_transfer_button.clickListener {
            navigate(
                IyziCoInfoFragment.newInstance(
                    IyziCoInfoScreenType.SETTLEMENT_SUCCESS
                )
            )
        }
    }

    companion object {
        fun newInstance() = IyziCoSettlementFragment()
    }
    private fun getRetriverBalance() {
        controller.getRetrieverMemberBalance(object : UIResponseCallBack<String>(this) {
            override fun onSuccess(response: String?) {
                super.onSuccess(response)
                showIyziCoBalance(response?.convertAmount() ?: "0.00")
            }
        })
    }
}