package com.adamweitzman.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * This class echoes a string called from JavaScript.
 */
public class AndroidSaml extends CordovaPlugin {

    private WebView inAppWebView;
    private WebViewClient currentClient;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {

        //TOMMOROW: KEEP LOOKING FOR ways to compile time check, look back through commits to see how i got the webview going in the first place

        if (message != null && message.length() > 0) {
            String sendBack = message.concat("JAVA LAND");
            callbackContext.success(sendBack);
        } else {
            callbackContext.error(message);
        }

        Context context= this.cordova.getActivity().getApplicationContext();

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                WebView myWebView = new WebView(context);
                setContentView(myWebView);

//                WebSettings webSettings = mWebView.getSettings();
//                webSettings.setJavaScriptEnabled(true);

                // REMOTE RESOURCE
                // mWebView.loadUrl("https://example.com");
                // mWebView.setWebViewClient(new MyWebViewClient());

                // LOCAL RESOURCE
                mWebView.loadUrl("file:///android_asset/index.html");

                // REMOTE RESOURCE
                // mWebView.loadUrl("https://example.com");
                // mWebView.setWebViewClient(new MyWebViewClient());

                // LOCAL RESOURCE
//                mWebView.loadUrl("file:///android_asset/index.html");
//
//                Context context = this.cordova.getActivity().getApplicationContext();
//
//                WebView myWebView = new WebView(context);
//                setContentView(myWebView);
//
//                myWebView.loadUrl("https://www.google.com");
//
//
////                inAppWebView.setWebViewClient(currentClient);
//
//
//                myWebView.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));

//                setContentView(inAppWebView);

                //inAppWebView.loadUrl("file:///android_asset/index.html");


            }
        });
    }
}

//NEXT TIME -->
//plugin android saml is coming in but not recognizing the method