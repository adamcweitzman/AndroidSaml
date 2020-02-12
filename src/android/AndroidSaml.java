package com.adamweitzman.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.webkit.WebView;

/**
* This class echoes a string called from JavaScript.
*/
public class AndroidSaml extends CordovaPlugin {
    
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

        WebView myWebView = new WebView(activityContext);
        setContentView(myWebView);

        myWebView.loadUrl("https://www.google.com");

        callbackContext.success(message + "coming from android");
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