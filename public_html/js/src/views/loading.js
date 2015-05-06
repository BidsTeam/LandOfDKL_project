/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone"
    ],
    function(Backbone) {

        return new (Backbone.View.extend({

            el : "#loader-screen",
            showLoaderTimerId : 0,

            initialize : function(options) {},

            show : function() {
                this.$el.css("visibility", "visible");
            },

            hide : function() {
                this.$el.css("visibility", "hidden");
            },

            isOpened : function() {
                return this.$el.css("visibility") == "visible";
            },

            showAfterTimeout : function(secondsTimeout) {
                this.showLoaderTimerId = setTimeout(this.show.bind(this), 2000);
            },

            clearTimeoutAndCloseIfOpened : function() {
                clearTimeout(this.showLoaderTimerId);
                this.hide();
            }
        }))();
    }
);