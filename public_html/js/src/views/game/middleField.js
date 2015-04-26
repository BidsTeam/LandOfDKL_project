/**
 * Created by rikimaru on 14.04.15.
 */

define(
    [
        "backbone",
        "jquery",
        "jquery-ui"
    ], function(Backbone, $, Ui) {

        function onStep(event, ui) {
            var $dragObj = ui.draggable;

            var position = $dragObj.position();
            $dragObj.detach().appendTo(this.$el);
            $dragObj.css("position", "relative");
            $dragObj.css("top", 0);
            $dragObj.css("left", 0);
            var newPosition = $dragObj.position();

            $dragObj.css("position", "absolute");
            $dragObj.css("top", position.top);
            $dragObj.css("left", position.left);

            $dragObj.animate(
                {
                    top: newPosition.top,
                    left: newPosition.left
                },
                300,
                "swing",
                function() {
                    $dragObj.css("position", "relative");
                    $dragObj.css("top", 0);
                    $dragObj.css("left", 0);
                }
            );

            $dragObj.trigger("step");
            this.$el.removeClass("on-card-over");
        }

        return Backbone.View.extend({

            initialize : function() {
                var fieldCoords = this.$el.position();

                this.$el.droppable({

                    activeClass : "underlight",
                    drop : onStep.bind(this),

                    out : function(event, ui) {
                        ui.draggable.attr("prepareToDrop", "0");
                        this.$el.removeClass("on-card-over");
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