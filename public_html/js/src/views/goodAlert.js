/**
 * Created by rikimaru on 30.04.15.
 */

define(
    [
        "backbone",
        "templates/goodAlert",
        "jquery"
    ], function(Backbone, goodAlertTmpl, $) {

        var effects = {

            fadeFromTop : function() {
                $elem = this.$(".alert__alert-box");
                $elem.addClass("alert-box_on-top");
                this.show();
                setTimeout(function() {
                    $elem.addClass("softAnimate")
                        .removeClass("alert-box_on-top");
                }, 10);
            }

        };

        return new (Backbone.View.extend({

            events : {
                "click .ok" : "hide"
            },

            initialize : function(options) {
                var data = {
                    text : ""
                };

                if (options) {
                    if (options.text) {
                        data.text = options.text;
                    }
                }

                this.setElement(goodAlertTmpl(data));
                $("body").append(this.$el);
            },

            alert : function(text, options) {
                var effect = "";
                if (options) {
                    if (options.effect) {
                        effect = options.effect;
                    }
                    if (options.textClass) {
                        this.$(".alert-box__text").addClass(options.textClass);
                    }
                }


                this.$(".alert-box__text").html(text);
                if (effect) {
                    effects[effect].call(this);
                } else {
                    this.show();
                }

            },

            show : function() {
                this.$el.css("visibility", "visible");
            },

            hide : function(options) {
                this.$(".alert__alert-box").removeClass("softAnimate");
                this.$el.css("visibility", "hidden"); //todo ...и скрытия
            }

        }))();
    }
);