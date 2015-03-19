/**
 * Created by rikimaru on 08.03.15.
 */
define(function() {

    var Templater = function() {
        var self = this;

        this.getTemplate = function(templateName, args) {
            var resHtml = "";
            require(["../templates/"+templateName], function(tmpl) {
                resHtml = tmpl(args);
            });
            return resHtml;
        }
    };

    return new Templater();
});