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
            if (type === "") {
                return false;
            }

            return new CardViewClass({
                type : type
            });
        };

        return new cardFabric();
    }
);