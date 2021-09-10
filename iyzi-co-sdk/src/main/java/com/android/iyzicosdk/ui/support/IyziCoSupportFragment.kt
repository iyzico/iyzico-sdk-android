import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.ui.support.IyziCoBottomSupportScreen
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.IyziCoUrlConstants
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_activity_iyzico.*
import kotlinx.android.synthetic.main.iyzico_fragment_support_.view.*
import java.io.Serializable
import java.net.URLDecoder

@Keep
internal class IyziCoSupportFragment : IyziCoBaseFragment() {
    override val layoutRes: Int = R.layout.iyzico_fragment_support_
    lateinit var url: IyziCoSupportScreenType
    var html = ""
    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        url =
            arguments?.let { it.getSerializable(BundleConstans.URL) as IyziCoSupportScreenType }
                ?: IyziCoSupportScreenType.SUPPORT
        html = arguments?.let { it.getString(BundleConstans.HTML_CODE) } ?: ""
        hideTopItems()
        root.iyzico_support_screen_button.drawableTextStart(R.drawable.iyzico_icn_support)
        root.iyzico_contracts_webview_layout_close_button.setOnClickListener {
            navigateUp()
            //hideTopItems()

        }
        root.iyzico_support_screen_button.clickListener {
            IyziCoBottomSupportScreen.showSupportBottom(
                getIyziCoActivity()?.supportFragmentManager
            )
        }
        when (url) {
            IyziCoSupportScreenType.PRIVACY_POLICY -> {
                setWebView(IyziCoUrlConstants.privacyPolicy)
            }
            IyziCoSupportScreenType.USER_AGREEMENT -> {
                setWebView(IyziCoUrlConstants.userAgreement)
            }
            IyziCoSupportScreenType.SUPPORT -> {
                root.iyzico_contracts_webview_layout.gone()
                root.iyzico_support_screen_button.show()
                root.iyzico_fragment_support_text_layout.show()
            }
            IyziCoSupportScreenType.COMMUNICATION -> {
                setWebView(IyziCoUrlConstants.communication)
            }
            IyziCoSupportScreenType.KVKK_PWI -> {
                setWebView(IyziCoUrlConstants.kvkk)

            }
            IyziCoSupportScreenType.USER_AGREMENT_PWI -> {
                setWebView(IyziCoUrlConstants.userAgreementPwi)

            }
            IyziCoSupportScreenType.THREED_PAYMENT -> {
                threeDPayment()
            }
        }
    }

    fun setWebView(myurl: String) {
        root.iyzico_contracts_webview_layout.let {
            it.setWebViewClient(WebViewClient())
            it.getSettings().setJavaScriptEnabled(true)
            it.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
            it.getSettings().setPluginState(WebSettings.PluginState.ON)
            it.getSettings().setMediaPlaybackRequiresUserGesture(false)
            it.setWebChromeClient(WebChromeClient())
            it.loadUrl(myurl)
            it.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showLoadingAnimation()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    hideLoadingAnimation()
                }
            }
        }
    }

    private fun threeDPayment() {
        root.iyzico_contracts_webview_layout_close_button.setImageResource(R.drawable.iyzico_ic_close)

        val settings: WebSettings = root.iyzico_contracts_webview_layout.getSettings()
        settings.defaultTextEncodingName = "utf-8"
        settings.javaScriptEnabled = true
        root.iyzico_contracts_webview_layout.addJavascriptInterface(
            MyWebViewJavascriptInterface(
                requireContext(),
                root.iyzico_contracts_webview_layout
            ), "Android"
        ) // Buraya dikkat


        root.iyzico_contracts_webview_layout.loadDataWithBaseURL(
            null,
            html,
            "text/html",
            "utf-8",
            null
        ) // elimizde bulunan bir html dökümanı webview e basıyoruz. URL de verebilirsin istersen


        root.iyzico_contracts_webview_layout.addJavascriptInterface(
            MyWebViewJavascriptInterface(
                requireContext(),
                root.iyzico_contracts_webview_layout
            ), "Android"
        )

        val mWebViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (url.contains("callback3ds/success")) {

                    navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.TRANSFER),
                        false
                    )

                } else if (url.contains("callback3ds/failure")) {

                    navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                        false
                    )

                } else {
                    ""
                    //bu durumda ne yapmalıyız
                }
            }
        }

        root.iyzico_contracts_webview_layout.webViewClient = mWebViewClient
    }

    internal class MyWebViewJavascriptInterface(
        var mContext: Context,
        var mWebView: WebView
    ) {
        @JavascriptInterface
        fun processHTML(htmlContent: String?) { // işlem
// Toast ile html içeriğini gösterebiliriz
        }

        @JavascriptInterface
        fun islemeDevamEt() { // işlem
        }

        @JavascriptInterface
        fun pencereyiKapat() { // işlem
// dialog.dismiss();
        }

        internal class MyWebViewJavascriptInterface(
            var mContext: Context,
            var mWebView: WebView
        ) {
            @JavascriptInterface
            fun processHTML(htmlContent: String?) { // işlem
// Toast ile html içeriğini gösterebiliriz
            }

            @JavascriptInterface
            fun islemeDevamEt() { // işlem
            }

            @JavascriptInterface
            fun pencereyiKapat() { // işlem
// dialog.dismiss();
            }

        }
    }

    companion object {
        fun newInstance(url: IyziCoSupportScreenType, html: String = ""): IyziCoSupportFragment {
            val fragment = IyziCoSupportFragment()
            val bundle = Bundle()
            bundle.putSerializable((BundleConstans.URL), url as Serializable)
            bundle.putString(BundleConstans.HTML_CODE, html)
            fragment.arguments = bundle
            return fragment
        }
    }
}
