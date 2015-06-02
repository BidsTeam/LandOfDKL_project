/**
 * Created by rikimaru on 21.05.15.
 */

define(
    [
        "backbone",
        "collections/socketsPool",
        "collections/cardCollection"
    ], function(Backbone, socketsPool, cardCollectionClass) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        function _onChangeHealth() {
            if (this.get("health") <= 0) {
                this.trigger("HEALTH_END");
            }
        }

        function _sendCardStepAction(cardIndex) {
            var request = JSON.stringify({
                action : "gameAction",
                gameAction : "setCard",
                chosenCard : cardIndex
            });
            Socket.send(request);
        }

        return Backbone.Model.extend({

             defaults : {
                 name : "",
                 startHealth : 20,
                 health : 0,
                 type : ""
             },

            cardsInHand : {},

            initialize : function(attrs) {
                this.bind("change:health", _onChangeHealth.bind(this));
                Socket.bind("newGameState", this.updateHealth, this);

                require(['models/game/battle'], function(BattleModel) {
                    BattleModel.bind("END_BATTLE", this._onBattleEnd, this);
                }.bind(this));

                this.cardsInHand = new cardCollectionClass();

                if (attrs['deck']) {
                        this.cardsInHand.add(attrs['deck']);
                }

                this.cardsInHand.bind("delete", this._deleteCard, this);
                this._construct(attrs);
            },

            updateHealth : function(msg) {
                var health = msg[this.get("type")+"Health"];
                this.set({health : health});
            },

            _sendCardStep : function(cardModel) {
                var indexOfCard = _.findIndex(this.cardsInHand.models, function(card) {
                    return cardModel.cid == card.cid;
                });
                _sendCardStepAction(indexOfCard);
            },

            _deleteCard : function(cardModel) {
                cardModel.set({deleted : true});
            },

            _onBattleEnd : function() {
                this.stopListening();
                this.off();
                this.cardsInHand.stopListening();
                this.cardsInHand.off();
            }
        });
    }
);