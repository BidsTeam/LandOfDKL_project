/**
 * Created by rikimaru on 19.03.15.
 */

define(
    [
        "backbone",
        "jquery"
    ],
    function(Backbone, $) {

        var Paginator = Backbone.View.extend({
            el : "#main-container",

            appendPage : function(pageHtml) {
                var page = $(pageHtml);
                this.$el.append(page);
                return $(page).attr("id");
            },

            showPage : function(pageId) {
                $("#"+pageId).addClass("active");
            },

            changePage : function(pageId) {
                this.$el.find("div[data-role=page].active").removeClass("active");
                $("#"+pageId).addClass("active");
            },

            hidePage : function(pageId) {
                $("#"+pageId).removeClass("active");
            }
        });

        return new Paginator();
    }
);