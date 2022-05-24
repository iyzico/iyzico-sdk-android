package com.android.iyzico;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.iyzicosdk.callback.IyzicoCallback;
import com.android.iyzicosdk.core.Iyzico;
import com.android.iyzicosdk.data.model.request.IyzicoBasketItem;
import com.android.iyzicosdk.util.enums.BasketItemType;
import com.android.iyzicosdk.util.enums.Currency;
import com.android.iyzicosdk.util.enums.Languages;
import com.android.iyzicosdk.util.enums.PaymentGroup;
import com.android.iyzicosdk.util.enums.ResultCode;

import java.util.ArrayList;
import java.util.List;

public class JavaMain extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Iyzico.client().initialize(
                "127.0.0.1", "qumpara", "qumparaSecret", "\"https://sandbox-consumerapigw.iyzipay.com/", Languages.TURKISH, "", ""
        );


        Integer[] myIntArray = new Integer[]{1, 2, 3};
        List<IyzicoBasketItem> myList = new ArrayList<>();
        myList.add(new IyzicoBasketItem("Collectibles",
                "BI101",
                BasketItemType.PHYSICAL,
                "Binocular",
                "60.00",
                "Sll5o4Dd3msNHWaHmJ5h4UOv7yA=",
                "50.19"));
        //PROD_URL="https://consumerapigw.iyzipay.com/"
        //TEST_URL="https://sandbox-consumerapigw.iyzipay.com/"
        Iyzico.client().initialize(
                "127.0.0.1",
                "qumpara",
                "qumparaSecret",
                "https://sandbox-consumerapigw.iyzipay.com/",
                Languages.TURKISH,
                "sandbox-nwm4ooTetPJDChjBxf3WqQwKW6qr6irx",
                "sandbox-9mf76Q4wJLZjv2GxyM8BflBfrAYSULTD"
        );


        Iyzico.client().startPayWithIyzico(
                this,
                "brand name",
                60.0,
                60.0,
                Currency.TRY,
                myIntArray,
                "B67832",//zorunlu
                PaymentGroup.SUBSCRIPTION,//zorunlu değil
                "https://www.merchant.com/callback",
                "BY789",//zorunlu
                "name",//zorunlu
                "surname",//zorunlu
                "74300864791",//zorunlu
                "Istanbul",//zorunlu
                "Turkey",//zorunlu
                "yourmail@email.com",
                "5555555555",//zorunlu
                "Merchant Ip",//ıp zorunlu
                "Merchant Adres",//zorunlu
                "JAne due",//zounlu
                "Bursa",//zorunlu
                "Turkey",//zorunlu
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",//orunlu
                "Jane Doe",//zorunlu
                "Istanbul",//zorunlu
                "Turkey",//zorunlu
                "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1",//zorunlu
                myList,
                new IyzicoCallback() {
                    @Override
                    public void message(String s) {
                        Toast.makeText(JavaMain.this, s,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(ResultCode resultCode, String s) {
                        Toast.makeText(JavaMain.this, s,
                                Toast.LENGTH_LONG).show();
                    }


                    @Override
                    public void balanceComplete(@NonNull String message, @NonNull String amount) {
                        Toast.makeText(JavaMain.this, amount,
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
