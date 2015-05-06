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
        "views/game/card",
        "views/loading"
    ],
    function(Backbone, gamePage, battlefieldTmpl, $, MiddleField, battleModel, Ui, CardViewClass, loading) {

        return new (Backbone.View.extend({

            template : battlefieldTmpl,

            playerField : {},
            opponentField : {},
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
                var $html = $(this.template());
                var $gameArea = $("#game-area");

                this.setElement($html);
                $gameArea.html(this.$el);

                this.$(".battlefield-container__field").css("height", $gameArea.height());

                this.middleField = new MiddleField({el : ".middle-field"});
                this.opponentField = this.$(".opponent-field");
                this.playerField = this.$(".player-field");
            },

            onOpponentStep : function(data) {
                var $opponentCard = $(this.opponentField.find(".card-container")[0]);
                $opponentCard.detach().appendTo(this.middleField.$el);
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
                    return cardView.model.cid == model.cid;
                });
            },

            addCardToHand : function(model) {
                var newCard = new CardViewClass({model : model});
                this.cardViews.push(newCard);
                this.playerField.append(newCard.$el);
            },

            addCardToOpponentHand : function(model) {
                var newCard = new CardViewClass({model : model});
                newCard.$el.draggable("disable");
                this.opponentCardViews.push(newCard);
                this.opponentField.append(newCard.$el);
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
                this.playerField = {};
                this.opponentField = {};
                this.middleField = {};
                this.$el.remove();
            }

        }))();
    }
);