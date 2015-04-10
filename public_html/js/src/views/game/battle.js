/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "views/pages/gamePage",
        "templates/battleField",
        "views/game/cardFactory",
        "jquery"
    ],
    function(Backbone, gamePage, battlefieldTmpl, cardFactory, $) {

        return new (Backbone.View.extend({

            template : battlefieldTmpl,

            initialize : function(options) {

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
                this.$(".player-field").append(cardFactory.createCard({type:"knight"}).$el);
            }
        }))();
    }
);