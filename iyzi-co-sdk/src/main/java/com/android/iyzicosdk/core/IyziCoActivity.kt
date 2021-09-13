package com.android.iyzicosdk.core

import IyziCoSupportFragment
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.iyzicosdk.R
import com.android.iyzicosdk.callback.IyziCoCallback
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.ui.have_member_login.IyziCoMemberLoginFragment
import com.android.iyzicosdk.util.KeyboardUtils
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.constants.TextMessages
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoPopupType
import com.android.iyzicosdk.util.enums.ResultCode
import com.android.iyzicosdk.util.extensions.*
import kotlinx.android.synthetic.main.iyzico_activity_iyzico.*
import kotlinx.android.synthetic.main.iyzico_payw_sticky.*
import kotlinx.android.synthetic.main.iyzico_wallet_sticky.*
import java.util.*
import kotlin.concurrent.fixedRateTimer


internal class IyziCoActivity : AppCompatActivity() {

    private val emailFragment by lazy { IyziCoLoginFragment() }
    private var timer = Timer()
    private var savedInstanceState: Bundle? = null
    internal var callback: IyziCoCallback? = null
    internal var initialRequestId: String? = null
    private var isSuccess = false
    private var progressDialog: Dialog? = null
    private var progressDialogtext = ""
    private var progressDialogPositiveText = ""


