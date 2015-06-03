/**
 * Created by rikimaru on 14.04.15.
 */

define(
    [
        "backbone",
        "jquery",
        "jquery-ui",
        "templates/battle/drop-field"
    ], function(Backbone, $, Ui, T_DropField) {

        function onStep(event, ui) {
            var $dragObj = ui.draggable;
            $dragObj.trigger("trigger", {name : "STEP"});
            this.$el.removeClass("on-card-over");
        }

        return Backbone.View.extend({

            initialize : function(options) {

                if (!options || !options.el) {
                    this.setElement(T_DropField());
                }

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
                this.$(".card-container").trigger("trigger", {name : "DELETE"});
            }

        });
    }
);