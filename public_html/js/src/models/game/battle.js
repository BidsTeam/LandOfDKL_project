  /**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/sockets/socket",
        "models/game/cardCollection"
    ], function(Backbone, Socket, cardCollection) {


        function sendAction(cardModel) {
            var request = JSON.stringify({
                action : "gameAction",
                gameAction : "setAction",
                chosen_action : cardModel.get("type")
            });
            Socket.send(request);
        }

        var battleModel = new (Backbone.Model.extend({

            cardsInHand : new cardCollection(),
            cardsInField : new cardCollection(),
            cardsInOpponentHand : new cardCollection(),

            initialize : function(options) {
                Socket.bind("game_action_set", this.setCard, this);
                Socket.bind("game_action_reveal", this.revealCards, this);
                Socket.bind("new_game", this.beginBattle, this);

                this.cardsInHand.bind("moveOnField", function(model) {
                    this.cardsInHand.remove(model);
                    this.cardsInField.add(model);
                    sendAction(model);
                }, this);

            },

            beginBattle : function(msg) {

                this.set({
                    opponentName : msg.opponent_name
                });

                // Добавление тестовых карт
                this.cardsInHand.add([
                    {type : "knight"}
                ]);

                this.cardsInOpponentHand.add([
                    {type : "closed"}
                ]);

                this.trigger("BATTLE_BEGAN");
            },

            searchBattle : function() {
                //var request = JSON.stringify({
                //    action : "findGame"
                //});
                //Socket.send(request);
                this.beginBattle({opponent_name : "blabla"});
            },

            step : function(cardModel) {
                this.cardsInField.trigger("moveOnField", cardModel);
            },

            setCard : function(data) {
                console.log("set");
                var isPlayerStep = data.is_setter;
                if (!isPlayerStep) {
                    this.trigger("OPPONENT_STEP");
                }
            },

            revealCards : function(data) {

            }

        }))();

        return battleModel;
    }
);