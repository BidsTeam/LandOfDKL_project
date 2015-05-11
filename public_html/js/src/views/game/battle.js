/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "views/pages/gamePage",
        "templates/battle/battleField",
        "jquery",
        "views/game/middleField",
        "models/game/battle",
        "jquery-ui",
        "views/game/card",
        "views/loading"
    ],
    function(Backbone, gamePage, battlefieldTmpl, $, MiddleField, battleModel, Ui, CardViewClass, loading) {

        return new (Backbone.View.extend({

            template : battlefieldTmpl,

            playerDeck : {},
            opponentDeck : {},
            middleField : {},

            cardViews : [],
            opponentCardViews : [],

            initialize : function() {
                this.model = battleModel;
                this.model.bind("BATTLE_BEGAN", this.onBeginBattle, this);
                this.model.bind("OPPONENT_STEP", this.onOpponentStep, this);
                this.model.bind("MY_STEP", this.onMyStep, this);
                this.model.bind("NEXT_STEP", this.onNextStep, this);
                this.model.bind("REMOVE_CARD", this.removeCardFromField, this);
                this.model.cardsInHand.bind("add", this.addCardToHand, this);
                this.model.cardsInOpponentHand.bind("add", this.addCardToOpponentHand, this);
                this.model.bind("END_BATTLE", this.onEndBattle, this);
            },

            onBeginBattle : function() {
                loading.hide();
                this.render();
            },

            render : function() {
                var $html = $(this.template(this.model.toJSON()));
                var $gameArea = $("#game-area");

                this.setElement($html);
                $gameArea.html(this.$el);

                this.$(".battlefield-container__field").css("height", $gameArea.height());

                this.middleField = new MiddleField({el : ".middle-field"});
                this.opponentDeck = this.$(".opponent-deck");
                this.playerDeck = this.$(".player-deck");
            },

            onOpponentStep : function(data) {
                var opponentCard = this.opponentCardViews.shift();
                opponentCard.replaceToDOMElem(this.middleField.$(".step-place_opponent"), true);
            },

            onMyStep : function() {
                for (var key in this.cardViews) {
                    this.cardViews[key].$el.draggable("disable");
                }
            },

            onNextStep : function() {
                this.middleField.clear();
                for (var key in this.cardViews) {
                    this.cardViews[key].$el.draggable("enable");
                }
            },

            removeCardFromField : function(model) {
                _.remove(this.cardViews, function(cardView) {
                    if (cardView.model.cid == model.cid) {
                        return true;
                    }
                });
            },

            addCardToHand : function(model) {
                var newCard = new CardViewClass({model : model});
                this.cardViews.push(newCard);
                this.playerDeck.append(newCard.$el);
            },

            addCardToOpponentHand : function(model) {
                var newCard = new CardViewClass({model : model, draggable : false});
                newCard.$el.removeClass("card-container_highlight"); //todo вынести отсюда
                this.opponentCardViews.push(newCard);
                this.opponentDeck.append(newCard.$el);
            },

            onEndBattle : function(result) {
                this.clear();
                switch (result) {
                    case 0 : alert("Ничья"); break;
                    case 1 : alert("Ты чемпион!"); break;
                    case -1 : alert("Ты проиграл..."); break;
                }
            },

            clear : function() {
                this.cardViews = [];
                this.opponentCardViews = [];
                this.playerDeck = {};
                this.opponentDeck = {};
                this.middleField = {};
                this.$el.remove();
            }

        }))();
    }
);