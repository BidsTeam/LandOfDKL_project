/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/card",
        "templates/card"
    ], function(Backbone, Ui, $, CardModel, CardTemplate) {

        return Backbone.View.extend({

            template : CardTemplate,
            type : "",

            initialize : function(options) {
                var cardType = options.type;
                var $htmlEl = $(CardTemplate({
                    type : cardType,
                    id : "player_card_1"
                }));

                this.model = new CardModel({type : cardType});
                this.setElement($htmlEl);

                this.$el.draggable({
                    containment : "#game-area"
                });
            }
        });
    }
);