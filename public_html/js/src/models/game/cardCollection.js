/**
 * Created by rikimaru on 16.04.15.
 */

define(
    [
        "backbone",
        "models/game/card"
    ], function(Backbone, cardModel) {

        return Backbone.Collection.extend({
            model : cardModel
        });
    }
);