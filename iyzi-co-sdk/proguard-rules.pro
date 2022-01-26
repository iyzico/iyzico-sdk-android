# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep public class com.android.iyzicosdk.data.model.request.IyzicoBasketItem
-keep public enum com.android.iyzicosdk.util.enums.Languages
-keep public enum com.android.iyzicosdk.util.enums.Currency
-keep public enum com.android.iyzicosdk.util.enums.PaymentGroup
-keep public enum com.android.iyzicosdk.util.enums.BasketItemType
-keep public enum com.android.iyzicosdk.util.enums.ResultCode
-keep public class  com.android.iyzicosdk.core.IyziCoActivity


-keep public class  com.android.iyzicosdk.core.Iyzico {
public static com.android.iyzicosdk.core.Iyzico client();
   public static ** Companion;
}
-keep public class com.android.iyzicosdk.callback.IyzicoCallback

-dontwarn org.jetbrains.annotations.
-keep class kotlin.Metadata { *; }
-keepclassmembers class com.android.iyzicosdk.data.model** {
  <init>(...);
  <fields>;
}
-keepclassmembers class com.android.iyzicosdk.data.repository.base.IyziCoBaseResponse {
  <init>(...);
  <fields>;
}
