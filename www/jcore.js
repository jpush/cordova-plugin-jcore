var exec = require('cordova/exec')

exports.coolMethod = function (arg0, success, error) {
  exec(success, error, 'jcore', 'coolMethod', [arg0])
}
module.exports = {
     testCountryCode: function (code) {
            exec(null, null, "JCorePlugin", 'testCountryCode', [code]);
        },
        setAuth: function (enable) {
            exec(null, null, "JCorePlugin", 'setAuth', [enable]);
        },
         enableAutoWakeup: function (enable) {
            exec(null, null, "JCorePlugin", 'enableAutoWakeup', [enable]);
        }
}