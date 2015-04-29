/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/game/card",
        "templates/card",
        "templates/card_info"
    ], function(Backbone, Ui, $, CardModel, CardTemplate, CardInfo) {

        return Backbone.View.extend({

            type : "",
            placePosition : {},

            initialize : function(options) {
                var $htmlEl;

                this.model.bind("change", this.update, this);

                if (!options.model) {
                    this.model = new CardModel({cardId : options.cardId});
                }
                this.type = this.model.get("cardType");

                $htmlEl = $(CardTemplate(
                    this.model.toJSON()
                ));

                this.setElement($htmlEl);

                this.$el.on("step", function(e) {
                    this.model.trigger("MY_STEP", this.model);
                    this.$el.draggable("disable");
                }.bind(this));

                this.$el.on("delete", function(e) {
                    this.model.trigger("delete", this.model);
                }.bind(this));

                this.$el.draggable({

                    scroll : false,

                    start : function(event, ui) {
                        var $elem = ui.helper;
                        var top = $elem.css("top");
                        top = (top === "auto") ? 0 : top;
                        var left = $elem.css("left");
                        left = (left === "auto") ? 0 : left;
                        this.placePosition = {
                            top : top,
                            left : left
                        };
                    }.bind(this),

                    stop : function(event, ui) {
                        var $elem = ui.helper;
                        if ($elem.attr("prepareToDrop") == 0 || $elem.attr("prepareToDrop") == undefined) {
                            $elem.animate({
                                top : this.placePosition.top,
                                left : this.placePosition.left
                            }, "fast");
                        }
                    }.bind(this)

                });
            },

            update : function() {
                $htmlEl = $(CardInfo(
                    this.model.toJSON()
                ));

                this.$el.html($htmlEl);
                this.$el.attr("cardType", this.model.get("cardType"));
            }
        });
    }
);