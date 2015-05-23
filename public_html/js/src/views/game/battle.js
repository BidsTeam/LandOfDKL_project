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
        "views/loading",
        "collections/socketsPool",
        "alert",
        "views/game/myPlayerInGame",
        "views/game/opponentPlayerInGame"
    ],
    function(
        Backbone,
        gamePage,
        battlefieldTmpl,
        $,
        MiddleField,
        battleModel,
        Ui,
        CardViewClass,
        loading,
        socketsPool,
        Alert,
        MyPlayerInGameClass,
        OpponentPlayerInGameClass
    ) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.View.extend({

            template : battlefieldTmpl,
            middleField : {},
            player : {},
            opponentPlayer : {},

            initialize : function() {
                this.model = battleModel;
                this.model.bind("BATTLE_BEGAN", this._onBeginBattle, this);
                this.model.bind("NEXT_STEP", this._onNextStep, this);
                this.model.bind("END_BATTLE", this._onEndBattle, this);
            },

            _onBeginBattle : function() {
                loading.hide();
                Socket.bind("closed", function() {
                    //todo обработка обрыва соединения и вывод ошибки
                });

                this.render();

                this.player = new MyPlayerInGameClass({
                    model : this.model.player,
                    $deckElem : this.$(".player-deck")
                });

                this.opponentPlayer = new OpponentPlayerInGameClass({
                    model : this.model.opponentPlayer,
                    $deckElem : this.$(".opponent-deck")
                });

                this.middleField = new MiddleField({el : ".middle-field"});

                this.model.player.bind("change:health", this.updateHealth, this);
                this.model.opponentPlayer.bind("change:health", this.updateHealth, this);

                this.renderContent();
            },

            render : function() {
                var $html = $(this.template());
                var $gameArea = $("#game-area");

                this.setElement($html);
                $gameArea.html(this.$el);
            },

            renderContent : function() {
                this.model.player.trigger("change:health", this.model.player);
                this.model.opponentPlayer.trigger("change:health", this.model.opponentPlayer);
            },

            _onNextStep : function() {
                this.middleField.clear();
            },

            _onEndBattle : function(result) {
                this._clear();
                var alertOptions = {
                    effect : "fadeFromTop",
                    textClass : ""
                };

                switch (result) {
                    case 0 : Alert.alert("Ничья", alertOptions); break;
                    case 1 : alertOptions.textClass = "alert-box__text_winner"; Alert.alert("Ты чемпион!", alertOptions); break;
                    case -1 : alertOptions.textClass = "alert-box__text_looser"; Alert.alert("Ты проиграл...", alertOptions); break;
                }
            },

            _clear : function() {
                this.middleField = {};
                this.player = {};
                this.opponentPlayer = {};
                this.$el.remove();
            }

        }))();
    }
);