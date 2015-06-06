  /**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "collections/cardCollection",
        "collections/socketsPool",
        "models/user",
        "models/game/myPlayerInGame",
        "models/game/opponentPlayerInGame",
        "models/game/card",
        "lodash"
    ], function(Backbone, cardCollection, socketsPool, User, MyPlayerInGameModelClass, OpponentPlayerInGameModelClass, CardModelClass, _) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.Model.extend({

            player : {},
            opponentPlayer : {},

            nextStepTimerId : 0,
            battleBegan : false,

            initialize : function() {
                Socket.bind("gameCardsReveal", this.revealCards, this);
                Socket.bind("new_game", this.beginBattle, this);
                Socket.bind("game_over", this.endBattle, this);
            },

            beginBattle : function(msg) {
                if (!this.battleBegan) {
                    var playerDeck = [];
                    for (var key in msg.deck) {
                        playerDeck.push(new CardModelClass({cardId : msg.deck[key]}));
                    }
                    User.set("name",msg.playerName);
                    this.player = new MyPlayerInGameModelClass({
                        deck : playerDeck,
                        type : "player",
                        name : msg.playerName,
                        health : msg.yourHealth
                    });

                    var opponentDeck = [];
                    for (var key in msg.deck) {
                        opponentDeck.push(new CardModelClass({cardType : "closed"}));
                    }
                    this.opponentPlayer = new OpponentPlayerInGameModelClass({
                        deck : opponentDeck,
                        type : "opponent",
                        name : msg.opponentName,
                        health : msg.opponentHealth
                    });

                    this.battleBegan = true;
                    this.trigger("BATTLE_BEGAN");
                }
            },

            searchBattle : function() {
                var request = JSON.stringify({
                    action : "findGame"
                });
                Socket.send(request);
            },

            revealCards : function(data) {
                var opponentCard = this.opponentPlayer.cardsInHand.shift();
                opponentCard.updateById(data.opponentCard);
                this.nextStepTimerId = setTimeout(this.nextStep.bind(this), 2000);
            },

            nextStep : function() {
                this.trigger("NEXT_STEP");
            },

            endBattle : function(msg) {
                clearTimeout(this.nextStepTimerId);
                this.player.clear();
                this.player = {};
                this.opponentPlayer.clear();
                this.opponentPlayer = {};
                this.trigger("END_BATTLE", Number(msg.gameResult));
                this.battleBegan = false;
            }

        }))();
    }
);