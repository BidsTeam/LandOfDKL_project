/**
 * Created by rikimaru on 23.05.15.
 */

define(
    [
        "backbone",
        "views/game/card",
        "models/game/battle",
        "views/game/playerInGame",
        "jquery"
    ], function(Backbone, CardViewClass, BattleModel, PlayerInGameViewsAbstractClass, $) {

        return PlayerInGameViewsAbstractClass.extend({
            _construct : function(options) {
                this.model.bind("STEP", this._onStep, this);
            },

            _onAddCardToHand : function(model, view) {
                view.$el.removeClass("card-container_highlight");
            },

            _onStep : function() {
                var opponentCard = this.cardViews.shift();
                opponentCard.replaceToDOMElem($(".step-place_opponent"), true);
            }
        });
    }
);