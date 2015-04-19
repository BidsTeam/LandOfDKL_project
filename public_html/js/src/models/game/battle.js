/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/sockets/socket",
        "models/game/cardCollection"
    ], function(Backbone, Socket, cardCollection) {


        /**
            Possible states:
            - THINKING (string)
            - WAITING_FOR_OPPONENT (string)
            - LOOK_FOR_RESULT (string)
            - WAITING_FOR_YOUR_STEP (string)
        */
        function updateState(state) {
            battleModel.set({state : state});
        }

        function sendAction(cardModel) {
            var request = JSON.stringify({
                action : "gameAction",
                gameAction : "setAction",
                chosen_action : cardModel.get("type")
            });
            Socket.send(request);
        }

        var battleModel = new (Backbone.Model.extend({

            defaults : {
                state : "THINKING",
                opponentName : "",
                currentCard : {} // Card Model
            },

            cardDeck : new cardCollection(),
            cardsInHand : new cardCollection(),
            cardsInField : new cardCollection(),

            initialize : function(options) {
                Socket.bind("game_action_set", this.update, this);
                Socket.bind("game_action_reveal", this.update, this);
                Socket.bind("new_game", this.beginBattle, this);

                this.cardDeck.bind("moveOnField", function(model) {
                    this.cardDeck.remove(model);
                    this.cardsInField.add(model);
                }, this);

                this.cardDeck.bind("remove", function(model) {
                }, this);
            },

            beginBattle : function(msg) {

                this.set({
                    opponentName : msg.opponent_name
                });

                // Добавление тестовых карт
                this.cardDeck.add([
                    {type : "knight"},
                    {type : "princess"},
                    {type : "dragon"},
                    {type : "dragon"},
                    {type : "dragon"},
                    {type : "dragon"},
                    {type : "dragon"}
                ]);

                this.trigger("BATTLE_BEGAN");
            },

            searchBattle : function() {
                var request = JSON.stringify({
                    action : "findGame"
                });
                Socket.send(request);
            },

            step : function(cardModel) {
                this.set({currentCard : cardModel});
                //sendAction(cardModel);
                updateState("WAITING_FOR_OPPONENT");
            },

            update : function(msg) {

                switch (msg.action) {

                    case "game_action_set" :
                        if (msg.is_setter) {
                            this.cardDeck.remove(this.get("currentCard"));
                            updateState("WAITING_FOR_OPPONENT");
                        } else {
                            updateState("WAITING_FOR_YOUR_STEP");
                        }
                        break;

                    case "game_action_reveal" :
                        updateState("LOOK_FOR_RESULT");
                        break;
                }
            }

        }))();

        return battleModel;
    }
);