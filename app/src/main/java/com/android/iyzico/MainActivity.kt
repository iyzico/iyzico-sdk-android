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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*   IyzicoPattern.Builder("asd").setEmail("kemaltunc191@gmail.com").build()*/
        /**
         * Builder patternmi yoksa bu şekil bir kullanımmı buna daha sonra karar verilecek
         * */
        Iyzico.client().initialize(
            "127.0.0.1",
            "tyHJXKUb7uxeksaejjLjTgC8ZQYuQ9",
            "zb5KTrDatdP9ebSeHmQ5SG4SGJw3eR",
            "https://consumerapigw.iyzipay.com/",
            Languages.TURKISH
        )

        start.setOnClickListener {
            Iyzico.client().initialize(
                "127.0.0.1",
                "tyHJXKUb7uxeksaejjLjTgC8ZQYuQ9",
                "zb5KTrDatdP9ebSeHmQ5SG4SGJw3eR",
                "https://consumerapigw.iyzipay.com/",
                Languages.TURKISH,
                "MU2SxEhkC3bTwSsgnUvPcBsKUzNzoW9O",
                "aaqZZtlaUdEQY1DgK1Guw8Prj16Hv6gg"
            )

            Iyzico.client().startPayWithIyzico(
                this,
                "Lidyana.com",
                60.0,
                60.0,
                Currency.TRY,
                arrayOf(2, 3, 6, 9),
                "B67832",//zorunlu
                PaymentGroup.SUBSCRIPTION,//zorunlu değil
                "https://www.merchant.com/callback",
                "BY789",//zorunlu
                "John",//zorunlu
                "Doe",//zorunlu
                "74300864791",//zorunlu
                "Istanbul",//zorunlu
                "Turkey",//zorunlu
                "msahincakir34+22092021@gmail.com",
                "5555555555",//zorunlu
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
                        "60.00"
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
                "msahincakir34+22092021@gmail.com",
                "5321869654",
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
                "msahincakir34+22092021@gmail.com",
                "5354817252",
                800.99,
                "mm",
                "fff",
                iyzicoCallback
            )
        }
        Iyzico


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