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
                this.listenTo(this, "change:health", _onChangeHealth.bind(this));
                this.listenTo(Socket, "newGameState", this.updateHealth.bind(this));

                require(['models/game/battle'], function(BattleModel) {
                    this.listenTo(BattleModel, "END_BATTLE", this._onBattleEnd.bind(this));
                }.bind(this));

                this.cardsInHand = new cardCollectionClass();

                if (attrs['deck']) {
                        this.cardsInHand.add(attrs['deck']);
                }
                if (attrs['health']) {
                    this.set({startHealth : attrs['health']});
                }

                this.listenTo(this.cardsInHand, "delete", this._deleteCard.bind(this));
                this._construct(attrs);
            },

            updateHealth : function(msg) {
                var health = msg[this.get("type")].health;
                //this.set({health : health,effectList:[{"name":"poison",value:5,time:2,description:"blabla",type:"poison"}]});
                this.set({health : health,effectList:msg[this.get("type")].effectList});
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