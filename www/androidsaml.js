// Empty constructor
function AndroidSamlBrowser() {};

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
AndroidSamlBrowser.echo = function(message, successCallback, errorCallback) {
  var options = {};
  options.message = message;

  cordova.exec(successCallback, errorCallback, 'AndroidSamlBrowser', 'echo', [options]);

  return 'RETURNED FROM PLUGIN';
}


// Installation constructor that binds ToastyPlugin to window
AndroidSamlBrowser.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.androidSaml = new AndroidSamlBrowser();
  return window.plugins.anroidSaml;
};

cordova.addConstructor(AndroidSamlBrowser.install);
module.exports = AndroidSamlBrowser;