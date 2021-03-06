// Empty constructor
function AndroidSaml() {};

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
AndroidSaml.echo = function(message, successCallback, errorCallback) {
  var options = {};
  options.message = message;

  cordova.exec(successCallback, errorCallback, 'AndroidSaml', 'echo', [options]);

  return 'RETURNED FROM PLUGIN';
}

AndroidSaml.close = function(successCallback, errorCallback) {
  var options = {};

  cordova.exec(successCallback, errorCallback, 'AndroidSaml', 'close', [options]);

  return 'RETURNED FROM PLUGIN';
}



// Installation constructor that binds ToastyPlugin to window
AndroidSaml.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.androidSaml = new AndroidSaml();
  return window.plugins.anroidSaml;
};

cordova.addConstructor(AndroidSaml.install);
module.exports = AndroidSaml;