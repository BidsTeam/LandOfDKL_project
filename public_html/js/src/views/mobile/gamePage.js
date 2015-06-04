/**
 * Created by rikimaru on 26.05.15.
 */

define(
    [
        "backbone",
        "pageView",
        "templates/mobile/game_page",
        "views/game/card",
        "models/game/battle",
        "views/game/myPlayerInGame",
        "views/game/dropField",
        "collections/socketsPool",
        "Alert"
    ], function(Backbone, PageView, MobileGamePageTemplate, cardView, BattleModel, C_MyPlayerInGame, C_DropField, socketsPool, Alert) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (PageView.extend({

            cardsControllerView : {},
            dropField : {},

            _construct : function(options) {
                require(['views/mobile/cards-controller'], function(cardsControllerView){
                    this.cardsControllerView = cardsControllerView;
                }.bind(this));
                BattleModel.bind("BATTLE_BEGAN", this.renderBattle, this);
                BattleModel.bind("NEXT_STEP", this._onNextStep, this);
                BattleModel.bind("END_BATTLE", this._onEndBattle, this);
            },

            renderBattle : function() {

                this.player = new C_MyPlayerInGame({
                    model : BattleModel.player,
                    $deckElem : this.cardsControllerView.$cardsContainer
                });

                BattleModel.player.cardsInHand.bind("change", function() {
                    var cardsCount = BattleModel.player.cardsInHand.getLength();
                    this.$(".counter-field__amount-num-of-cards").html(cardsCount);
                }.bind(this));

                this.dropField = new C_DropField();
                this.$(".drop-field-container").html(this.dropField.$el);
            },

            continueBattle : function(msg) {
                this.go();
                BattleModel.beginBattle(msg);
            },

            reconnectToBattle : function(msg) {
                Socket.send(JSON.stringify({
                    action : "reconnect"
                }));
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
                this.cardsControllerView.clear();
            }

        }))({pageHtml : MobileGamePageTemplate()});
    }
);