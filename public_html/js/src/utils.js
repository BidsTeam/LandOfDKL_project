define("utils", ['lodash'], function(_) {

    return {

        templateFromUrl : function (url, data, settings) {
            var templateHtml = "";
            this.cache = this.cache || {};
            if (this.cache[url]) {
                templateHtml = this.cache[url];
            } else {
                $.ajax({
                    url: url,
                    method: "GET",
                    async: false, // Говнокод, буду решать
                    beforeSend: function (xhr) {
                        xhr.overrideMimeType("text/plain; charset=utf-8"); // без этого, он попадает в error
                    },
                    success: function (data) {
                        templateHtml = data;
                    },
                    error: function (data) {
                        console.log("ERROR templateFromUrl");
                    }
                });
                this.cache[url] = templateHtml;
            }
            return _.template(templateHtml, data, settings);
        },

        isValidString: function isValid(str){
            return !/[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/g.test(str) && str != "";
        },

        isMobileDevice : function() {
            var deviceDetector = {
                Android: function() {
                    return navigator.userAgent.match(/Android/i);
                },
                BlackBerry: function() {
                    return navigator.userAgent.match(/BlackBerry/i);
                },
                iOS: function() {
                    return navigator.userAgent.match(/iPhone|iPad|iPod/i);
                },
                Opera: function() {
                    return navigator.userAgent.match(/Opera Mini/i);
                },
                Windows: function() {
                    return navigator.userAgent.match(/IEMobile/i);
                },
                any: function() {
                    return (this.Android() || this.BlackBerry() || this.iOS() || this.Opera() || this.Windows());
                }
            };
            return deviceDetector.any();
        }

    };
});