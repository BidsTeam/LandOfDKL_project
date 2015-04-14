/**
 * Created by rikimaru on 14.04.15.
 */

define(
    [
        "backbone",
        "jquery",
        "jquery-ui"
    ], function(Backbone, $, Ui) {

        return Backbone.View.extend({

            el : ".middle-field",

            initialize : function(options) {
                var coords = this.$el.position();
                this.$el.droppable({
                    activeClass : "underlight",
                    drop : function(event, ui) {
                        var $dragObj = ui.draggable;
                        $dragObj.css("position", "absolute");
                        $dragObj.animate({top: coords.top, left: coords.left}, "fast");
                        this.trigger("STEP");
                    }.bind(this),

                    activate : function() {
                    },

                    deactivate : function() {
                    },

                    out : function() {
                    },

                    over : function() {
                    }
                });
            }

        });
    }
);