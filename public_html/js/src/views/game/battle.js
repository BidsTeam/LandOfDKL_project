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
            opponentCardViews : [],

            initialize : function() {
                this.model = battleModel;
                this.model.bind("change:state", this.update, this);
                this.model.bind("BATTLE_BEGAN", this.beginBattle, this);
                this.model.bind("OPPONENT_STEP", this.opponentStep, this);
            },

            beginBattle : function() {
                console.log("b");
                gamePage.go();
                this.render();
            },

            render : function() {
                var $html = $(this.template());
                var $gameArea = $("#game-area");
                var cardDeck;
                var opponentCardDeck;
                var newCard;

                this.setElement($html);
                $gameArea.html(this.$el);

                this.$(".battlefield-container__field").css("height", $gameArea.height());

                this.middleField = new MiddleField({el : ".middle-field"});
                this.opponentField = this.$(".opponent-field");
                this.playerField = this.$(".player-field");

                // Выставление своих карт
                cardDeck = this.model.cardsInHand.models;
                for (var key in cardDeck) {
                    newCard = new CardViewClass({model : cardDeck[key]});
                    this.cardViews.push(newCard);
                    this.playerField.append(newCard.$el);
                }

                // Выставление карт противника
                opponentCardDeck = this.model.cardsInOpponentHand.models;
                for (var key in opponentCardDeck) {
                    newCard = new CardViewClass({model : opponentCardDeck[key]});
                    newCard.$el.draggable("disable");
                    this.opponentCardViews.push(newCard);
                    this.opponentField.append(newCard.$el);
                }
            },

            opponentStep : function(data) {
                var $opponentCard = this.opponentField.find(".card_container");
                console.log($opponentCard);
            }

        }))();
    }
);