    var finisTime = 0
    var isTimerContinue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iyzico_activity_iyzico)
        this.savedInstanceState = savedInstanceState
        IyziCo.client().callback?.let { this.callback = it }
        hideSoftKeyboard()

        when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
            IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                openEmailFragment(savedInstanceState)
                progressDialogtext = getString(R.string.iyzico_close_widget_pop_up_pwi)
                progressDialogPositiveText =
                    getString(R.string.iyzico_close_widget_pop_up_pwi_button)
                initPayWithIyziCo()
            }
            IyziCoSDKType.REFUND -> {
                openEmailFragment(savedInstanceState)
                progressDialogtext = getString(R.string.iyzico_close_widget_pop_up_refund)
                progressDialogPositiveText =
                    getString(R.string.iyzico_close_widget_pop_up_refund_button)
                initRefund()
            }
            IyziCoSDKType.SETTLEMENT -> {
                openEmailFragment(savedInstanceState)
                progressDialogtext = getString(R.string.iyzico_close_widget_pop_up_settlement)
                progressDialogPositiveText =
                    getString(R.string.iyzico_close_widget_pop_up_settlement_button)
                initSettlement()
            }
            IyziCoSDKType.CASH_OUT -> {
                openEmailFragment(savedInstanceState)
                progressDialogtext = getString(R.string.iyzico_close_widget_pop_up_cashout)
                progressDialogPositiveText =
                    getString(R.string.iyzico_close_widget_pop_up_cashout_button)
                initCashOut()
            }
            IyziCoSDKType.TOPUP -> {
                openEmailFragment(savedInstanceState)
                progressDialogtext = getString(R.string.iyzico_close_widget_pop_up_top_up)
                progressDialogPositiveText =
                    getString(R.string.iyzico_close_widget_pop_up_top_up_button)
                initTopup()
            }
        }

        clickInit()

    }

    private fun openEmailFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null && currentFragment == null) {
            openFragment(emailFragment, true)
        }
    }

    fun setIsSuccess() {
        isSuccess = true
    }

    /**
     * Headerda bir güncelleme filan olacaksa veya açılacak fragment değişecek ise aşağıdaki 4 fonksiyon kullanılır
     */
    private fun initPayWithIyziCo() {
        showTopItem()
    }

    internal fun startPayWithIyziCoTimer() {
        if (iyzico_payw_sticky_layout.visibility == View.GONE) {
            iyzico_payw_sticky_layout.show()
            iyzico_payw_sticky_brand_textView.text = IyziCoResourcesConstans.IyziCoBrand
            iyzico_payw_sticky_price_textView.text =
                IyziCoResourcesConstans.IyziCoProductPrice.toString().addTlIcon()

            finisTime = 1 * 1000 * 60 * 5
            startTimer()

        }
    }

    internal fun setTimerAgain() {
        finisTime = 1 * 1000 * 60 * 5
        timer.cancel()
        startTimer()
    }

    internal fun getCurrentFragment():Fragment = currentFragment!!

    private fun startTimer() {
        isTimerContinue = true
        timer = fixedRateTimer("timer", false, 0, 1000) {
            runOnUiThread {
                finisTime -= 1000
                iyzico_payw_timer_TextView.text = "Kalan Süre: ${
                    finisTime.toLong().toTimer {
                        timer.cancel()
                        openInformationDialog(
                            IyziCoPopupType.TIMEOUT_POPUP,
                            R.drawable.iyzico_ic_hourglass,
                            getString(R.string.iyzico_buying_time_is_finish_error),
                            "",
                            getString(R.string.iyzico_common_button_try_again),
                            {
                                removeTimer()
                                // Set empty for when open flow at timeout for create again true header
                                IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY = ""
                                openFragment(

                                    IyziCoLoginFragment.newInstance(
                                        IyziCoLoginScreenType.TRY_AGAIN
                                    ), false
                                )
                            },
                            {

                            },
                            {
                                IyziCoConfig.MERCHANT_API_KEY = ""
                                IyziCoConfig.MERCHANT_SECRET_KEY = ""
                                callback?.error(ResultCode.FAIL, TextMessages.TIME_OUT_TEXT)
                                this@IyziCoActivity.finish()
                            }
                        )

                    }
                }"
            }
        }
    }

    /**
     * Sayacı sıfırlayıp kapatmayı sağlar
     */
    internal fun removeTimer() {
        timer.cancel()
        iyzico_payw_sticky_layout.gone()
        isTimerContinue = false
    }

    internal fun initRefund() {
    }

    internal fun initSettlement() {
    }

    internal fun initCashOut() {
    }

    internal fun initTopup() {

    }

    internal fun showStickLayout() {
        if(currentFragment !is IyziCoMemberLoginFragment && currentFragment !is IyziCoSupportFragment){
            iyzico_payw_sticky_layout.show()
            if (!isTimerContinue)
                startTimer()
        }

    }

    internal fun showCloseButton() {
        iyzico_activity_iyzico_header_icon_imageView.show()
        iyzico_activity_iyzico_header_icon_imageView.setImageResource(R.drawable.iyzico_ic_close)
        iyzico_activity_back_frameLayout.setOnClickListener {
            openInformationDialog(IyziCoPopupType.CLOSE_OR_CONTINUE_PAGE,
                R.drawable.iyzico_ic_logout,
                progressDialogtext,
                getString(R.string.iyzico_custom_popup_close_page_button_text),
                getString(R.string.iyzico_custom_popup_close_continue_button),
                backButtonCallback = {

                    IyziCoConfig.MERCHANT_API_KEY = ""
                    IyziCoConfig.MERCHANT_SECRET_KEY = ""
                    if (isSuccess) {
                        callback?.message(TextMessages.SUCCESS)
                    } else {
                        callback?.error(
                            ResultCode.CLOSED_TRANSACTION,
                            TextMessages.CLOSED_TRANSACTION
                        )
                    }
                    IyziCoConfig.MERCHANT_API_KEY = ""
                    IyziCoConfig.MERCHANT_SECRET_KEY = ""
                    this@IyziCoActivity.finish()
                },
                continueButtonCallback = {
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                        showStickLayout()
                    }
                }
            )
        }
    }

    internal fun hideBackButton() {
        iyzico_activity_iyzico_header_icon_imageView.gone()
    }

    internal fun showBackButton() {
        iyzico_activity_iyzico_header_icon_imageView.show()
        iyzico_activity_iyzico_header_icon_imageView.setImageResource(R.drawable.iyzico_ic_back)
        iyzico_activity_back_frameLayout.setOnClickListener {
            onBackPressed()
            showCloseButton()
        }
    }

    /**
     * IyziCo bakiyeyi üst tarafta göstermek için kullanılır
     */
    internal fun showIyziCoBalance(walletPrice: String) {
        iyzico_wallet_sticky_layout.show()
        iyzico_wallet_sticky_wallet_price_Textview.setText(walletPrice.addTlIcon())
    }

    internal fun hideTopItems() {
        iyzico_Activity_top_linear_layout.gone()
        iyzico_payw_sticky_layout.gone()
        iyzico_wallet_sticky_layout.gone()
    }

    internal fun setPwiPrice(price: String) {
        iyzico_payw_sticky_price_textView.text = price.addTlIcon()
    }

    internal fun hideWalletSticky() {
        iyzico_wallet_sticky_layout.gone()
    }

    internal fun hideTimerSticky() {
        iyzico_payw_timer_linearLayout.gone()
    }

    internal fun showTimerSticky() {
        iyzico_payw_timer_linearLayout.show()

    }

    internal fun showTopItem() {
        iyzico_Activity_top_linear_layout.show()
    }

    /**
     * IyziCo bakiyeyi üst tarafttan kaldırmak için kullanılır
     */
    internal fun hideIyziCoBalance() {
        iyzico_wallet_sticky_layout.gone()
    }

    internal fun showWalletSticky() {
        iyzico_wallet_sticky_layout.show()
    }


    private fun clickInit() {
        iyzico_activity_back_frameLayout.setOnSafeClickListener {
            onBackPressed()
        }
    }

    val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.activity_iyzico_container)

    /**
     * Stackdeki fragmentleri silmeye yarar
     * bu işlem pin dogrulamasından sonra ödeme ekranına geçince
     * stackdeki fragmentleri silmek için kullanıldı
     * */
    internal fun clearStack() {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        IyziCoLoginFragment.iyziCoLoginScreenType = IyziCoLoginScreenType.NORMAL
    }

    fun hideSoftKeyboard() {
        KeyboardUtils.forceCloseKeyboard(window.decorView)
    }

    fun getYear(): Int {
        val now: Calendar = Calendar.getInstance()
        return now.get(Calendar.YEAR)
    }

    fun getMonth(): Int {
        val now: Calendar = Calendar.getInstance()
        return (now.get(Calendar.MONTH) + 1)
    }


    internal fun openFragment(fragment: Fragment, addStack: Boolean = false) {
        hideSoftKeyboard()
        val fm = supportFragmentManager.beginTransaction()
        if (addStack) {
            fm.addToBackStack(null)
        }

        fm.replace(R.id.activity_iyzico_container, fragment)
            .commit()

    }

    internal fun openKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onBackPressed() {
        removeTimer()
        hideSoftKeyboard()
        if (supportFragmentManager.backStackEntryCount > 1) {
            val fl = findViewById<FrameLayout>(R.id.activity_iyzico_container)
            fl.removeAllViews()
            super.onBackPressed()
            if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                showStickLayout()
            }
        } else {
            if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                showStickLayout()
            }
            openInformationDialog(IyziCoPopupType.CLOSE_OR_CONTINUE_PAGE,
                R.drawable.iyzico_ic_logout,
                progressDialogtext,
                getString(R.string.iyzico_custom_popup_close_page_button_text),
                getString(R.string.iyzico_custom_popup_close_continue_button),
                backButtonCallback = {
                    IyziCoConfig.MERCHANT_API_KEY = ""
                    IyziCoConfig.MERCHANT_SECRET_KEY = ""
                    this@IyziCoActivity.finish()
                },
                continueButtonCallback = {
                }
            )
        }
    }

    override fun onDestroy() {
        IyziCo.client().callback = null
        callback = null
        IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY = ""
        super.onDestroy()
    }

    fun openInformationDialog(
        type: IyziCoPopupType,
        imageId: Int,
        message: String,
        negativeButtonText: String = "",
        positiveButtonText: String,
        backButtonCallback: (() -> Unit?)? = null,
        continueButtonCallback: () -> Unit,
        closeButtonCallback: (() -> Unit?)? = null

    ) {
        createPopup(this, type,
            imageId,
            message,
            negativeButtonText,
            positiveButtonText,
            continueButtonCallback = {
                continueButtonCallback()
            }, backButtonCallback = {
                backButtonCallback?.let {
                    it.invoke()
                }
            },
            closeButtonCallback = {
                closeButtonCallback?.let {
                    it.invoke()
                }
            })
    }

    fun showLoadingAnimation() {

        progressDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        } ?: run {
            progressDialog = Dialog(this, R.style.IyziCoCustomDialogTheme).animationLoading()
            progressDialog?.show()
        }
    }

    fun hideLoadingAnimation() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, IyziCoActivity::class.java)
            context.startActivity(intent)
        }
    }
}