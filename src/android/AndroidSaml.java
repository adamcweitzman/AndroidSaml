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
public class AndroidSamlBrowser extends CordovaPlugin {

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

        //TOMMOROW: KEEP LOOKING FOR ways to compile time check, look back through commits to see how i got the webview going in the first place

        if (message != null && message.length() > 0) {
            String sendBack = message.concat("JAVA LAND");
            callbackContext.success(message);
        } else {
            callbackContext.error(message);
        }

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
                currentClient = new AndroidSamlBrowserClient(thatWebView);

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

                //cordova.getActivity().setContentView(myWebView);

                //inAppWebView.loadUrl("https://app.booksbnb.com/");
                //"https://configuat.devonway.com/configuat/YWServices/saml/IdpRedirect.jsp?sub=RGHCX77552"

                //OK
                //ESPN, GOOGLE, YAHOO

            }
        });
    }

//    public String showWebPage(final String url) {
//
//        final CordovaWebView thatWebView = this.webView;
//
//        // Create dialog in new thread
//        Runnable runnable = new Runnable() {
//            /**
//             * Convert our DIP units to Pixels
//             *
//             * @return int
//             */
//            private int dpToPixels(int dipValue) {
//                int value = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP,
//                        (float) dipValue,
//                        cordova.getActivity().getResources().getDisplayMetrics()
//                );
//
//                return value;
//            }
//
//            private View createCloseButton(int id){
//                View _close;
//                Resources activityRes = cordova.getActivity().getResources();
//
//                if (closeButtonCaption != "") {
//                    // Use TextView for text
//                    TextView close = new TextView(cordova.getActivity());
//                    close.setText(closeButtonCaption);
//                    close.setTextSize(20);
//                    if (closeButtonColor != "") close.setTextColor(android.graphics.Color.parseColor(closeButtonColor));
//                    close.setGravity(android.view.Gravity.CENTER_VERTICAL);
//                    close.setPadding(this.dpToPixels(10), 0, this.dpToPixels(10), 0);
//                    _close = close;
//                }
//                else {
//                    ImageButton close = new ImageButton(cordova.getActivity());
//                    int closeResId = activityRes.getIdentifier("ic_action_remove", "drawable", cordova.getActivity().getPackageName());
//                    Drawable closeIcon = activityRes.getDrawable(closeResId);
//                    if (closeButtonColor != "") close.setColorFilter(android.graphics.Color.parseColor(closeButtonColor));
//                    close.setImageDrawable(closeIcon);
//                    close.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    if (Build.VERSION.SDK_INT >= 16)
//                        close.getAdjustViewBounds();
//
//                    _close = close;
//                }
//
//                RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
//                if (leftToRight) closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                else closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                _close.setLayoutParams(closeLayoutParams);
//
//                if (Build.VERSION.SDK_INT >= 16)
//                    _close.setBackground(null);
//                else
//                    _close.setBackgroundDrawable(null);
//
//                _close.setContentDescription("Close Button");
//                _close.setId(Integer.valueOf(id));
//                _close.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        closeDialog();
//                    }
//                });
//
//                return _close;
//            }
//
//            @SuppressLint("NewApi")
//            public void run() {
//
//                // CB-6702 InAppBrowser hangs when opening more than one instance
//                if (dialog != null) {
//                    dialog.dismiss();
//                };
//
//                // Let's create the main dialog
//                dialog = new InAppBrowserDialog(cordova.getActivity(), android.R.style.Theme_NoTitleBar);
//                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                dialog.setCancelable(true);
//                dialog.setInAppBroswer(getInAppBrowser());
//
//                // Main container layout
//                LinearLayout main = new LinearLayout(cordova.getActivity());
//                main.setOrientation(LinearLayout.VERTICAL);
//
//                // Toolbar layout
//                RelativeLayout toolbar = new RelativeLayout(cordova.getActivity());
//                //Please, no more black!
//                toolbar.setBackgroundColor(toolbarColor);
//                toolbar.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, this.dpToPixels(44)));
//                toolbar.setPadding(this.dpToPixels(2), this.dpToPixels(2), this.dpToPixels(2), this.dpToPixels(2));
//                if (leftToRight) {
//                    toolbar.setHorizontalGravity(Gravity.LEFT);
//                } else {
//                    toolbar.setHorizontalGravity(Gravity.RIGHT);
//                }
//                toolbar.setVerticalGravity(Gravity.TOP);
//
//                // Action Button Container layout
//                RelativeLayout actionButtonContainer = new RelativeLayout(cordova.getActivity());
//                RelativeLayout.LayoutParams actionButtonLayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                if (leftToRight) actionButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                else actionButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                actionButtonContainer.setLayoutParams(actionButtonLayoutParams);
//                actionButtonContainer.setHorizontalGravity(Gravity.LEFT);
//                actionButtonContainer.setVerticalGravity(Gravity.CENTER_VERTICAL);
//                actionButtonContainer.setId(leftToRight ? Integer.valueOf(5) : Integer.valueOf(1));
//
//                // Back button
//                ImageButton back = new ImageButton(cordova.getActivity());
//                RelativeLayout.LayoutParams backLayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
//                backLayoutParams.addRule(RelativeLayout.ALIGN_LEFT);
//                back.setLayoutParams(backLayoutParams);
//                back.setContentDescription("Back Button");
//                back.setId(Integer.valueOf(2));
//                Resources activityRes = cordova.getActivity().getResources();
//                int backResId = activityRes.getIdentifier("ic_action_previous_item", "drawable", cordova.getActivity().getPackageName());
//                Drawable backIcon = activityRes.getDrawable(backResId);
//                if (navigationButtonColor != "") back.setColorFilter(android.graphics.Color.parseColor(navigationButtonColor));
//                if (Build.VERSION.SDK_INT >= 16)
//                    back.setBackground(null);
//                else
//                    back.setBackgroundDrawable(null);
//                back.setImageDrawable(backIcon);
//                back.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                back.setPadding(0, this.dpToPixels(10), 0, this.dpToPixels(10));
//                if (Build.VERSION.SDK_INT >= 16)
//                    back.getAdjustViewBounds();
//
//                back.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        goBack();
//                    }
//                });
//
//                // Forward button
//                ImageButton forward = new ImageButton(cordova.getActivity());
//                RelativeLayout.LayoutParams forwardLayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
//                forwardLayoutParams.addRule(RelativeLayout.RIGHT_OF, 2);
//                forward.setLayoutParams(forwardLayoutParams);
//                forward.setContentDescription("Forward Button");
//                forward.setId(Integer.valueOf(3));
//                int fwdResId = activityRes.getIdentifier("ic_action_next_item", "drawable", cordova.getActivity().getPackageName());
//                Drawable fwdIcon = activityRes.getDrawable(fwdResId);
//                if (navigationButtonColor != "") forward.setColorFilter(android.graphics.Color.parseColor(navigationButtonColor));
//                if (Build.VERSION.SDK_INT >= 16)
//                    forward.setBackground(null);
//                else
//                    forward.setBackgroundDrawable(null);
//                forward.setImageDrawable(fwdIcon);
//                forward.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                forward.setPadding(0, this.dpToPixels(10), 0, this.dpToPixels(10));
//                if (Build.VERSION.SDK_INT >= 16)
//                    forward.getAdjustViewBounds();
//
//                forward.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        goForward();
//                    }
//                });
//
//                // Edit Text Box
//                edittext = new EditText(cordova.getActivity());
//                RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                textLayoutParams.addRule(RelativeLayout.RIGHT_OF, 1);
//                textLayoutParams.addRule(RelativeLayout.LEFT_OF, 5);
//                edittext.setLayoutParams(textLayoutParams);
//                edittext.setId(Integer.valueOf(4));
//                edittext.setSingleLine(true);
//                edittext.setText(url);
//                edittext.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
//                edittext.setImeOptions(EditorInfo.IME_ACTION_GO);
//                edittext.setInputType(InputType.TYPE_NULL); // Will not except input... Makes the text NON-EDITABLE
//                edittext.setOnKeyListener(new View.OnKeyListener() {
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        // If the event is a key-down event on the "enter" button
//                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                            navigate(edittext.getText().toString());
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//
//
//                // Header Close/Done button
//                int closeButtonId = leftToRight ? 1 : 5;
//                View close = createCloseButton(closeButtonId);
//                toolbar.addView(close);
//
//                // Footer
//                RelativeLayout footer = new RelativeLayout(cordova.getActivity());
//                int _footerColor;
//                if(footerColor != ""){
//                    _footerColor = Color.parseColor(footerColor);
//                }else{
//                    _footerColor = android.graphics.Color.LTGRAY;
//                }
//                footer.setBackgroundColor(_footerColor);
//                RelativeLayout.LayoutParams footerLayout = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, this.dpToPixels(44));
//                footerLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                footer.setLayoutParams(footerLayout);
//                if (closeButtonCaption != "") footer.setPadding(this.dpToPixels(8), this.dpToPixels(8), this.dpToPixels(8), this.dpToPixels(8));
//                footer.setHorizontalGravity(Gravity.LEFT);
//                footer.setVerticalGravity(Gravity.BOTTOM);
//
//                View footerClose = createCloseButton(7);
//                footer.addView(footerClose);
//
//
//                // WebView
//                inAppWebView = new WebView(cordova.getActivity());
//                inAppWebView.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
//                inAppWebView.setId(Integer.valueOf(6));
//                // File Chooser Implemented ChromeClient
//                inAppWebView.setWebChromeClient(new InAppChromeClient(thatWebView) {
//                    // For Android 5.0+
//                    public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
//                    {
//                        LOG.d(LOG_TAG, "File Chooser 5.0+");
//                        // If callback exists, finish it.
//                        if(mUploadCallbackLollipop != null) {
//                            mUploadCallbackLollipop.onReceiveValue(null);
//                        }
//                        mUploadCallbackLollipop = filePathCallback;
//
//                        // Create File Chooser Intent
//                        Intent content = new Intent(Intent.ACTION_GET_CONTENT);
//                        content.addCategory(Intent.CATEGORY_OPENABLE);
//                        content.setType("*/*");
//
//                        // Run cordova startActivityForResult
//                        cordova.startActivityForResult(InAppBrowser.this, Intent.createChooser(content, "Select File"), FILECHOOSER_REQUESTCODE_LOLLIPOP);
//                        return true;
//                    }
//
//                    // For Android 4.1+
//                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
//                    {
//                        LOG.d(LOG_TAG, "File Chooser 4.1+");
//                        // Call file chooser for Android 3.0+
//                        openFileChooser(uploadMsg, acceptType);
//                    }
//
//                    // For Android 3.0+
//                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
//                    {
//                        LOG.d(LOG_TAG, "File Chooser 3.0+");
//                        mUploadCallback = uploadMsg;
//                        Intent content = new Intent(Intent.ACTION_GET_CONTENT);
//                        content.addCategory(Intent.CATEGORY_OPENABLE);
//
//                        // run startActivityForResult
//                        cordova.startActivityForResult(InAppBrowser.this, Intent.createChooser(content, "Select File"), FILECHOOSER_REQUESTCODE);
//                    }
//
//                });
//                currentClient = new InAppBrowser.InAppBrowserClient(thatWebView, edittext, beforeload);
//                inAppWebView.setWebViewClient(currentClient);
//                WebSettings settings = inAppWebView.getSettings();
//                settings.setJavaScriptEnabled(true);
//                settings.setJavaScriptCanOpenWindowsAutomatically(true);
//                settings.setBuiltInZoomControls(showZoomControls);
//                settings.setPluginState(android.webkit.WebSettings.PluginState.ON);
//
//                // Add postMessage interface
//                class JsObject {
//                    @JavascriptInterface
//                    public void postMessage(String data) {
//                        try {
//                            JSONObject obj = new JSONObject();
//                            obj.put("type", MESSAGE_EVENT);
//                            obj.put("data", new JSONObject(data));
//                            sendUpdate(obj, true);
//                        } catch (JSONException ex) {
//                            LOG.e(LOG_TAG, "data object passed to postMessage has caused a JSON error.");
//                        }
//                    }
//                }
//
//                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    settings.setMediaPlaybackRequiresUserGesture(mediaPlaybackRequiresUserGesture);
//                    inAppWebView.addJavascriptInterface(new JsObject(), "cordova_iab");
//                }
//
//                String overrideUserAgent = preferences.getString("OverrideUserAgent", null);
//                String appendUserAgent = preferences.getString("AppendUserAgent", null);
//
//                if (overrideUserAgent != null) {
//                    settings.setUserAgentString(overrideUserAgent);
//                }
//                if (appendUserAgent != null) {
//                    settings.setUserAgentString(settings.getUserAgentString() + appendUserAgent);
//                }
//
//                //Toggle whether this is enabled or not!
//                Bundle appSettings = cordova.getActivity().getIntent().getExtras();
//                boolean enableDatabase = appSettings == null ? true : appSettings.getBoolean("InAppBrowserStorageEnabled", true);
//                if (enableDatabase) {
//                    String databasePath = cordova.getActivity().getApplicationContext().getDir("inAppBrowserDB", Context.MODE_PRIVATE).getPath();
//                    settings.setDatabasePath(databasePath);
//                    settings.setDatabaseEnabled(true);
//                }
//                settings.setDomStorageEnabled(true);
//
//                if (clearAllCache) {
//                    CookieManager.getInstance().removeAllCookie();
//                } else if (clearSessionCache) {
//                    CookieManager.getInstance().removeSessionCookie();
//                }
//
//                // Enable Thirdparty Cookies on >=Android 5.0 device
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    CookieManager.getInstance().setAcceptThirdPartyCookies(inAppWebView,true);
//                }
//
//                inAppWebView.loadUrl(url);
//                inAppWebView.setId(Integer.valueOf(6));
//                inAppWebView.getSettings().setLoadWithOverviewMode(true);
//                inAppWebView.getSettings().setUseWideViewPort(useWideViewPort);
//                inAppWebView.requestFocus();
//                inAppWebView.requestFocusFromTouch();
//
//                // Add the back and forward buttons to our action button container layout
//                actionButtonContainer.addView(back);
//                actionButtonContainer.addView(forward);
//
//                // Add the views to our toolbar if they haven't been disabled
//                if (!hideNavigationButtons) toolbar.addView(actionButtonContainer);
//                if (!hideUrlBar) toolbar.addView(edittext);
//
//                // Don't add the toolbar if its been disabled
//                if (getShowLocationBar()) {
//                    // Add our toolbar to our main view/layout
//                    main.addView(toolbar);
//                }
//
//                // Add our webview to our main view/layout
//                RelativeLayout webViewLayout = new RelativeLayout(cordova.getActivity());
//                webViewLayout.addView(inAppWebView);
//                main.addView(webViewLayout);
//
//                // Don't add the footer unless it's been enabled
//                if (showFooter) {
//                    webViewLayout.addView(footer);
//                }
//
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//                dialog.setContentView(main);
//                dialog.show();
//                dialog.getWindow().setAttributes(lp);
//                // the goal of openhidden is to load the url and not display it
//                // Show() needs to be called to cause the URL to be loaded
//                if(openWindowHidden) {
//                    dialog.hide();
//                }
//            }
//        };
//        this.cordova.getActivity().runOnUiThread(runnable);
//        return "";
//    }


}



//plugin android saml is coming in but not recognizing the method