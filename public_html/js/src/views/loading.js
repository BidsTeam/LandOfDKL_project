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

            initialize : function(options) {},

            show : function() {
                this.$el.css("visibility", "visible");
            },

            hide : function() {
                this.$el.css("visibility", "hidden");
            }
        }))();
    }
);