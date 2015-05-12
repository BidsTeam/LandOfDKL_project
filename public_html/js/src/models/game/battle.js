  /**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/socket",
        "models/game/cardCollection",
        "models/user"
    ], function(Backbone, Socket, cardCollection, User) {


        function sendAction(cardIndex) {
            var request = JSON.stringify({
                action : "gameAction",
                gameAction : "setCard",
                chosenCard : cardIndex
            });
            Socket.send(request);
        }

        return new (Backbone.Model.extend({

            cardsInHand : new cardCollection(),
            cardsInOpponentHand : new cardCollection(),

            nextStepTimerId : 0,

            initialize : function() {
                Socket.bind("game_action_set", this.setOpponentCard, this);
                Socket.bind("gameCardsReveal", this.revealCards, this);
                Socket.bind("new_game", this.beginBattle, this);
                Socket.bind("game_over", this.endBattle, this);
                this.cardsInHand.bind("MY_STEP", this.myStep, this);
                this.cardsInHand.bind("delete", this.removeCard, this);
            },

            beginBattle : function(msg) {
                this.set({
                    opponentName : msg.opponent_name,
                    playerName : User.get("login") || User.get("username")
                });

                this.trigger("BATTLE_BEGAN");

                for (var key in msg.deck) {
                    this.cardsInHand.add({cardId : msg.deck[key]});
                    this.cardsInOpponentHand.add({cardType : "closed"});
                }

            },

            searchBattle : function() {
                var request = JSON.stringify({
                    action : "findGame"
                });
                Socket.send(request);
            },

            setOpponentCard : function(data) {
                var isPlayerStep = data.isSetter;
                if (!isPlayerStep) {
                    this.trigger("OPPONENT_STEP");
                }
            },

            revealCards : function(data) {
                var opponentCard = this.cardsInOpponentHand.shift();
                opponentCard.updateById(data.opponentCard);
                this.nextStepTimerId = setTimeout(this.nextStep.bind(this), 2000);
            },

            nextStep : function() {
                this.trigger("NEXT_STEP");
            },

            endBattle : function(msg) {
                clearTimeout(this.nextStepTimerId);
                this.cardsInHand.reset();
                this.cardsInOpponentHand.reset();
                this.trigger("END_BATTLE", Number(msg.gameResult));
            },

            removeCard : function(model) {
                this.trigger("REMOVE_CARD", model);
            },

            myStep : function(cardModel) {
                this.trigger("MY_STEP");
                var indexOfCard = _.findIndex(this.cardsInHand.models, function(card) {
                    return cardModel.cid == card.cid;
                });
                sendAction(indexOfCard);
            }

        }))();
    }
);