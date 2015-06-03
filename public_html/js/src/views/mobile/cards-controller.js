/**
 * Created by rikimaru on 30.05.15.
 */

define(
    [
        "backbone",
        "models/game/battle",
        "views/mobile/gamePage"
    ], function(Backbone, BattleModel, gamePageView) {

        var _animSpeed = 300;
        var scrollBlocked = false;

        return new (Backbone.View.extend({

            el : ".cards-mobile-controller",
            $cardsContainer : this.$(".action-field__cards-container"),
            $counterCards : this.$('.counter-field'),

            events : {
                "click .action-field__left-arrow" : "showPrevCard",
                "click .action-field__right-arrow" : "showNextCard"
            },

            initialize : function(options) {
            },

            showPrevCard : function() {
                if (!scrollBlocked) {
                    var currScroll = this.$cardsContainer.scrollLeft();
                    var width = this.$cardsContainer.width();
                    scrollBlocked = true;
                    this.$cardsContainer.animate({
                        scrollLeft : (currScroll - width) * 1 + "px"
                    }, _animSpeed, function() {
                        scrollBlocked = false;
                    });
                }
            },

            showNextCard : function() {
                if (!scrollBlocked) {
                    var currScroll = this.$cardsContainer.scrollLeft();
                    var width = this.$cardsContainer.width();
                    scrollBlocked = true;
                    this.$cardsContainer.animate({
                        scrollLeft: (currScroll + width) * 1 + "px"
                    }, _animSpeed, function () {
                        scrollBlocked = false;
                    });
                }
            },

            clear : function() {
                this.$cardsContainer.html("");
                this.$(".counter-field__curr-num-of-card").html("0");
                this.$(".counter-field__amount-num-of-card").html("0");
            }

        }))();
    }
);