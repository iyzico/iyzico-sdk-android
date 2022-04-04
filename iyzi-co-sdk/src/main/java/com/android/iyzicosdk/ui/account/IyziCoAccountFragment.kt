package com.android.iyzicosdk.ui.account

import IyziCoSupportFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.IyziCoBankTransferInitiaalize
import com.android.iyzicosdk.data.model.request.IyziCoPaymentCard
import com.android.iyzicosdk.data.model.request.IyziCopayWithCardRequest
import com.android.iyzicosdk.data.model.response.*
import com.android.iyzicosdk.ui.adapter.*
import com.android.iyzicosdk.ui.adapter.IyziCoBankAdapter
import com.android.iyzicosdk.ui.adapter.IyziCoBankClickListener
import com.android.iyzicosdk.ui.adapter.IyziCoCardAdapter
import com.android.iyzicosdk.ui.adapter.IyziCoInstallmentAdapter
import com.android.iyzicosdk.ui.components.edit_text.IyziCoCustomEditText
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.ui.remittance_information.IyziCoRemittanceInformationBottomSheet
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.BundleConstans.CAN_TRANSFER_AMOUNT
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.*
import com.android.iyzicosdk.util.enums.IyziCoCurrentType
import com.android.iyzicosdk.util.enums.IyziCoBottomSheetType
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.IyziCoPaymentType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.extensions.*
import com.android.iyzicosdk.util.extensions.vertical
import kotlinx.android.synthetic.main.iyzico_add_card_layout.view.*
import kotlinx.android.synthetic.main.iyzico_bottom_banks_layout.view.*
import kotlinx.android.synthetic.main.iyzico_bottom_banks_layout.view.iyzico_fragment_remittance_eft_recyclerView
import kotlinx.android.synthetic.main.iyzico_card_no_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_cell_card_item.view.*
import kotlinx.android.synthetic.main.iyzico_fragment_account.view.*
import kotlinx.android.synthetic.main.iyzico_fragment_remittance_eft.view.*
import kotlinx.android.synthetic.main.iyzico_layout_remittence_information_bottom_sheet.view.*
import kotlinx.android.synthetic.main.iyzico_my_account_sub_layout.view.*
import kotlinx.android.synthetic.main.iyzico_my_cards_layout.view.*
import kotlinx.android.synthetic.main.iyzico_my_current_balance_layout.view.*

