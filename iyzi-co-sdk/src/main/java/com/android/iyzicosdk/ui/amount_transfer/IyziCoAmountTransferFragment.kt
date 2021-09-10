import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoAmountResponse
import com.android.iyzicosdk.ui.account.IyziCoAccountFragment
import com.android.iyzicosdk.ui.amount_transfer.IyziCoAmountFragmentController
import com.android.iyzicosdk.ui.components.buttons.IyziCoCustomButton
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.BundleConstans.CAN_TRANSFER_AMOUNT
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoButtonType
import com.android.iyzicosdk.util.enums.IyziCoCurrentType
import com.android.iyzicosdk.util.extensions.*
import kotlinx.android.synthetic.main.iyzico_can_transfer_amount_layout.view.*
import kotlinx.android.synthetic.main.iyzico_fragment_amount_transfer.view.*
import kotlinx.android.synthetic.main.iyzico_select_price_layout.view.*

@Keep
internal class IyziCoAmountTransferFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_amount_transfer
    private var controller = IyziCoAmountFragmentController.newInstance(this)
    private var beforeDot = ""
    private var afterDot = ""
    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        clearStack()
        changeListener()
        root.iyzico_fragment_amount_price_editText.autoFocus()
    }

    fun clearBorder() {
        with(root) {
            iyzico_select_price_button_one.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
            iyzico_select_price_button_two.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
            iyzico_select_price_button_three.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
            root.iyzico_select_price_button_four.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
        }
    }

    fun autoFillButtons(view: IyziCoCustomButton) {
        if (view.type != IyziCoButtonType.BLUE_FILL) {
            root.iyzico_fragment_amount_price_editText.setText(
                view.text.substring(
                    1,
                    view.text.length
                ).defaultButton()
            )
            clearBorder()
            view.setButton(IyziCoButtonType.BLUE_FILL)
        } else {
            view.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
        }
    }

    override fun clickListener() {
        super.clickListener()
        root.iyzico_select_price_button_one.clickListener {
            autoFillButtons(it)
        }
        root.iyzico_select_price_button_two.clickListener {
            autoFillButtons(it)
        }
        root.iyzico_select_price_button_three.clickListener {
            autoFillButtons(it)
        }
        root.iyzico_select_price_button_four.clickListener {
            autoFillButtons(it)
        }
    }

    private fun changeListener() {
        root.iyzico_fragment_amount_price_editText.firstEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().equals(beforeDot)) {
                    root.iyzico_can_transfer_amount_transfer_all_balance.isClicked = false
                    root.iyzico_can_transfer_amount_transfer_all_balance.unFocus()
                }
            }

        })
        root.iyzico_fragment_amount_price_editText.secondEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().equals(afterDot)) {
                    root.iyzico_can_transfer_amount_transfer_all_balance.isClicked = false
                    root.iyzico_can_transfer_amount_transfer_all_balance.unFocus()
                }
            }
        })
    }

    override fun initUICashOut() {
        super.initUICashOut()
        hideBackButton()
        showCloseButton()
        root.iyzico_fragment_amount_transfer_topup_container.show()
        root.iyzico_can_transfer_amount_max_price.setText(
            IyziCoResourcesConstans.IyziCoWalletPrice.addTlIcon()
        )
        beforeDot = root.iyzico_can_transfer_amount_max_price.text.toString().substringBefore(",")
            .replace("₺", "").replace(" ", "")
        afterDot = root.iyzico_can_transfer_amount_max_price.text.toString().substringAfter(",")
        getRetrieverMemberBalance()
        root.iyzico_can_transfer_amount_transfer_all_balance.setToAmount()
        root.iyzico_fragment_amount_transfer_contunie_button.clickListener {
            if (!root.iyzico_fragment_amount_price_editText.getPrice().isInvalidPrice()) {
                showSDKError(getString(R.string.iyzico_amount_transfer_null_price_warning))
            } else {
                var amount = root.iyzico_fragment_amount_price_editText.getPrice()

                controller.amountBalanceComplete(
                    amount,
                    IyziCoCurrentType.TRY.type,
                    getIyziCoActivity()?.initialRequestId ?: "",
                    object :
                        UIResponseCallBack<IyziCoAmountResponse>(this) {
                        override fun onSuccess(response: IyziCoAmountResponse?) {
                            super.onSuccess(response)

                        }
                    })
            }
        }
        root.iyzico_can_transfer_amount_transfer_all_balance.clickListener { view, b ->
            if (b) {
                root.iyzico_fragment_amount_price_editText.setText(
                    IyziCoResourcesConstans.IyziCoWalletPrice.addTlIcon()
                )
            } else {
                root.iyzico_fragment_amount_price_editText.setText(getString(R.string.iyzico_init_amount_value))
            }
            root.iyzico_can_transfer_amount_transfer_all_balance.clearBorderFocusforCashout()
        }
    }

    fun getRetrieverMemberBalance() {
        controller.getRetrieverMemberBalance(object : UIResponseCallBack<String>(this) {
            override fun onSuccess(response: String?) {
                super.onSuccess(response)
                showIyziCoBalance(response?.convertAmount() ?:BundleConstans.ZERO_MONEY)
            }
        })
    }

    override fun initUITopup() {
        super.initUITopup()
        root.iyzico_select_price_contain.show()
        root.iyzico_can_transfer_amount.gone()
        getRetrieverMemberBalance()
        root.iyzico_fragment_amount_transfer_contunie_button.textView.setText(getText(R.string.iyzico_transfer_money))
        root.iyzico_fragment_amount_transfer_contunie_button.clickListener {
            if (!root.iyzico_fragment_amount_price_editText.getPrice().isInvalidPrice()) {
                showSDKError(getString(R.string.iyzico_amount_transfer_null_price_warning))
            } else {
                var bundle = Bundle()
                bundle.putString(
                    CAN_TRANSFER_AMOUNT,
                    root.iyzico_fragment_amount_price_editText.getPrice()
                )
                val fragment = IyziCoAccountFragment.newInstance()
                fragment.arguments = bundle
                clearStack()
                navigate(fragment, false)
            }
        }
        root.iyzico_fragment_amount_transfer_all_textView.apply {
            gone()
        }
    }

    fun errorEmptyAmount() {
        showSDKError(getString(R.string.iyzico_NotEmpty))
    }

    companion object {
        fun newInstance() = IyziCoAmountTransferFragment()
    }
}
