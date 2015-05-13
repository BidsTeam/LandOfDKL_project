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

                if (!options.model) {
                    this.model = new CardModel({cardId : options.cardId});
                }
                this.model.bind("change", this.update, this);
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

                        this.placePosition = $elem.position();

                        $tempContainer = $elem.wrap("<div class='temp-container'>").parent();
                        $tempContainer.css({
                            "min-height" : $tempContainer.height(),
                            "min-width" : $tempContainer.width(),
                            padding : 0,
                            margin : 0
                        });

                        $elem.css($.extend({}, {position : "absolute"}, this.placePosition));
                    }.bind(this),

                    stop : function(event, ui) {
                        var $elem = ui.helper;
                        if ($elem.attr("prepareToDrop") == 0 || $elem.attr("prepareToDrop") == undefined) {
                            $elem.animate({
                                top : this.placePosition.top,
                                left : this.placePosition.left
                            }, "fast", function() {
                                $elem.unwrap();
                                $elem.css({position : "relative", top : 0, left : 0});
                            });
                        }
                    }.bind(this),

                    drag : function(event, ui) {
                        ui.position = {
                            top : ui.position.top + this.placePosition.top,
                            left : ui.position.left + this.placePosition.left
                        };
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