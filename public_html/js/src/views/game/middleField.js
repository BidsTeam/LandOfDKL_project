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

            initialize : function() {
                var fieldCoords = this.$el.position();
                this.$el.droppable({

                    activeClass : "underlight",

                    drop : function(event, ui) {
                        var $dragObj = ui.draggable;
                        var cardType = $dragObj.attr("cardType");
                        $dragObj.css("position", "absolute");
                        $dragObj.animate({
                            top: fieldCoords.top,
                            left: fieldCoords.left
                        }, "fast");
                        $dragObj.trigger("step");
                        this.$el.removeClass("on-card-over");
                    }.bind(this),

                    activate : function() {
                    },

                    deactivate : function() {
                    },

                    out : function(event, ui) {
                        this.$el.removeClass("on-card-over");
                        ui.draggable.attr("prepareToDrop", "0");
                    }.bind(this),

                    over : function(event, ui) {
                        ui.draggable.attr("prepareToDrop", "1");
                        this.$el.addClass("on-card-over");
                    }.bind(this)
                });
            }

        });
    }
);