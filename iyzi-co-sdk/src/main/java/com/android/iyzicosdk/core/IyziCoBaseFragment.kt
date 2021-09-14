package com.android.iyzicosdk.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.android.iyzicosdk.R
import com.android.iyzicosdk.callback.IyzicoCallback
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoPopupType
import com.android.iyzicosdk.util.enums.ResultCode
import com.android.iyzicosdk.util.extensions.setMargins
import kotlinx.android.synthetic.main.iyzico_payw_sticky.*


internal abstract class IyziCoBaseFragment : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    lateinit var root: View

    private lateinit var activity: IyziCoActivity

    private val callback: IyzicoCallback? get() = activity.callback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is IyziCoActivity) {
            this.activity = context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!this::root.isInitialized) {
            root = inflater.inflate(layoutRes, container, false)
            initUI(inflater, container, savedInstanceState)
            /**
             * Gelen tipe göre SDK'da ekranları değiştirmek için kullanılır
             */
            when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                    initUIPayWithIyziCo()
                }
                IyziCoSDKType.REFUND -> {
                    initUIRefund()
                }
                IyziCoSDKType.SETTLEMENT -> {
                    initUISettlement()
                }
                IyziCoSDKType.CASH_OUT -> {
                    initUICashOut()
                }
                IyziCoSDKType.TOPUP -> {
                    initUITopup()
                }
            }

        } else {
            reCreate(inflater, container, savedInstanceState)
            when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                    reCreatePayWithIyziCo()
                }
                IyziCoSDKType.REFUND -> {
                    reCreateRefund()
                }
                IyziCoSDKType.SETTLEMENT -> {
                    reCreateSettlement()
                }
                IyziCoSDKType.CASH_OUT -> {
                    reCreateCashOut()
                }
                IyziCoSDKType.TOPUP -> {
                    reCreateTopup()
                }
            }
        }

        clickListener()

        return root
    }

    open fun initUI(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {

    }

    open fun reCreate(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {

    }

    open fun reCreateTopup() {

    }

    open fun reCreateCashOut() {

    }

    open fun reCreateSettlement() {

    }

    open fun reCreateRefund() {

    }

    open fun initUITopup() {

    }

    open fun initUICashOut() {

    }

    open fun initUISettlement() {

    }

    open fun initUIRefund() {

    }

    open fun reCreatePayWithIyziCo() {

    }

    open fun initUIPayWithIyziCo() {

    }


    open fun clickListener() {

    }

    fun getCurrentFragment() = activity.getCurrentFragment()

    fun showCloseButton() {
        activity.showCloseButton()
    }

    fun showBackButton() {
        activity.showBackButton()
    }

    fun navigate(fragment: Fragment, addStack: Boolean = false) {
        hideKeyboard()
        activity.openFragment(fragment, addStack)
    }

    fun openKeyboard() {
        activity.openKeyboard()
    }

    fun navigateUp() {
        activity.onBackPressed()
    }

    fun hideTopItems() {
        activity.hideTopItems()
    }

    fun clearStack() {
        activity.clearStack()
    }

    fun hideKeyboard() {
        activity.hideSoftKeyboard()
    }

    fun getIyziCoActivity(): IyziCoActivity? {
        return if (this::activity.isInitialized) {
            activity
        } else {
            null
        }
    }

    fun getYear(): Int {
        return activity.getYear()
    }

    fun getMonth(): Int {
        return activity.getMonth()
    }

    fun finish() {
        IyziCoConfig.MERCHANT_API_KEY = ""
        IyziCoConfig.MERCHANT_SECRET_KEY = ""
        activity.finish()
    }

    fun showLoadingAnimation() {
        hideKeyboard()
        activity.showLoadingAnimation()
    }

    fun hideLoadingAnimation() {
        activity.hideLoadingAnimation()
    }

    fun hideBackButton() {
        activity.hideBackButton()
    }

    override fun onDestroyView() {
        val parentViewGroup = view?.parent as ViewGroup?
        parentViewGroup?.removeAllViews()
        super.onDestroyView()
    }

    fun callbackMessage(message: String) {
        callback?.message(message)
    }

    fun showTopItem() {
        activity.showTopItem()
        activity.iyzico_payw_sticky_brand_textView.setMargins(16, 0)

    }

    fun hideWalletSticky() {
        activity.hideWalletSticky()
    }

    fun showWalletSticky() {
        activity.showWalletSticky()
    }

    fun showStickLayout() {
        activity.showStickLayout()
    }

    fun startPayWithIyziCoTimer() {
        showTimerSticky()
        activity.startPayWithIyziCoTimer()
    }

    fun setTimerAgain(){
        activity.setTimerAgain()
    }
    fun callbackError(code: ResultCode, message: String) {
        callback?.error(code, message)
    }


    fun showSDKError(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    fun removeTimer() {
        activity.removeTimer()
    }

    fun showIyziCoBalance(walletPrice: String) {
        activity.showIyziCoBalance(walletPrice)
    }

    fun setPwiPrice(price: String) {
        activity.setPwiPrice(price)
    }

    fun hideIyziCoBalance() {
        activity.hideIyziCoBalance()
    }

    fun hideTimerSticky() {
        activity.hideTimerSticky()
    }

    fun showTimerSticky() {
        activity.showTimerSticky()
    }

    fun setIsSuccess() {
        activity.setIsSuccess()
    }

    fun createErrorPopup(
        message: String
    ) {
        activity.openInformationDialog(
            IyziCoPopupType.ERROR_POPUP,
            R.drawable.iyzico_error_image,
            message,
            "",
            getString(R.string.iyzico_okey_text),
            null,
            {
            },
            null
        )
    }

}