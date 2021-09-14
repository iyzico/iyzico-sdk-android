package com.android.iyzico;


import com.android.iyzicosdk.core.Iyzico;
import com.android.iyzicosdk.util.enums.Languages;

public class JavaMain {

    public JavaMain() {
        Iyzico.client().initialize(
                "127.0.0.1", "qumpara", "qumparaSecret", "\"https://sandbox-consumerapigw.iyzipay.com/", Languages.TURKISH, "", ""
        );
    }
}
