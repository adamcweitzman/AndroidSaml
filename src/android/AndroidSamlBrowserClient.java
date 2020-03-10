package com.adamweitzman.cordova.plugin;

import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import java.util.Base64;

public class AndroidSamlBrowserClient extends WebViewClient {
    private final AndroidSamlBrowserDialog dialog;
    CordovaWebView webView;
    CallbackContext callbackContext;
    private String TAG = "AndroidSaml";
    AndroidSaml androidSaml;
    Runnable runnableContext;

    public AndroidSamlBrowserClient(CordovaWebView webView, CallbackContext callbackContext, AndroidSamlBrowserDialog dialog) {
        this.webView = webView;
        this.callbackContext = callbackContext;
        this.dialog = dialog;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:" + "(function(){var el = document.getElementsByName(\"SAMLResponse\")[0]; return (el ? el.value : null);})()" );

        view.evaluateJavascript("(function(){return window.document.body.innerHTML})();", new ValueCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceiveValue(String s) {
                logLongString(s);
                Log.i(TAG, "revieved value from page load: " + s);

                boolean isToken = isAlphaNumeric(s);


//                try {
//                    Base64.Decoder decoder = Base64.getDecoder();
//                    decoder.decode(s);
//                    Log.i(TAG, s);
//                    callbackContext.success(s);
//                } catch (IllegalArgumentException ex) {
//                    Log.i(TAG, "not a base 64 encoded string");
//                }


//                boolean isToken = Base64.isBase64(test);
//
//
                Log.i(TAG, "matches: " + isToken);
                if(isToken){
                    Log.i(TAG, s);
                    callbackContext.success(s);
                }
            }
        });
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
        Log.i(TAG, "in should override url loading...");
        return true;
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        Log.i(TAG, "in form resubmission...");
    }

    public boolean isAlphaNumeric(String s){
        Log.i(TAG, "Testing string: " + s);
        String pattern= "^[a-zA-Z0-9+=\\/\"]*";
        return s.matches(pattern);
    }

    public void logLongString(String str) {
        if(str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            logLongString(str.substring(4000));
        } else
            Log.i(TAG, str);
    }

}