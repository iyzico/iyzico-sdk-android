package com.android.iyzico

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.iyzicosdk.callback.IyziCoCallback
import com.android.iyzicosdk.core.IyziCo
import com.android.iyzicosdk.data.model.request.IyziCoBasketItem
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
        IyziCo.client().initialize(
            "127.0.0.1",
            "qumpara",
            "qumparaSecret",
            "https://consumerapigw.iyzipay.com/",
            Languages.TURKISH
        )

        start.setOnClickListener {
            IyziCo.client().initialize(
                "127.0.0.1",
                "qumpara",
                "qumparaSecret",
                "https://consumerapigw.iyzipay.com/",
                Languages.TURKISH,
                "sandbox-DR5z07SXKVWyzwOHR6c85tccPb5ogOE9",
                "sandbox-fEzf4Lbbzw5yGOVjGGl6UF2sM3CZ2nPl"
            )
            IyziCo.client().startPayWithIyziCo(
                this,
                "Lidyana.com",
                60.0,
                60.0,
                Currency.TL,
                arrayOf(2, 3, 6, 9),
                "B67832",
                PaymentGroup.SUBSCRIPTION,
                "https://www.merchant.com/callback",
                "BY789",
                "John",
                "Doe",
                "74300864791",
                "Istanbul",
                "Turkey",
                "yenimail7920212424@email.com",
                "5558765421",
                "Merchant Ip",
                "Merchant Adres",
                "34218",
                "2015-09- 17 23:45:06 ",
                "2018-09- 17 23:45:06 ",
                "JAne due",
                "Bursa",
                "Turkey",
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",
                "Jane Doe",
                "Istanbul",
                "Turkey",
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",
                BasketItemType.PHYSICAL.type,
                "Binocular",
                "Collectibles",
                "BI101",
                "5352121212",
                listOf(
                    IyziCoBasketItem(
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
            IyziCo.client().startRefund(
                this,
                "yenimail792021-2@email.com",
                "5457878282",
                "1234",
                "fff",
                "fff",
                iyzicoCallback
            )
        }

        settlement.setOnClickListener {
            IyziCo.client().startSettlement(
                this,
                "yenimail792021-3@email.com",
                "5457878202",
                225.30,
                "fff",
                "fff",
                iyzicoCallback
            )
        }

        topup.setOnClickListener {
            IyziCo.client().startTopUp(
                this,
                "yenimail792021242443@email.com",
                "5321869654",
                "Lidyana.com",
                "fff",
                "fff",
                iyzicoCallback
            )
        }
//                "vurw17829al@basefy.com",
        cashOut.setOnClickListener {
            IyziCo.client().startCashOut(
                this,
                "yenimail792021242448@gmail.com",
                "5354817252",
                800.99,
                "mm",
                "fff",
                iyzicoCallback
            )
        }
        IyziCo


    }

    private val iyzicoCallback = object : IyziCoCallback {
        override fun message(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }

        override fun error(code: ResultCode, message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}