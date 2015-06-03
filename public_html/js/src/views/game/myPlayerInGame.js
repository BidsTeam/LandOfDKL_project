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
        "jquery",
        "sprintf"
    ], function(Backbone, CardViewClass, BattleModel, PlayerInGameViewsAbstractClass, CardAdditionalInfo, $, Sprintf) {

        function _showInfoForPlayer() {
            var effectList = this.model.get("effects");
            _.forEach(effectList,function(val,key) {
                effectList[key].description = sprintf(val.description,val.value,val.value)
            });
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