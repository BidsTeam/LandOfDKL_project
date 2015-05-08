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

        function _onStep() {
            this.model.trigger("MY_STEP", this.model);
            this.replaceToDOMElem($(".middle-field"), true);
        }

        function _onDelete() {
            this.model.trigger("delete", this.model);
        }

        function _unwrapAndReplace($elem, $replaceTo) {
            var $parent = $elem.parent();
            $elem.detach().appendTo($replaceTo);
            if ($parent.hasClass("temp-container")) {
                $parent.remove();
            }
        }

        return Backbone.View.extend({

            startPosition : {},

            initialize : function(options) {
                if (!options.model) {
                    this.model = new CardModel({cardId : options.cardId});
                }

                var $htmlEl = $(CardTemplate(
                    this.model.toJSON()
                ));

                this.setElement($htmlEl);

                this.model.bind("change", this.render, this);
                this.$el.on("step", _onStep.bind(this));
                this.$el.on("dblclick", _onStep.bind(this));
                this.$el.on("delete", _onDelete.bind(this));

                this.$el.draggable({

                    scroll : false,

                    start : function(event, ui) {
                        this.startPosition = this.$el.position();
                        var $tempContainer = this.$el.wrap("<div class='temp-container'>").parent();
                        $tempContainer.css({
                            "min-height" : $tempContainer.height(),
                            "min-width" : $tempContainer.width(),
                            padding : 0,
                            margin : 0
                        });
                        this.$el.css($.extend({}, {position : "absolute"}, this.startPosition));
                    }.bind(this),

                    stop : function(event, ui) {
                        if (this.$el.attr("prepareToDrop") == 0 || this.$el.attr("prepareToDrop") == undefined) {
                            this.returnToStartPosition();
                        }
                    }.bind(this),

                    drag : function(event, ui) {
                        ui.position = {
                            top : ui.position.top + this.startPosition.top,
                            left : ui.position.left + this.startPosition.left
                        };
                    }.bind(this)

                });

            },

            render : function() {
                var $cardContentHtml = $(CardInfo(
                    this.model.toJSON()
                ));
                this.$el.html($cardContentHtml);
            },

            returnToStartPosition : function() {
                this.replaceToPosition({
                        top : this.startPosition.top,
                        left : this.startPosition.left
                    }, true, function() {
                        this.$el.unwrap();
                        this.$el.css({position : "relative", top : 0, left : 0});
                    }.bind(this)
                );
            },

            replaceToPosition : function(positionObj, isAnimate, callback) {
                if (isAnimate) {
                    this.$el.animate(positionObj, "fast", callback);
                } else {
                    this.$el.css(positionObj);
                    callback();
                }
            },

            replaceToDOMElem : function($elemTo, isAnimate, callback) {
                if (isAnimate) {
                    var position = this.$el.position();

                    _unwrapAndReplace(this.$el, $elemTo);

                    this.$el.css({
                        position : "relative",
                        top : 0,
                        left : 0
                    });
                    var newPosition = this.$el.position();
                    this.$el.css({
                        position : "absolute",
                        top : position.top,
                        left : position.left
                    });

                    this.$el.animate({
                            top: newPosition.top,
                            left: newPosition.left
                        }, 300, "swing", function() {
                            this.$el.css({
                                position : "relative",
                                top : 0,
                                left : 0
                            });
                            if (callback) {
                                callback();
                            }
                        }.bind(this)
                    );

                } else {
                    _unwrapAndReplace(this.$el, $elemTo);
                }
            }
        });
    }
);