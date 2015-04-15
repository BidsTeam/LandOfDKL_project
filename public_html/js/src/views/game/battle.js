/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "views/pages/gamePage",
        "templates/battleField",
        "views/game/cardFactory",
        "jquery",
        "views/game/middleField",
        "models/game/battle"
    ],
    function(Backbone, gamePage, battlefieldTmpl, cardFactory, $, MiddleField, battleModel) {

        return new (Backbone.View.extend({

            template : battlefieldTmpl,

            playerField : {},
            opponentField : {},
            middleField : {},

            initialize : function(options) {
                this.model = battleModel;
            },

            beginBattle : function() {
                gamePage.go();
                this.render();

            },

            render : function() {
                var $html = $(this.template());
                var $gameArea = $("#game-area");

                this.setElement($html);
                $gameArea.html(this.$el);

                this.$(".battlefield-container__field").css("height", $gameArea.height());

                this.middleField = new MiddleField();
                this.middleField.bind("STEP", this.model.step, this);

                this.opponentField = this.$(".opponent-field");
                this.playerField = this.$(".player-field");
                this.playerField.append(cardFactory.createCard({type:"knight"}).$el);
                this.playerField.append(cardFactory.createCard({type:"princess"}).$el);
            }
        }))();
    }
);