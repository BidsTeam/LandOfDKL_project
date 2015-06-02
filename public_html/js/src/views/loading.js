/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone"
    ],
    function(Backbone) {

        return new (Backbone.View.extend({

            events : {
                "click .cancel-button" : "cancel"
            },

            el : "#loader-screen",
            showLoaderTimerId : 0,

            initialize : function(options) {},

            show : function(options) {

                if (options) {

                    if (options.cancelHandler) {
                        this.cancelHandler = options.cancelHandler;
                        this.$(".cancel-button").css("visibility", "visible");
                    }

                    if (options.showCancelButton === false) {
                        this.$(".cancel-button").css("visibility", "hidden");
                    }

                }

                this.$el.css("visibility", "visible");
            },

            hide : function() {
                this.$el.css("visibility", "hidden");
                this.cancelHandler = function(){};
            },

            isOpened : function() {
                return this.$el.css("visibility") == "visible";
            },

            showAfterTimeout : function(secondsTimeout, attrs) {
                this.showLoaderTimerId = setTimeout(this.show.bind(this, attrs), secondsTimeout);
            },

            clearTimeoutAndCloseIfOpened : function() {
                clearTimeout(this.showLoaderTimerId);
                this.hide();
            },

            cancel : function(options) {
                this.cancelHandler(options);
                this.hide();
            }
        }))();
    }
);