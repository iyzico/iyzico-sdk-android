package com.android.iyzico

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.iyzicosdk.callback.IyzicoCallback
import com.android.iyzicosdk.core.Iyzico
import com.android.iyzicosdk.data.model.request.IyzicoBasketItem
import com.android.iyzicosdk.util.enums.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val URL = "https://sandbox-consumerapigw.iyzipay.com/"
    val CLIEND_ID = "qumpara"
    val CLIEND_SECRET_KEY = "qumparaSecret"
    val MERCHANT_API_KEY = "sandbox-BBjF3qdtrFBVC5VC8bOTqZJw1rpLz1dK"
    val MERCHANT_SECRET_KEY = "sandbox-ZvLTaQaWZtTMIhvxoqhfDG4IJA9bKNBV"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //canli             "https://consumerapigw.iyzipay.com/",
        Iyzico.client().initialize(
            "127.0.0.1",
            CLIEND_ID,
            CLIEND_SECRET_KEY,
            URL,
            Languages.TURKISH, MERCHANT_SECRET_KEY, MERCHANT_API_KEY
        )
        /*  "127.0.0.1",
          "tyHJXKUb7uxeksaejjLjTgC8ZQYuQ9",
          "zb5KTrDatdP9ebSeHmQ5SG4SGJw3eR",
          "https://sandbox-consumerapigw.iyzipay.com/",*/


        /**
         * Sandbox
         *
         * https://sandbox-consumerapigw.iyzipay.com/
         * qumpara,
         * qumparaSecret",
         *
         * */


        /**
         * Prod
         *
         * https://consumerapigw.iyzipay.com
         * tyHJXKUb7uxeksaejjLjTgC8ZQYuQ9,
         * zb5KTrDatdP9ebSeHmQ5SG4SGJw3eR",
         * */


        start.setOnClickListener {
            Iyzico.client().initialize(
                "127.0.0.1",
                CLIEND_ID,
                CLIEND_SECRET_KEY,
                URL,
                Languages.TURKISH,
                MERCHANT_SECRET_KEY,
                MERCHANT_API_KEY
            )

            Iyzico.client().startPayWithIyzico(
                this,
                "Lidyana.com",
                152.00,
                152.00,
                Currency.TRY,
                arrayOf(1, 2, 3, 6, 9),
                "B67832",//zorunlu
                PaymentGroup.PRODUCT,//zorunlu değil
                "https://www.merchant.com/callback",
                "BY789",//zorunlu
                "John",//zorunlu
                "Doe",//zorunlu
                "74300864791",//zorunlu
                "Istanbul",//zorunlu
                "Turkey",//zorunlu
                "aktasbatuhann@gmail.com",
                "5616100697",//zorunlu
                "Merchant Ip",//ıp zorunlu
                "Merchant Adres",//zorumaskedGsmNumbernlu
                "JAne due",//zounlu
                "Bursa",//zorunlu
                "Turkey",//zorunlu
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",//orunlu
                "Jane Doe",//zorunlu
                "Istanbul",//zorunlu
                "Turkey",//zorunlu
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",//zorunlu
                listOf(
                    IyzicoBasketItem(
                        "Collectibles",
                        "BI101",
                        BasketItemType.PHYSICAL,
                        "Binocular",
                        "152.00",
                        null, null
                    )
                ),
                iyzicoCallback
            )
        }
        refund.setOnClickListener {
            /* Iyzico.client().startRefund(
                 this,
                 "yenimail792021-2@email.com",
                 "5457878282",
                 "1234",
                 "fff",
                 "fff",
                 iyzicoCallback
             )*/
        }

        settlement.setOnClickListener {
            /* Iyzico.client().startSettlement(
                 this,
                 "yenimail792021-3@email.com",
                 "5457878202",
                 225.30,
                 "fff",
                 "fff",
                 iyzicoCallback
             )*/
        }

        topup.setOnClickListener {
            Iyzico.client().startTopUp(
                this,
                "aktasbatuhann@gmail.com",
                "5616100697",
                "Lidyana.com",
                "fff",
                "fff",
                iyzicoCallback
            )
        }
//                "vurw17829al@basefy.com",
        cashOut.setOnClickListener {
            Iyzico.client().startCashOut(
                this,
                "aktasbatuhann@gmail.com",
                "5616100697",
                9.99,
                "mm",
                "fff",
                iyzicoCallback
            )
        }


    }

    private val iyzicoCallback = object : IyzicoCallback {
        override fun message(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }

        override fun error(code: ResultCode, message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}