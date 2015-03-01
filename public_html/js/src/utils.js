var Util = {
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
    }

}