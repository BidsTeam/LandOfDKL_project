/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/game/card",
        "templates/card"
    ], function(Backbone, Ui, $, CardModel, CardTemplate) {

        return Backbone.View.extend({

            template : CardTemplate,
            type : "",

            initialize : function(options) {
                var cardType;
                var $htmlEl;

                cardType = options.type;
                this.model = new CardModel({type : cardType});

                $htmlEl = $(CardTemplate({
                    type : cardType,
                    title : this.model.get("title"),
                    effect : this.model.get("effect"),
                    description : this.model.get("description")
                }));
                this.setElement($htmlEl);

                this.$el.draggable({
                    containment : "#game-area"
                });
            }
        });
    }
);