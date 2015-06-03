/**
 * Created by rikimaru on 23.05.15.
 */

define(
    [
        "backbone",
        "views/game/card",
        "models/game/battle",
        "jquery"
    ], function(Backbone, CardViewClass, BattleModel, $) {

        return Backbone.View.extend({


            initialize : function(options) {
                BattleModel.bind("NEXT_STEP", this._onNextStepBegin, this);
                BattleModel.bind("END_BATTLE", this._clean, this);
                this.model.cardsInHand.bind("delete", this._removeCardFromHand, this);

                this.$deck = options.$deckElem;
                this.cardViews = [];

                var playerCardsModels = this.model.cardsInHand.models;
                for (var key in playerCardsModels) {
                    this._addCardToHand(playerCardsModels[key]);
                }

                this._construct(options);
            },

            _addCardToHand : function(model) {
                var newCard = new CardViewClass({model : model});
                this.cardViews.push(newCard);

                if (!model.get("deleted")) {
                    this.$deck.append(newCard.$el);
                    this._onAddCardToHand(model, newCard);
                }
            },

            _removeCardFromHand : function(model) {
                _.remove(this.cardViews, function(cardView) {
                    if (cardView.model.cid == model.cid) {
                        cardView.remove();
                        return true;
                    }
                });
            },

            _clean : function() {
                this.remove();
                this.off();
                this.model.cardsInHand.stopListening();
                this.model.cardsInHand.off();
                this.cardViews = [];
            },

            _onNextStepBegin : function() {},
            _onAddCardToHand : function() {}
        });
    }
);