@Keep
internal class IyziCoAccountFragment : IyziCoBaseFragment(), IyziCoBankClickListener,
    IyziCoInstallmentClickListener {

    override val layoutRes: Int = R.layout.iyzico_fragment_account
    private val cardAdapter by lazy { IyziCoCardAdapter(requireContext()) }
    private var controller = IyziCoAccountFragmentController.newInstance(this)
    private var balance = BundleConstans.ZERO_MONEY
    private var canTransferAmount = BundleConstans.ZERO_MONEY.setWalletPrice()
    private val bankAdapter by lazy { IyziCoBankAdapter(this, requireContext()) }
    private val installmentAdapter by lazy { IyziCoInstallmentAdapter(requireContext()) }
    var isFirstBalanceRequest = true
    private lateinit var selectedCard: IyziCoCardItem
    private var installmentNumber = 1
    private var iyziCoPaymentType: IyziCoPaymentType = IyziCoPaymentType.NULL
    private var referenceCode: String = ""
    var oldBalance = balance
    var selectedCardId = ""
    private var totalPrice = 0.0
    private var memberToken = ""
    private var useBalance = false
    private var force3ds = false
    private var isAmex = false
    private var rotatingStart = false

    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        controller.initUi()
    }

    fun setWebView(html: String) {
        navigate(
            IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.THREE_D, htmlCode = html),
            false
        )
        /*  navigate(
              IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.THREED_PAYMENT, html), false
          )*/
    }

    //kredi kartlarını listeleyen servistir
    fun getCards() {
        controller.getMemberCards(object :
            UIResponseCallBack<IyziCoRetrieveMemberCardsResponse>(this) {
        })
    }

    fun getBanks() {
        controller.getBanks(object : UIResponseCallBack<IyziCoBanksResponse>(this) {

        })
    }

    fun myCardsAutoFocus() {
        root.iyzico_fragment_account_my_cards_layout.autoFocus()
    }

    //kullanıcının bakiyesini listeler
    fun getRetriverBalance() {
        oldBalance = balance
        controller.getRetrieverMemberBalance(object : UIResponseCallBack<String>(this) {
            override fun onSuccess(response: String?) {
                super.onSuccess(response)
                balance = response?.convertAmount() ?: BundleConstans.ZERO_MONEY
                checkVisibilityButton()
            }
        }, isFirstBalanceRequest)
        isFirstBalanceRequest = false
    }

    fun setZeroBalance() {
        balance = BundleConstans.ZERO_MONEY

    }

    fun setBalanceToRefresh() {

        if ((balance.toDouble() - oldBalance.convertForDouble()) > 0.0) {
            rotatingStart = true
            with(root) {
                iyzico_my_current_balance_card_textview.setText(
                    (balance.toDouble() - oldBalance.convertForDouble()).toString()
                        .addTlIcon() + " " + getString(R.string.iyzico_topup_balance_loaded)
                )
                iyzico_my_current_balance_green_info_cardview.show()
                iyzico_my_current_balance_normal_info_textview.gone()
                iyzico_fragment_account_current_balance_textView.setText(
                    balance.setWalletPrice().addTlIcon()
                )
                checkVisibilityButton()

            }
            oldBalance = balance
            root.iyzico_my_current_balance_green_info_cardview.animationFadeIn {
                fadeout()
            }
        }

    }

    fun fadeout() {
        Handler().postDelayed(Runnable {
            root.iyzico_my_current_balance_green_info_cardview.animationFadeOut {
                root.iyzico_my_current_balance_green_info_cardview.gone()
                rotatingStart = false
                root.iyzico_my_current_balance_normal_info_textview.show()

            }
        }, 1500)
    }

    fun setBalance() {

        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            root.iyzico_fragment_account_current_balance_textView.text =
                balance.setWalletPrice().addTlIcon()
            balance.toDouble().apply {
                if (this <= 0.0) {
                    hideUseBalance()
                } else if (this >= IyziCoResourcesConstans.IyziCoProductPrice.toDouble()) {
                    hideUseBalance()
                } else {
                    root.iyzico_add_card_my_balance_textView.text = balance.addTlIcon()
                    root.iyzico_add_card_use_my_balance_button.apply {
                        show()
                        isClicked = true
                        setFocus()
                        setNormalBorder()
                        useBalance = true
                        setToMixPayment()
                    }
                    root.iyzico_add_card_spend_price.show()
                    if (this > IyziCoResourcesConstans.IyziCoProductPrice.toDouble()) {
                        root.iyzico_my_cards_amount_to_withdrawn_credit_card.setText(
                            BundleConstans.ZERO_MONEY.withDrawnCreditCard(getString(R.string.iyzico_transcation_will_delivery_from_card))
                        )
                    } else {
                        root.iyzico_my_cards_amount_to_withdrawn_credit_card.setText(
                            ((IyziCoResourcesConstans.IyziCoProductPrice.toDouble() - balance.toDouble())).toString()
                                .withDrawnCreditCard(getString(R.string.iyzico_transcation_will_delivery_from_card))
                        )
                    }
                }
            }
        }
    }

    private fun setToMixPayment() {
        if (useBalance) {
            when (iyziCoPaymentType) {
                IyziCoPaymentType.NEW_CARD -> {
                    iyziCoPaymentType = IyziCoPaymentType.NEW_CARD_WITH_BALANCE
                }
                IyziCoPaymentType.CARD -> {
                    iyziCoPaymentType = IyziCoPaymentType.CARD_WITH_BALANCE
                }
                IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE -> {
                    iyziCoPaymentType = IyziCoPaymentType.NEW_CARD_WITH_BALANCE
                }
                IyziCoPaymentType.CARD_WITHOUT_BALANCE -> {
                    iyziCoPaymentType = IyziCoPaymentType.CARD_WITH_BALANCE
                }
            }
        } else {
            when (iyziCoPaymentType) {
                IyziCoPaymentType.NEW_CARD -> {
                    iyziCoPaymentType = IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE
                }
                IyziCoPaymentType.CARD -> {
                    iyziCoPaymentType = IyziCoPaymentType.CARD_WITHOUT_BALANCE
                }
                IyziCoPaymentType.NEW_CARD_WITH_BALANCE -> {
                    iyziCoPaymentType = IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE
                }
                IyziCoPaymentType.CARD_WITH_BALANCE -> {
                    iyziCoPaymentType = IyziCoPaymentType.CARD_WITHOUT_BALANCE
                }
            }
        }
    }

    fun hideUseBalance() {
        root.iyzico_fragment_Account_my_installment_information_layout.show()
        root.iyzico_add_card_use_my_balance_button.gone()
        useBalance = false
        setToMixPayment()
        root.iyzico_add_card_use_my_balance_button.isClicked = false
    }

    fun unfocusListener() {
        with(root) {
            iyzico_add_card_name.unfocusListener {
                if (iyzico_add_card_name.text.isEmpty()) {
                    iyzico_add_card_name.errorBorder(getString(R.string.iyzico_compulsory))
                }
            }
            iyzico_add_card_card_number.unfocusListener {
                if ((it as IyziCoCustomEditText).text.toString().isEmpty()) {
                    iyzico_add_card_card_number.errorBorder(getString(R.string.iyzico_compulsory))
                } else if ((it).text.toString().invalidCardNumber(isAmex))
                    iyzico_add_card_card_number.errorBorder(getString(R.string.iyzico_wrong))
            }
            iyzico_add_card_date.unfocusListener {
                if (iyzico_add_card_cvc_number.text != "") {
                    hideKeyboard()
                }
                if ((it as IyziCoCustomEditText).text.toString().isEmpty()) {
                    iyzico_add_card_date.errorBorder(getString(R.string.iyzico_compulsory))
                } else if (it.text.toString()
                        .invalidDexpirationDate(getMonth(), getYear())
                ) {
                    iyzico_add_card_date.errorBorder(getString(R.string.iyzico_wrong))
                }
            }
            iyzico_add_card_cvc_number.unfocusListener {
                hideKeyboard()
                if ((it as IyziCoCustomEditText).text.toString().isEmpty())
                    iyzico_add_card_cvc_number.errorBorder(getString(R.string.iyzico_compulsory))
                else if (it.text.toString().invalidCvc(isAmex))
                    iyzico_add_card_cvc_number.errorBorder(getString(R.string.iyzico_wrong))
            }


            iyzico_add_card_name.textListener {
                if (it.isNotEmpty())
                    detectAllItemsAreValid()
            }
            iyzico_add_card_card_number.textListener {
                if (it.isNotEmpty())
                    detectAllItemsAreValid()
            }
            iyzico_add_card_cvc_number.textListener {
                if (it.isNotEmpty())
                    detectAllItemsAreValid()

                if (it.length == 3) {
                    val name = root.iyzico_add_card_name.text
                    val number = root.iyzico_add_card_card_number.text.replace(" ", "")
                    val cvv = root.iyzico_add_card_cvc_number.text
                    val month = root.iyzico_add_card_date.text.take(2)
                    val year = root.iyzico_add_card_date.text.takeLast(2)

                    controller.pwiInquireWithNewCard(
                        "",
                        IyziCoCurrentType.TRY.type,
                        IyziCoConfig.LANGUAGE.type,
                        paidPrice().toDouble(),
                        name,
                        number,
                        cvv,
                        month,
                        year
                    )
                }

            }
            iyzico_add_card_date.textListener {
                if (it.isNotEmpty())
                    detectAllItemsAreValid()
            }
        }
    }

    private fun detectAllItemsAreValid() {
        if (allItemIsValid(false)) {
            showInstallmentContainer()
        } else {
            if (root.iyzico_add_card_card_number.text.length < 7)
                showInstallmentInfo()
        }
    }

    private fun allItemIsValid(withWarning: Boolean = true): Boolean {
        var isvalid = true
        if (root.iyzico_add_card_name.text.isEmpty()) {
            if (withWarning)
                root.iyzico_add_card_name.errorBorder(getString(R.string.iyzico_compulsory))
            isvalid = false
        }
        if (root.iyzico_add_card_card_number.text.isEmpty()) {
            if (withWarning)
                root.iyzico_add_card_card_number.errorBorder(getString(R.string.iyzico_compulsory))
            isvalid = false
        } else if (root.iyzico_add_card_card_number.text.invalidCardNumber(isAmex)) {
            if (withWarning)
                root.iyzico_add_card_card_number.errorBorder(getString(R.string.iyzico_wrong))
            isvalid = false
        }

        if (root.iyzico_add_card_date.text.isEmpty()) {
            if (root.iyzico_add_card_date.text.isEmpty()) {
                if (withWarning)
                    root.iyzico_add_card_date.errorBorder(getString(R.string.iyzico_compulsory))
                isvalid = false
            } else if (root.iyzico_add_card_date.text.invalidDexpirationDate(getMonth(), getYear())
            ) {
                if (withWarning)
                    root.iyzico_add_card_date.errorBorder(getString(R.string.iyzico_wrong))
                isvalid = false
            }
        }
        if (root.iyzico_add_card_cvc_number.text.invalidCvc(isAmex)) {
            if (withWarning)
                root.iyzico_add_card_cvc_number.errorBorder(getString(R.string.iyzico_compulsory))
            isvalid = false
        }
        return isvalid
    }

    override fun initUIPayWithIyziCo() {
        super.initUIPayWithIyziCo()
        root.iyzico_add_card_use_my_balance_button.setToBalance()
        root.iyzico_fragment_remittance_transfer_textView.gone()
        showStickLayout()
        if (root.iyzico_add_card_use_my_balance_button.isClicked)
            root.iyzico_add_card_use_my_balance_button.setNormalBorder()
        setToMixPayment()
        root.iyzico_fragment_account_continue_button.clickListener {

            if (isNewCard() && allItemIsValid()) {
                var input = root.iyzico_add_card_date.text.split("/")
                controller.setPayment(
                    iyziCoPaymentType,
                    force3ds,
                    balance.toDouble(),
                    IyziCoLoginChannelType.THIRD_PARTY_APP,
                    selectedCardId,
                    root.iyzico_add_card_name.text,
                    (root.iyzico_add_card_card_number.text.clearSpace()),
                    root.iyzico_add_card_cvc_number.text,
                    input[0] ?: "",
                    input[1],
                    0,
                    true,
                    true,
                    installmentNumber,
                    memberToken,
                    paidPrice(),
                    false
                )
            } else if (!isNewCard() && root.iyzico_fragment_account_continue_button.buttonEnabled) {
                controller.setPayment(
                    iyziCoPaymentType,
                    force3ds,
                    balance.toDouble(),
                    IyziCoLoginChannelType.THIRD_PARTY_APP,
                    selectedCardId,
                    "", "",
                    "",
                    "",
                    "",
                    0,
                    true,
                    true,
                    installmentNumber,
                    memberToken,
                    paidPrice(),
                    false
                )
            }
        }
        root.iyzico_fragment_account_my_account_constraint.setOnClickListener {
            selectedMyAccountPaymentType()
            checkVisibilityButton()
        }
    }

    override fun initUITopup() {
        super.initUITopup()
        unSelectedPaymentType()
        getBanks()
        root.iyzico_fragment_amount_to_load_layout.show()
        root.iyzico_fragment_account_my_account.gone()
        root.iyzico_fragment_amount_to_load_line_view.show()
        hideUseBalance()
        arguments?.let {
            canTransferAmount = arguments!!.getString(CAN_TRANSFER_AMOUNT).toString()
        }
        root.iyzico_fragment_acount_transfer_amount_textView.text =
            canTransferAmount.addTlIcon()
        root.iyzico_fragment_account_continue_button.clickListener {
            if (isNewCard()) {
                if (allItemIsValid()) {
                    depositsWithNewCard()
                } else {
                    showSDKError(getString(R.string.iyzico_please_check_card_datas))
                }
            } else {
                controller.createDepositRegisteredCard(
                    getIyziCoActivity()?.initialRequestId ?: "",
                    selectedCard.cardToken,
                    canTransferAmount,
                    IyziCoCurrentType.TRY.type,
                    IyziCoConfig.CLIENT_IP,
                    IyziCoLoginChannelType.THIRD_PARTY_APP.type,
                    object :
                        UIResponseCallBack<IyziCoCreateDepositCardResponse>(this) {
                        override fun onSuccess(data: IyziCoCreateDepositCardResponse?) {
                            data?.balanceAmount?.let { it -> showIyziCoBalance(it) }
                            startInfoPage(data?.depositStatus ?: "", canTransferAmount)

                        }
                    }
                )
            }
        }
    }

    fun startInfoPage(depositStatus: String, balanceAmount: String) {
        when (depositStatus) {
            IyziCoDepositStatus.WAITING_FOR_PROVISION.toString() -> {
                navigate(
                    IyziCoInfoFragment.newInstance(
                        IyziCoInfoScreenType.TOPUP_WAİT,
                        balanceAmount.convertAmount()
                    )
                )
            }
            IyziCoDepositStatus.APPROVED.toString() -> {
                navigate(
                    IyziCoInfoFragment.newInstance(
                        IyziCoInfoScreenType.TRANSFER_CONFIRMATION_TO_TOPUP,
                        balanceAmount.convertAmount()
                    )
                )

            }
            else -> {
                navigate(
                    IyziCoInfoFragment.newInstance(
                        IyziCoInfoScreenType.ERROR,
                        balanceAmount.convertAmount()
                    )
                )
            }
        }
    }

    private fun depositsWithNewCard() {
        controller.createDepositWithNewCard(
            canTransferAmount,
            root.iyzico_add_card_name.text,
            root.iyzico_add_card_card_number.text.replace(" ", ""),
            IyziCoLoginChannelType.THIRD_PARTY_APP.type,
            IyziCoConfig.CLIENT_IP,
            IyziCoCurrentType.TRY.type,
            root.iyzico_add_card_cvc_number.text,
            root.iyzico_add_card_date.text.take(2),
            root.iyzico_add_card_date.text.takeLast(2),
            getIyziCoActivity()?.initialRequestId ?: "",
            object :
                UIResponseCallBack<IyziCoCreateDepositCardResponse>(this) {
                override fun onSuccess(response: IyziCoCreateDepositCardResponse?) {
                    super.onSuccess(response)
                    startInfoPage(response?.depositStatus ?: "", canTransferAmount)
                }
            }
        )
    }

    override fun clickListener() {
        super.clickListener()

        root.iyzico_my_account_sub_layout_info_textview.spannableExtension(
            9,
            30,
            R.color.iyzico_black,
            clickSpan = {
            })

        root.iyzico_my_account_sub_layout_reset_icn.setOnClickListener {

            root.iyzico_my_account_sub_layout_reset_icn.rotateAnimation(finishHandler = {
                getRetriverBalance()
            })
        }
        root.iyzico_fragment_account_new_card_double_border.clickListener { view, b ->
            root.iyzico_fragment_account_new_card_double_border.apply {
                focus()
                isClicked != isClicked
            }

            showNewCardContainer()
        }
        root.iyzico_add_card_use_my_balance_button.clickListener { view, b ->
            if (b) {
                useBalance = true
                hideInstallmentContainer()
                root.iyzico_add_card_use_my_balance_button.setNormalBorder()
                root.iyzico_add_card_spend_price.show()
                root.iyzico_fragment_Account_my_installment_information_layout.gone()
                root.iyzico_my_cards_amount_to_withdrawn_credit_card.setText(
                    ((IyziCoResourcesConstans.IyziCoProductPrice.toDouble() - balance.toDouble())).toString()
                        .withDrawnCreditCard(getString(R.string.iyzico_transcation_will_delivery_from_card))
                )
                setToMixPayment()
            } else {
                useBalance = false
                if (isNewCard()) {
                    root.iyzico_fragment_Account_my_installment_options_layout.gone()
                    root.iyzico_fragment_Account_my_installment_information_layout.show()
                }
                unSelectMyBalance()
                if ((!isNewCard())) {
                    getInstallments(selectedCard.binNumber, IyziCoInstallmentType.NORMAL)
                    setToMixPayment()
                }

            }
        }
        root.iyzico_fragment_account_my_account.expandableOpened {
            hideKeyboard()
            setPwiPrice(IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString())
            totalPrice = IyziCoResourcesConstans.IYZICO_PAID_PRICE
            root.iyzico_fragment_account_my_cards_layout.closeOtherItem()
            root.iyzico_fragment_account_eft_expandable.closeOtherItem()
            unSelectedPaymentType()
            selectedMyAccountPaymentType()
            checkVisibilityButton()

            if (!it) {
                iyziCoPaymentType = IyziCoPaymentType.NULL
                checkVisibilityButton()
            }
        }
        root.iyzico_fragment_account_my_cards_layout.expandableOpened {
            hideKeyboard()
            root.iyzico_fragment_account_my_account.closeOtherItem()
            root.iyzico_fragment_account_eft_expandable.closeOtherItem()
            setFirstCard()
            setBalance()
            checkVisibilityButton()

            if (!it) {
                iyziCoPaymentType = IyziCoPaymentType.NULL
                checkVisibilityButton()
            }
        }
        root.iyzico_fragment_account_eft_expandable.expandableOpened {
            hideKeyboard()
            setPwiPrice(IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString())
            totalPrice = IyziCoResourcesConstans.IYZICO_PAID_PRICE
            root.iyzico_fragment_account_my_cards_layout.closeOtherItem()
            root.iyzico_fragment_account_my_account.closeOtherItem()
            iyziCoPaymentType = IyziCoPaymentType.NULL
            checkVisibilityButton()
        }
    }

    fun selectMyBalance() {
        root.iyzico_add_card_spend_price.show()
        if (!allItemIsValid(false) && isNewCard())
            root.iyzico_fragment_Account_my_installment_information_layout.gone()
        root.iyzico_fragment_Account_my_installment_options_layout.gone()
        root.iyzico_add_card_use_my_balance_button.setNormalBorder()
    }

    fun unSelectMyBalance() {
        root.iyzico_add_card_spend_price.gone()
        if (isNewCard() && !allItemIsValid(false)) {
            root.iyzico_fragment_Account_my_installment_options_layout.gone()
            if (root.iyzico_fragment_account_new_card_double_border.isClicked && root.iyzico_add_card_card_number.text.length < 7 && IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {

            } else if (root.iyzico_fragment_account_new_card_double_border.isClicked)
                root.iyzico_fragment_Account_my_installment_options_layout.show()
        } else {

            root.iyzico_fragment_Account_my_installment_options_layout.show()
            root.iyzico_fragment_Account_my_installment_information_layout.gone()
        }
        root.iyzico_add_card_use_my_balance_button.setNormalBorder()
    }

    fun checkMyAccount() {
        if (balance.toDouble() >= IyziCoResourcesConstans.IyziCoProductPrice.toDouble()) {
            root.iyzico_fragment_account_my_account.autoFocus()
            selectedMyAccountPaymentType()
        } else {
            myCardsAutoFocus()
        }
    }

    private fun isNewCard(): Boolean {
        return (iyziCoPaymentType === IyziCoPaymentType.NEW_CARD || iyziCoPaymentType === IyziCoPaymentType.NEW_CARD_WITH_BALANCE || iyziCoPaymentType === IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE)
    }

    private fun isCreditCard(): Boolean {
        return (iyziCoPaymentType === IyziCoPaymentType.CARD || iyziCoPaymentType === IyziCoPaymentType.CARD_WITH_BALANCE || iyziCoPaymentType === IyziCoPaymentType.CARD_WITHOUT_BALANCE)
    }

    private fun selectedMyAccountPaymentType() {
        root.fragment_account_my_account_check_imageView.setImageResource(R.drawable.iyzico_ic_check_button)
        unSelectedCreditCard()
        unSelectedNewCardButton()
        iyziCoPaymentType = IyziCoPaymentType.BALANCE
    }

    private fun unSelectedMyAccountPaymentType() {
        iyziCoPaymentType = IyziCoPaymentType.NULL
        root.fragment_account_my_account_check_imageView.setImageResource(R.drawable.iyzico_ic_empty_radio)
    }

    private fun unSelectedCreditCard() {
        cardAdapter.items.forEach { entity ->
            entity.isSelected = false
        }
        cardAdapter.notifyDataSetChanged()
    }

    fun unSelectedPaymentType() {
        unSelectedMyAccountPaymentType()
        unSelectedCreditCard()
        checkVisibilityButton()
    }

    fun setBankAdapter(data: List<IyziCoBankItem>, referenceCode: String) {
        root.iyzico_fragment_remittance_eft_recyclerView.vertical(bankAdapter)
        hideKeyboard()
        this.referenceCode = referenceCode
        bankAdapter.items = data
        bankAdapter.onItemClick {
            initEftInformationScreen()
        }
    }

    fun setInstallmentAdapter(data: List<IyziCoInstallmentPrice>) {
        root.iyzico_fragment_Account_my_installment_options_recyclerview.vertical(
            installmentAdapter
        )
        data[0].isChecked = true
        totalPrice = data[0].totalPrice
        installmentAdapter.items = data
        setPwiPrice(totalPrice.toString())
        if (iyziCoPaymentType == IyziCoPaymentType.CARD_WITHOUT_BALANCE || iyziCoPaymentType == IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE) {
            root.iyzico_fragment_Account_my_installment_information_layout.gone()
            root.iyzico_fragment_Account_my_installment_options_layout.show()
            installmentAdapter.onItemClick { clickItem ->

                installmentAdapter.items.forEach { entity ->
                    if (clickItem != entity) {
                        entity.isChecked = false
                    } else {
                        totalPrice = clickItem.totalPrice
                        setPwiPrice(totalPrice.toString())
                        installmentNumber = clickItem.installmentNumber
                        entity.isChecked = true
                    }
                }
                installmentAdapter.notifyDataSetChanged()
            }
        }
    }

    fun setCardAdapter(list: List<IyziCoCardItem>) {
        root.iyzico_fragment_account_reyclerview.vertical(cardAdapter)
        cardAdapter.items = list
        if (!cardAdapter.items.isNullOrEmpty()) {
            selectedCardId = cardAdapter.items[0].cardToken

        }

        cardAdapter.onItemClick { clickItem ->
            unSelectedMyAccountPaymentType()
            hideNewCardContainer()
            hideKeyboard()

            getInstallments(clickItem.binNumber, IyziCoInstallmentType.NORMAL)
            showInstallmentContainer()

            root.iyzico_fragment_Account_my_installment_options_layout.gone()

            cardAdapter.items.forEach { entity ->
                if (clickItem != entity) {
                    entity.isSelected = false
                } else {
                    selectedCard = clickItem
                    entity.isSelected = true
                    selectedCardId = entity.cardToken

                    iyziCoPaymentType = if (entity.isSelected) {
                        IyziCoPaymentType.CARD
                    } else {
                        IyziCoPaymentType.NULL
                    }
                }
                setToMixPayment()

            }
            cardAdapter.notifyDataSetChanged()
            checkVisibilityButton()

            getInquireService()

        }
    }


    private fun getInquireService() {
        cardAdapter.items.find {
            it.isSelected
        }?.let {
            controller.pwiInquire(
                "",
                IyziCoCurrentType.TRY.type,
                IyziCoConfig.LANGUAGE.type,
                paidPrice().toDouble(),
                it.cardToken,
                IyziCoLoginChannelType.THIRD_PARTY_APP.type,
                object : UIResponseCallBack<IyziCoPWIResponse>(this) {
                    override fun onSuccess(response: IyziCoPWIResponse?) {
                        super.onSuccess(response)
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        super.onError(errorCode, errorMessage)
                        it.bonusPointAmount = 103.5
                        cardAdapter.notifyDataSetChanged()
                    }
                }
            )
        }


    }

    fun setForce3Ds(value: Boolean) {
        force3ds = value
    }


    @SuppressLint("SetTextI18n")
    fun setPlusInstallment(plusInstallmentResponseList: List<IyziCoPlusInstallmentResponse>) {
        with(root) {
            if (plusInstallmentResponseList.isNotEmpty()) {
                iyzico_fragment_account_installment_container.show()
                var text = ""
                when (plusInstallmentResponseList.size) {
                    1 -> {
                        val singleCampaign = plusInstallmentResponseList.first()
                        text =
                            "${singleCampaign.cardBankDtoList.first().cardBankName} ${getString(R.string.iyzico_plus_installment_info_text)} +${singleCampaign.plusInstallment} Taksit"
                    }
                    2 -> {
                        val doubleCampaign = plusInstallmentResponseList.take(2)
                        text =
                            "${doubleCampaign[0].cardBankDtoList.first().cardBankName} veya ${doubleCampaign[1].cardBankDtoList.first().cardBankName} " +
                                    "${getString(R.string.iyzico_plus_installment_info_text)} +${doubleCampaign[0].plusInstallment} +${doubleCampaign[1].plusInstallment}  Taksit"
                    }
                }

                iyzico_fragment_account_installment_textview.text = text

                val startIndex = text.indexOf("vade")


                iyzico_fragment_account_installment_textview.spannableExtension(
                    startIndex,
                    text.length - 1,
                    R.color.iyzico_dark_green,
                    clickSpan = {}
                )

            } else {
                iyzico_fragment_account_installment_container.gone()
            }
        }

    }

    private fun showInstallmentContainer() {
        root.iyzico_fragment_Account_my_installment_information_layout.gone()
        //root.iyzico_add_card_use_my_balance_button.focus()
    }

    private fun hideInstallmentContainer() {
        root.iyzico_fragment_Account_my_installment_options_layout.gone()
        setPwiPrice(IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString())
        installmentNumber = 1
    }

    private fun showInstallmentInfo() {
        // root.iyzico_fragment_Account_my_installment_information_layout.show()
        root.iyzico_fragment_Account_my_installment_options_layout.gone()
    }

    fun setFirstCard() {
        unSelectedCreditCard()
        unSelectedNewCardButton()
        cardAdapter.items.getOrNull(0)?.let {
            it.isSelected = true
            cardAdapter.notifyDataSetChanged()
            iyziCoPaymentType = IyziCoPaymentType.CARD
            setToMixPayment()

            getInstallments(it.binNumber, IyziCoInstallmentType.NORMAL)
            showInstallmentContainer()
            selectedCard = it

            getInquireService()

        } ?: run {
            showNewCardContainer()
            root.iyzico_fragment_account_new_card_double_border.focus()
            root.iyzico_fragment_account_new_card_double_border.clearBorderFocus()
            root.iyzico_fragment_account_new_card_double_border.isClicked = false
            //root.iyzico_fragment_Account_my_installment_information_layout.show()

            iyziCoPaymentType = IyziCoPaymentType.NEW_CARD
            setToMixPayment()

        }
        checkVisibilityButton()
    }

    private fun showNewCardContainer() {
        root.iyzico_fragment_account_new_card_top_linear_layout.setBackgroundResource(R.drawable.iyzico_border_new_card_cardview_radius)
        root.iyzico_add_card_layout_linear_layout.show()
        root.iyzico_new_card_information_text.show()
        unSelectedPaymentType()
        iyziCoPaymentType = IyziCoPaymentType.NEW_CARD
        setToMixPayment()
        root.iyzico_fragment_account_continue_button.buttonEnabled()
        root.iyzico_fragment_account_new_card_double_border.clearBorderFocus()
        showInstallmentInfo()
        if (iyziCoPaymentType == IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE && IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            root.iyzico_fragment_Account_my_installment_information_layout.show()
        }
        setPwiPrice(IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString())
        totalPrice = IyziCoResourcesConstans.IYZICO_PAID_PRICE
    }

    fun hideNewCardContainer() {
        hideKeyboard()
        clearEdittextsFocus()
        root.iyzico_fragment_account_new_card_top_linear_layout.setBackgroundResource(R.color.iyzico_f1_grey_color)
        root.iyzico_add_card_layout_linear_layout.gone()
        root.iyzico_new_card_information_text.gone()
        iyziCoPaymentType = IyziCoPaymentType.NULL
        root.iyzico_fragment_account_new_card_double_border.unFocus()
        root.iyzico_fragment_account_new_card_double_border.setNormalBorder()
        //root.iyzico_fragment_add_new_card_button.setPadding(0, 60, 0, 60)
        root.iyzico_fragment_Account_my_installment_information_layout.gone()
    }

    private fun checkVisibilityButton() {
        root.iyzico_fragment_account_continue_button.show()
        when (iyziCoPaymentType) {
            IyziCoPaymentType.BALANCE -> {
                if (balance.toDouble() < IyziCoResourcesConstans.IyziCoProductPrice.toDouble()
                ) {
                    root.iyzico_fragment_account_continue_button.buttonDisable()
                    root.fragment_account_my_account_check_imageView.gone()
                } else {
                    root.fragment_account_my_account_check_imageView.show()
                    root.iyzico_fragment_account_continue_button.buttonEnabled()

                }
            }
            IyziCoPaymentType.NULL -> {
                root.iyzico_fragment_account_continue_button.buttonDisable()
            }
            else -> {
                root.iyzico_fragment_account_continue_button.buttonEnabled()
            }
        }
    }

    private fun unSelectedNewCardButton() {
        root.iyzico_fragment_account_new_card_double_border.unFocus()
        hideNewCardContainer()
    }

    private fun initEftInformationScreen() {
        root.iyzico_fragment_remittance_eft_banks_linearLayout.gone()
        root.iyzico_fragment_remittance_eft_remittance_information_linearLayout.show()

    }

    override fun clickBank(itemCount: Int, item: IyziCoBankItem) {
        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            controller.bankTransferInitialize(
                item.bankId.toInt(),
                item.iban,
                item.legalCompanyTitle,
                item.bankName,
                item.bankLogoUrl
            )
        } else {
            showRemittanceInformationBottomSheet(
                item.iban,
                item.legalCompanyTitle,
                item.bankName,
                referenceCode,
                item.bankLogoUrl
            )
        }

    }

    fun showRemittanceInformationBottomSheet(
        iban: String,
        title: String,
        bankName: String,
        reference: String = referenceCode,
        bankLogo: String?
    ) {
        IyziCoRemittanceInformationBottomSheet.show(
            getIyziCoActivity()?.supportFragmentManager,
            IyziCoBottomSheetType.TRANSFER_TOP_UP,
            iban,
            title, reference,
            bankName,
            bankLogo = bankLogo ?: ""
        )
    }

    fun showRemittanceInformationBottomSheetForPwi(
        iban: String,
        title: String,
        bankName: String,
        reference: String = referenceCode,
        bankTransferId: String,
        bankLogo: String
    ) {
        IyziCoRemittanceInformationBottomSheet.show(
            getIyziCoActivity()?.supportFragmentManager,
            IyziCoBottomSheetType.TRANSFER_DETAIL,
            iban,
            title, reference,
            bankName,
            bankTransferId,
            bankLogo
        )
    }

    private fun clearEdittextsFocus() {
        with(root) {
            iyzico_add_card_card_number.clearText()
            iyzico_add_card_date.clearText()
            iyzico_add_card_cvc_number.clearText()
            iyzico_add_card_name.clearText()
        }
    }

    fun pwiRetrive() {
        controller.pwiRetrive(
            getIyziCoActivity()?.initialRequestId ?: "",
            IyziCoConfig.LANGUAGE.type,
            object : UIResponseCallBack<IyziCoPWIRetriveResponse>(this) {
                override fun onSuccess(response: IyziCoPWIRetriveResponse?) {
                    super.onSuccess(response)
                    memberToken = response?.memberToken ?: ""
                }
            })
    }

    fun getInstallments(binNumber: String?, type: IyziCoInstallmentType) {
        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            controller.getInstallments(
                binNumber!!,
                IyziCoConfig.LANGUAGE.type,
                IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString(),
                type
            )
        }
    }

    fun checkItToAmex(numbers: String) {
        if ((numbers.clearSpace()).isAmex() == IyziCoCardTypes.AMEX) {
            isAmex = true
            root.iyzico_add_card_cvc_number.setEditTextMaxLength(4)
            root.iyzico_add_card_card_number.setAmex(numbers)
        } else {
            root.iyzico_add_card_cvc_number.setEditTextMaxLength(3)
            root.iyzico_add_card_cvc_number.clearText()
            root.iyzico_add_card_cvc_number.clearBorder()
            isAmex = false
        }
    }

    fun setNameEdittext() {
        root.iyzico_add_card_name.setIsUpper()
    }

    fun cardNumberTextWatcher() {

        root.iyzico_add_card_card_number.iyzico_card_no_edit_text.addTextChangedListener(object :
            TextWatcher {
            var mToggle = false
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mToggle) {
                    if (p0?.length ?: 0 >= 7) {
                        checkItToAmex(p0?.substring(0, 7) ?: "")
                    }
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                        if (p0.toString().length == 7) {
                            getInstallments(
                                p0.toString().cardtobinNumber(),
                                IyziCoInstallmentType.NEW_CARD
                            )
                            if (useBalance == false) {
                                root.iyzico_fragment_Account_my_installment_options_layout.show()
                                root.iyzico_add_card_card_number.iyzico_card_no_edit_text.requestFocus()
                                root.iyzico_fragment_Account_my_installment_information_layout.gone()
                            }


                        } else if (p0.toString().length < 7) {

                            if (useBalance == false) {
                                root.iyzico_fragment_Account_my_installment_options_layout.gone()
                                root.iyzico_fragment_Account_my_installment_information_layout.show()
                            }
                        }
                    }


                }
                mToggle = !mToggle
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    fun paidPrice(): String {
        if (installmentNumber == 1) {
            return IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString()
        } else {
            return totalPrice.toString()
        }
    }

    companion object {
        fun newInstance() = IyziCoAccountFragment()
    }

    override fun clickInstallment(itemCount: Int, item: IyziCoInstallmentDetail) {

    }
}