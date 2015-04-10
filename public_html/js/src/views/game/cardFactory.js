/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "views/game/card"
    ], function(CardViewClass) {

        var cardFabric = function(){};

        cardFabric.prototype.createCard = function(options) {
            var type = options.type || "";
            var availableTypes = ["knight", "princess", "dragon"];

            if (availableTypes.indexOf(type) === -1) {
                return false;
            }

            return new CardViewClass({
                type : type
            });
        };

        return new cardFabric();
    }
);