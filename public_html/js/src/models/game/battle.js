  /**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/socket",
        "models/game/cardCollection"
    ], function(Backbone, Socket, cardCollection) {


        function sendAction(cardIndex) {
            var request = JSON.stringify({
                action : "gameAction",
                gameAction : "setCard",
                chosenCard : cardIndex
            });
            console.log(request);
            Socket.send(request);
        }

        var battleModel = new (Backbone.Model.extend({

            cardsInHand : new cardCollection(),
            cardsInField : new cardCollection(),
            cardsInOpponentHand : new cardCollection(),

            initialize : function(options) {
                Socket.bind("game_action_set", this.setCard, this);
                Socket.bind("gameCardsReveal", this.revealCards, this);
                Socket.bind("new_game", this.beginBattle, this);
                Socket.bind("game_over", this.endBattle, this);

                this.cardsInHand.bind("step", function(cardModel) {
                    this.trigger("STEP");
                    var indexOfCard = _.findIndex(this.cardsInHand.models, function(card) {
                        return cardModel.cid == card.cid;
                    });
                    sendAction(indexOfCard);
                }, this);

                this.cardsInHand.bind("delete", this.removeCard, this);
            },

            beginBattle : function(msg) {

                this.set({
                    opponentName : msg.opponent_name
                });

                for (var key in msg.deck) {
                    this.cardsInHand.add({cardId : msg.deck[key]});
                    this.cardsInOpponentHand.add({cardType : "closed"});
                }

                this.trigger("BATTLE_BEGAN");
            },

            searchBattle : function() {
                var request = JSON.stringify({
                    action : "findGame"
                });
                Socket.send(request);
            },

            setCard : function(data) {
                var isPlayerStep = data.isSetter;
                if (!isPlayerStep) {
                    this.trigger("OPPONENT_STEP");
                }
            },

            revealCards : function(data) {
                var opponentCard = this.cardsInOpponentHand.shift();
                opponentCard.updateById(data.opponentCard);
                setTimeout(this.nextStep.bind(this), 3000);
            },

            nextStep : function() {
                this.trigger("NEXT_STEP");
            },

            endBattle : function(msg) {
                console.log(msg);
            },

            removeCard : function(model) {
                this.trigger("removeCard", model);
            }
        }))();

        return battleModel;
    }
);