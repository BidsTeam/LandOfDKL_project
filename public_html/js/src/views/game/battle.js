/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "views/pages/gamePage",
        "templates/battleField",
        "jquery",
        "views/game/middleField",
        "models/game/battle",
        "jquery-ui",
        "views/game/card"
    ],
    function(Backbone, gamePage, battlefieldTmpl, $, MiddleField, battleModel, Ui, CardViewClass) {

        return new (Backbone.View.extend({

            template : battlefieldTmpl,

            playerField : {},
            opponentField : {},
            middleField : {},

            cardViews : [],

            initialize : function() {
                this.model = battleModel;
                this.model.bind("change:state", this.update, this);
                this.model.bind("BATTLE_BEGAN", this.render, this);
            },

            beginBattle : function() {
                gamePage.go();
                //this.model.searchBattle();
                this.model.beginBattle({opponentName : "blabla"});
                //this.render();
            },

            render : function() {
                var $html = $(this.template());
                var $gameArea = $("#game-area");
                var cardDeck = [];
                var cardsOnField = [];

                this.setElement($html);
                $gameArea.html(this.$el);

                this.$(".battlefield-container__field").css("height", $gameArea.height());

                this.middleField = new MiddleField({el : ".middle-field"});
                this.opponentField = this.$(".opponent-field");
                this.playerField = this.$(".player-field");

                cardDeck = this.model.cardDeck.models;
                for (var key in cardDeck) {
                    newCard = new CardViewClass({model : cardDeck[key]});
                    this.cardViews.push(newCard);
                    this.playerField.append(newCard.$el);
                    cardsOnField.push(newCard.$el);
                }

                for (var key in cardsOnField) {
                    var $card = cardsOnField[cardsOnField.length-1-key];
                    var position = $card.position();
                    $card.css("position", "absolute")
                        .css("top", position.top)
                        .css("left", position.left);

                }
            },

            update : function(model) {
                var state = model.get("state");
                var cardDeck = this.cardViews;
                switch (state) {
                    case "WAITING_FOR_OPPONENT" :
                        for (var key in cardDeck) {
                            //cardDeck[key].$el.draggable("disable");
                        }
                        break;
                    case "THINKING" :
                        for (var key in cardDeck) {
                            cardDeck[key].$el.draggable("enable");
                        }
                        break;
                }
            },

            restruct : function() {

            }
        }))();
    }
);