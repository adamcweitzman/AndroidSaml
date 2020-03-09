package com.adamweitzman.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This class echoes a string called from JavaScript.
 */
public class AndroidSaml extends CordovaPlugin {

    private AndroidSamlBrowserDialog dialog;
    private WebView inAppWebView;
    private WebChromeClient chromeClient;
    private WebViewClient currentClient;
    private CordovaWebView cordovaWebView;
    private String TAG = "AndroidSaml";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            Log.i(TAG, message);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private int getAppResource(String name, String type) {
        return cordova.getActivity().getResources().getIdentifier(name, type, cordova.getActivity().getPackageName());
    }

    private void echo(String message, CallbackContext callbackContext) {

//        if (message != null && message.length() > 0) {
//            String sendBack = message.concat("JAVA LAND");
//            callbackContext.success(message);
//        } else {
//            callbackContext.error(message);
//        }

        Context context= this.cordova.getActivity().getApplicationContext();

        dialog = new AndroidSamlBrowserDialog(cordova.getActivity(), android.R.style.Theme_NoTitleBar);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(true);
        dialog.setInAndroidSaml(this);

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayout main = new LinearLayout(cordova.getActivity());
                main.setOrientation(LinearLayout.VERTICAL);

                final CordovaWebView thatWebView = webView;
                currentClient = new AndroidSamlBrowserClient(thatWebView, callbackContext, dialog);

                WebView myWebView = new WebView(cordova.getActivity());
                myWebView.setWebViewClient(currentClient);

                myWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                myWebView.setId(Integer.valueOf(6));

                WebSettings settings = myWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setBuiltInZoomControls(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);

//                RelativeLayout webViewLayout = new RelativeLayout(cordova.getActivity());
//                webViewLayout.addView(myWebView);
//                main.addView(webViewLayout);

                String url = message.replaceAll("\\\\|\\{|\\}|\"|message", "").replaceFirst(":", "");
                Log.i(TAG, url);

                //cordova.getActivity().setContentView(myWebView);

                myWebView.loadUrl(url);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);
                myWebView.requestFocus();
                myWebView.requestFocusFromTouch();

                RelativeLayout webViewLayout = new RelativeLayout(cordova.getActivity());
                webViewLayout.addView(myWebView);
                main.addView(webViewLayout);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.setContentView(main);
                dialog.show();
                dialog.getWindow().setAttributes(lp);



            }
        });
    }
}
