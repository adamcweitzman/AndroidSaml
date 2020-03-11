package com.adamweitzman.cordova.plugin;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.webkit.ClientCertRequest;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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

    public AndroidSamlBrowserClient(CordovaWebView webView, CallbackContext callbackContext, AndroidSamlBrowserDialog dialog) {
        this.webView = webView;
        this.callbackContext = callbackContext;
        this.dialog = dialog;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        checkIfPageIsSaml(view, "ON PAGE FINISHED");
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        checkIfPageIsSaml(view, "ON LOAD RESOURCE");
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        checkIfPageIsSaml(view, "ON PAGE COMMIT VISIBLE");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        checkIfPageIsSaml(view, "ON PAGE STARTED");
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        checkIfPageIsSaml(view, "ON RECEIVED LOGIN REQUEST");
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        checkIfPageIsSaml(view, "ON FORM RESUBMISSION");
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        checkIfPageIsSaml(view, "ON RECEIVED CLIENT CERT REQUEST");
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        checkIfPageIsSaml(view, "ON RECEIVED HTTP ERROR");
    }

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        //Prevents redirect from happening
//        return true;
//    }

    private void checkIfPageIsSaml(WebView view, String eventName) {
        view.evaluateJavascript("(function(){var el = document.getElementsByName(\"SAMLResponse\")[0]; return (el ? el.value : null);})()", new ValueCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceiveValue(String document) {
                logLongString("Event captured, " + eventName + ": Checking if page is SAML: " + document);
                if(!document.contains("null") && document != null) {
                    boolean isToken = isBase64WithQuotes(document);
                    if(isToken){
                        Log.i(TAG, "Page is a valid Encrypted SAML Assertion");
                        callbackContext.success(document);
                    } else {
                        Log.i(TAG, "Page is not valid SAML, skipping...");
                    }
                } else {
                    Log.i(TAG, "Page is null, skipping...");
                }
            }
        });
    }

    private boolean isBase64WithQuotes(String s){
        String pattern= "^[a-zA-Z0-9+=\\/\"]*";
        return s.matches(pattern);
    }

    private void logLongString(String str) {
        if(str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            logLongString(str.substring(4000));
        } else
            Log.i(TAG, str);
    }

}