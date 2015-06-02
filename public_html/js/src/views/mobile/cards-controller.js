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

        return new (Backbone.View.extend({

            el : ".cards-mobile-controller",
            events : {
                "click .action-field__left-arrow" : "showPrevCard",
                "click .action-field__right-arrow" : "showNextCard"
            },

            $cardsContainer : this.$(".action-field__cards-container"),
            $counterCards : this.$('.counter-field'),

            initialize : function() {

            },

            showPrevCard : function() {
                var currScroll = this.$cardsContainer.scrollLeft();
                var width = this.$cardsContainer.width();
                this.$cardsContainer.animate({
                    scrollLeft : (currScroll-width)*1+"px"
                }, _animSpeed);
            },

            showNextCard : function() {
                var currScroll = this.$cardsContainer.scrollLeft();
                var width = this.$cardsContainer.width();
                this.$cardsContainer.animate({
                    scrollLeft : (currScroll+width)*1+"px"
                }, _animSpeed);
            }

        }))();
    }
);