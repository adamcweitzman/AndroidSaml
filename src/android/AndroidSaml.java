package com.adamweitzman.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                WebView myWebView = new WebView(activityContext);
//                setContentView(myWebView);
                inAppWebView.setWebViewClient(currentClient);


                inAppWebView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                setContentView(myWebView);

                inAppWebView.loadUrl("file:///android_asset/index.html");

//            myWebView.loadUrl("https://www.google.com");
//
//
//                    super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            mWebView = findViewById(R.id.activity_main_webview);
//        mWebView.setWebViewClient(new WebViewClient());
//            WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

                // REMOTE RESOURCE
                // mWebView.loadUrl("https://example.com");
                // mWebView.setWebViewClient(new MyWebViewClient());

                // LOCAL RESOURCE








//            @Override
//            public void run() {
//
//
//
//
//
//
//
//
//                currentClient.waitForBeforeload = false;
//                inAppWebView.setWebViewClient(currentClient);
//
//                // ((InAppBrowserClient)inAppWebView.getWebViewClient()).waitForBeforeload = false;
//
//                // inAppWebView.loadUrl(url);
            }
        });



        // myWebView.loadUrl("https://www.google.com");

        // callbackContext.success(message + "coming from android");
        // if (message != null && message.length() > 0) {
        //     callbackContext.success(message + "coming from android");
        // } else {
        //     callbackContext.error("Expected one non-empty string argument.");
        // }

        //create new web view
        //create listener to http requests (redirects specifically) and return null to not redirect
        //open to pased in url
        //when url loads, pass the data found in the form back to the ui
        //ui call the success function
    }
}

//NEXT TIME -->
//plugin android saml is coming in but not recognizing the method