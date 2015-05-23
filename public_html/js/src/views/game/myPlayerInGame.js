/**
 * Created by rikimaru on 23.05.15.
 */

define(
    [
        "backbone",
        "views/game/card",
        "models/game/battle",
        "views/game/playerInGame",
        "templates/card/card_additional_info",
        "jquery"
    ], function(Backbone, CardViewClass, BattleModel, PlayerInGameViewsAbstractClass, CardAdditionalInfo, $) {

        function _showInfoForPlayer() {
            $(".card-info-container_player").html(CardAdditionalInfo(this.model.toJSON()));
        }

        return PlayerInGameViewsAbstractClass.extend({

            _construct : function(options) {
                this.model.cardsInHand.bind("STEP", this._onStep, this);
            },

            _onAddCardToHand : function(model, view) {
                view.$el.on("mouseenter", _showInfoForPlayer.bind(view));
            },

            _onNextStepBegin : function() {
                for (var key in this.cardViews) {
                    this.cardViews[key].$el.draggable("enable");
                }
            },

            _onStep : function() {
                for (var key in this.cardViews) {
                    this.cardViews[key].$el.draggable("disable");
                }
            }
        });
    }
);