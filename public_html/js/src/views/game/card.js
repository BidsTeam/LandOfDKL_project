/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/game/card",
        "templates/card/card",
        "templates/card/card_general_info",
        "collections/socketsPool",
        "lodash",
        "jquery-touch"
    ], function(Backbone, Ui, $, CardModel, CardTemplate, CardInfo, socketsPool, _, Touch) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        var handlers = {

            "STEP" : function() {
                this.model.trigger("STEP", this.model);

                this.replaceToDOMElem(
                    $(".step-place_player"),
                    true,
                    function() {
                        this._clearStyles();
                    }.bind(this)
                );
            },

            "DELETE" : function() {
                this.model.trigger("delete", this.model);
                this.$el.remove();
            }
        };

        function _handleTrigger(event, options) {
            if (options) {
                if (options.name) {
                    this.trigger(options.name);
                    if (handlers[options.name]) {
                        if (!options.args) {
                            options.args = {};
                        }
                        handlers[options.name].call(this, options.args);
                    }
                }
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
                this.$el.on("trigger", _handleTrigger.bind(this));

                if (options.draggable !== false) {
                    this.$el.draggable({

                        scroll : false,

                        start : function(event, ui) {

                            this.$el
                                .addClass("inDragPosition")
                                .removeClass("softAnimate")
                                .css(
                                {
                                    height : this.$el.height(),
                                    width : this.$el.width(),
                                    "margin-left" : this.$el.css("margin-left")
                                });

                            this.startPosition = this.$el.position();

                            this.$el.css(_.assign(
                                    {position : "absolute"},
                                    this.startPosition
                                ));

                        }.bind(this),

                        stop : function(event, ui) {
                            this.$el.removeClass("inDragPosition");
                            if (this.$el.attr("prepareToDrop") == 0 || this.$el.attr("prepareToDrop") == undefined) {
                                this.returnToStartPosition(function() {
                                    this.$el.addClass("card-container_highlight");
                                    setTimeout(function(){
                                        this.$el.addClass("softAnimate")
                                    }.bind(this), 300);
                                }.bind(this));
                            }
                        }.bind(this),

                        drag : function(event, ui) {
                            ui.position = {
                                top : ui.position.top + this.startPosition.top,
                                left : ui.position.left + this.startPosition.left
                            };
                        }.bind(this)

                    });
                }

            },

            render : function() {
                var $cardContentHtml = $(CardInfo(
                    this.model.toJSON()
                ));
                this.$el.html($cardContentHtml);
            },

            returnToStartPosition : function(callback) {
                this.replaceToPosition({
                        top : this.startPosition.top,
                        left : this.startPosition.left
                    }, true, function() {
                        this._clearStyles();
                        if (callback) {
                            callback();
                        }

                    }.bind(this)
                );
            },

            replaceToPosition : function(positionObj, isAnimate, callback) {
                this.$el.css("position", "absolute");
                if (isAnimate) {
                    this.$el.animate(positionObj, "fast", callback);
                } else {
                    this.$el.css(positionObj);
                    callback();
                }
            },

            _clearStyles : function() {
                this.$el
                    .css({
                        height : "",
                        width : "",
                        top : "",
                        left : "",
                        position : "",
                        "margin-left" : ""
                    });
            },

            replaceToDOMElem : function($elemTo, isAnimate, callback) {

                this.$el.css(
                    {
                        height : this.$el.height(),
                        width : this.$el.width()
                    }
                );

                if (isAnimate) {

                    var position = this.$el.position();

                    this.$el
                        .detach()
                        .appendTo($elemTo)
                        .css({
                            position : "relative",
                            top : 0,
                            left : 0
                        });

                    var newPosition = this.$el.position();

                    this.$el
                        .css(
                        {
                            position : "absolute",
                            top : position.top,
                            left : position.left
                        })
                        .animate({

                            top: newPosition.top,
                            left: newPosition.left

                        }, 300, "swing", function() {

                            this._clearStyles();
                            if (callback) {
                                callback();
                            }

                        }.bind(this)
                    );

                } else {
                    this.$el
                        .detach()
                        .appendTo($elemTo)
                }
            }
        });
    }
);