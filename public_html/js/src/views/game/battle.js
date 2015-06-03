/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "views/pages/gamePage",
        "templates/battle/battleField",
        "jquery",
        "views/game/dropField",
        "models/game/battle",
        "jquery-ui",
        "views/game/card",
        "views/loading",
        "collections/socketsPool",
        "alert",
        "views/game/myPlayerInGame",
        "views/game/opponentPlayerInGame",
        "views/game/healthIndicator"
    ],
    function(
        Backbone,
        gamePage,
        battlefieldTmpl,
        $,
        DropField,
        battleModel,
        Ui,
        CardViewClass,
        loading,
        socketsPool,
        Alert,
        MyPlayerInGameClass,
        OpponentPlayerInGameClass,
        HealthIndicatorClass
    ) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.View.extend({

            dropField : {},
            player : {},
            opponentPlayer : {},
            playerHealth : {},
            opponentHealth : {},

            initialize : function() {
                this.model = battleModel;
                this.model.bind("BATTLE_BEGAN", this._onBeginBattle, this);
                this.model.bind("NEXT_STEP", this._onNextStep, this);
                this.model.bind("END_BATTLE", this._onEndBattle, this);
            },

            _onBeginBattle : function() {

                loading.hide();

                this.render();

                this.player = new MyPlayerInGameClass({
                    model : this.model.player,
                    $deckElem : this.$(".player-deck")
                });
                this.opponentPlayer = new OpponentPlayerInGameClass({
                    model : this.model.opponentPlayer,
                    $deckElem : this.$(".opponent-deck")
                });

                this.playerHealth = new HealthIndicatorClass({
                    model : this.player.model
                });
                this.opponentHealth = new HealthIndicatorClass({
                    model : this.opponentPlayer.model
                });

                this.dropField = new DropField({el : ".drop-field"});

                this.renderContent();
            },

            render : function() {
                this.setElement(battlefieldTmpl());
                $("#game-area").html(this.$el);
            },

            renderContent : function() {
                this.$(".health-container__player").html(this.playerHealth.$el);
                this.$(".health-container__opponent").html(this.opponentHealth.$el);
            },

            _onNextStep : function() {
                this.dropField.clear();
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
                this.dropField = {};
                delete this.dropField;
                this.player = {};
                delete this.player;
                this.opponentPlayer = {};
                delete this.opponentPlayer;
                this.$el.remove();
            }

        }))();
    }
);