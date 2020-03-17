package com.adamweitzman.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AndroidSaml extends CordovaPlugin {

    private AndroidSamlBrowserDialog dialog;
    private WebView myWebView;
    private WebChromeClient chromeClient;
    private WebViewClient currentClient;
    private CordovaWebView cordovaWebView;
    private String TAG = "AndroidSaml";
    private CallbackContext callbackContext;
    private static final String EXIT_EVENT = "exit";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            Log.i(TAG, message);
            this.echo(message, callbackContext);
            return true;
        } else if (action.equals("close")) {
            closeDialog();
        }
        return true;
    }

    private void echo(String message, CallbackContext callbackContext) {

        AndroidSaml context = this;
        this.cordova.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //Create dialog
                dialog = new AndroidSamlBrowserDialog(cordova.getActivity(), android.R.style.Theme_NoTitleBar);
                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setCancelable(true);
                dialog.setInAndroidSaml(context);

                LinearLayout main = new LinearLayout(cordova.getActivity());
                main.setOrientation(LinearLayout.VERTICAL);

                final CordovaWebView thatWebView = webView;
                currentClient = new AndroidSamlBrowserClient(thatWebView, callbackContext, dialog);

                //Create webview
                myWebView = new WebView(cordova.getActivity());
                myWebView.setWebViewClient(currentClient);

                myWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                myWebView.setId(Integer.valueOf(6));

                WebSettings settings = myWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setBuiltInZoomControls(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);

                //URL decode
                String url = message.replaceAll("\\\\|\\{|\\}|\"|message|", "").replaceFirst(":", "");
                Log.i(TAG, url);

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

    public void closeDialog() {
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Closing webview...");
                final WebView childView = myWebView;

                if (childView == null) {
                    return;
                }

                childView.setWebViewClient(new WebViewClient() {
                    // NB: wait for about:blank before dismissing
                    public void onPageFinished(WebView view, String url) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                });

                //SHOULD NOT HAPPEN: if child view dialog is null, load a blank page and continue running
                childView.loadUrl("about:blank");
            }
        });
    }

}
