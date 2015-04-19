/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone",
        "lodash"
    ],
    function(Backbone, _) {

        var Card = function(options) {
            this.type = options.type;
            this.title = options.title;
            this.description = options.description;
            this.effect = options.effect;
        };

        var CARDS_DEFAULTS = {
            knight : new Card({
                type : "knight",
                title : "Рыцарь",
                description : "Что то делает",
                effect : "Благодать"
            }),
            princess : new Card({
                type : "princess",
                title : "Принцесса",
                description : "Что то делает",
                effect : "Убийственная красота"
            }),
            dragon : new Card({
                type : "dragon",
                title : "Дракон",
                description : "Что то делает",
                effect : "Адский огонь"
            })
        };

        return Backbone.Model.extend({

            initialize : function(attrs) {
                
                var card;
                var cardType = attrs.type || "";

                this.bind("step", function(){
                    this.trigger("moveOnField", this);
                }, this);


                if (!cardType || !_.has(CARDS_DEFAULTS, cardType)) {
                    return;
                }
                card = _.cloneDeep(CARDS_DEFAULTS[cardType]);
                for (var key in card) {
                    this.set(key, card[key]);
                }
            }
        });
    }
);