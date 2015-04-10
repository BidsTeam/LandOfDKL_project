/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone",
        "jquery-ui",
        "jquery",
        "models/card",
        "templates/card"
    ], function(Backbone, Ui, $, CardModel, CardTemplate) {

        var CardView = Backbone.View.extend({

            template : CardTemplate,
            type : "",

            initialize : function(options) {
                var htmlEl = CardTemplate({
                    type : options.type,
                    id : "player_card_1"
                });
                this.setElement("#"+$(htmlEl).prop("id"));
                $("#testPage").append(htmlEl);
            }
        });

        var cardFabric = function(){};

        cardFabric.prototype.createCard = function(options) {
            var type = options.type || "";
            var availableTypes = ["knight", "princess", "dragon"];

            if (availableTypes.indexOf(type) === -1) {
                return false;
            }

            return new CardView({
                model : new CardModel(),
                type : type
            });
        };

        return new cardFabric();
    }
);