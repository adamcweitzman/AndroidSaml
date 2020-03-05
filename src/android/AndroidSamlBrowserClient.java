package com.adamweitzman.cordova.plugin;

import android.webkit.WebViewClient;
import org.apache.cordova.CordovaWebView;

public class AndroidSamlBrowserClient extends WebViewClient {
//    EditText edittext;
    CordovaWebView webView;
    String beforeload;
    boolean waitForBeforeload;

    public AndroidSamlBrowserClient(CordovaWebView webView) {
        this.webView = webView;
//        this.edittext = mEditText;
//        this.beforeload = beforeload;
//        this.waitForBeforeload = beforeload != null;
    }
}