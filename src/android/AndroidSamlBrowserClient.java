package com.adamweitzman.cordova.plugin;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidSamlBrowserClient extends WebViewClient {
    private final AndroidSamlBrowserDialog dialog;
    CordovaWebView webView;
    CallbackContext callbackContext;
    private String TAG = "AndroidSaml";
    AndroidSaml androidSaml;

    public AndroidSamlBrowserClient(CordovaWebView webView, CallbackContext callbackContext, AndroidSamlBrowserDialog dialog) {
        this.webView = webView;
        this.callbackContext = callbackContext;
        this.dialog = dialog;
    }

    // TODO: 3/5/20 i think i have the bearer token now, figure out how to get that back from the app

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:" + "(function(){var el = document.getElementsByName(\"SAMLResponse\")[0]; return (el ? el.value : null);})()" );
        //iew.evaluateJavascript("(function(){var el = document.getElementsByName(\"SAMLResponse\")[0]; return (el ? el.value : null);})()", null);

        view.evaluateJavascript("(function(){return window.document.body.innerHTML})();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.i(TAG, s);
                callbackContext.success(s);
                //dialog.dismiss();
                webView.handleDestroy();

            }
        });
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
        return true;
    }

//    public void closeDialog() {
//        this.cordova.getActivity().runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final WebView childView = AndroidSaml;
//                // The JS protects against multiple calls, so this should happen only when
//                // closeDialog() is called by other native code.
//                if (childView == null) {
//                    return;
//                }
//
//                childView.setWebViewClient(new WebViewClient() {
//                    // NB: wait for about:blank before dismissing
//                    public void onPageFinished(WebView view, String url) {
//                        if (dialog != null) {
//                            dialog.dismiss();
//                            dialog = null;
//                        }
//                    }
//                });
//                // NB: From SDK 19: "If you call methods on WebView from any thread
//                // other than your app's UI thread, it can cause unexpected results."
//                // http://developer.android.com/guide/webapps/migrating.html#Threads
//                childView.loadUrl("about:blank");
//
//                try {
//                    JSONObject obj = new JSONObject();
//                    obj.put("type", EXIT_EVENT);
//                    sendUpdate(obj, false);
//                } catch (JSONException ex) {
//                    LOG.d(LOG_TAG, "Should never happen");
//                }
//            }
//        });
//    }

}