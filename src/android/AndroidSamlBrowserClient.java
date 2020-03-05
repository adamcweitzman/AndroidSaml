package com.adamweitzman.cordova.plugin;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
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

    // TODO: 3/5/20 i think i have the bearer token now, figure out how to get that back from the app

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:" + "(function(){var el = document.getElementsByName(\"SAMLResponse\")[0]; return (el ? el.value : null);})()" );
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
        return true;
    }
}