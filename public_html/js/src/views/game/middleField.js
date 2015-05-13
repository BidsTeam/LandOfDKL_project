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
            var $dragObjContainer = $dragObj.parent();
            var position = $dragObj.position();
            var newPosition;

            $dragObj.detach().appendTo(this.$el);
            $dragObjContainer.remove();

            $dragObj.css({
                position : "relative",
                top : 0,
                left : 0
            });

            newPosition = $dragObj.position();

            $dragObj.css({
                position : "absolute",
                top : position.top,
                left : position.left
            });

            $dragObj.animate(
                {
                    top: newPosition.top,
                    left: newPosition.left
                },
                300,
                "swing",
                function() {
                    $dragObj.css({
                        position : "relative",
                        top : 0,
                        left : 0
                    });
                }
            );

            $dragObj.trigger("step");
            this.$el.removeClass("on-card-over");
        }

        return Backbone.View.extend({

            initialize : function() {

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
            },

            clear : function() {
                this.$(".card-container").trigger("delete");
                this.$el.html("");
            }

        });
    }
);