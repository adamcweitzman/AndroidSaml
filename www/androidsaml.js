// Empty constructor
function AndroidSaml() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
AndroidSaml.prototype.echo = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'AndroidSaml', 'echo', [options]);
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