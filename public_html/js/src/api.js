/**
 * Created by rikimaru on 29.04.15.
 */

define(
    [
        "jquery"
    ],function($) {

        var API = function API(baseUrl) {
            this.baseUrl = baseUrl || "/";
        };

        function _send(type, url, data) {
            return new Promise(function(resolve, reject) {
                $.ajax({
                    url : url,
                    data : data,
                    type : type,
                    success : function(msg) {
                        resolve(msg);
                    },
                    error : function(msg) {
                        reject(msg);
                    }
                })
            });
        }

        API.prototype.apiRequest = function(type, url, data) {
            var sendUrl = this.baseUrl + url;
            return _send(type, sendUrl, data);
        };

        return API;
});