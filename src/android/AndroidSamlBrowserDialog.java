package com.adamweitzman.cordova.plugin;

import android.app.Dialog;
import android.content.Context;

public class AndroidSamlBrowserDialog extends Dialog {
    Context context;
    AndroidSaml androidSaml = null;

    public AndroidSamlBrowserDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public void setInAndroidSaml(AndroidSaml browser) {
        this.androidSaml = browser;
    }

    public void onBackPressed () {
//        if (this.inAppBrowser == null) {
//            this.dismiss();
//        } else {
//            // better to go through the in inAppBrowser
//            // because it does a clean up
//            if (this.inAppBrowser.hardwareBack() && this.inAppBrowser.canGoBack()) {
//                this.inAppBrowser.goBack();
//            }  else {
//                this.inAppBrowser.closeDialog();
//            }
//        }
    }
}
