/**
 * Created by rikimaru on 09.04.15.
 */

define(
    [
        "backbone",
        "lodash"
    ],
    function(Backbone, _) {


        return Backbone.Model.extend({

            initialize : function(attrs) {
                var setObj = {};

                if (attrs.cardId) {
                    var cardTypes = JSON.parse(localStorage.getItem("cards"));
                    var card = _.find(cardTypes, function(card) {
                        return card.id == attrs.cardId;
                    });
                    for (var key in card) {
                        setObj[key] = card[key];
                    }
                    this.set(setObj);

                } else {

                    for (var key in attrs) {
                        setObj[key] = attrs[key];
                        this.set(setObj);
                    }

                }
            },

            updateById : function(id) {
                var setObj = {};
                var cardTypes = JSON.parse(localStorage.getItem("cards"));
                var card = _.find(cardTypes, function(card) {
                    return card.id == id;
                });

                for (var key in card) {
                    setObj[key] = card[key];
                }
                this.set(setObj);
            }
        });
    }
);