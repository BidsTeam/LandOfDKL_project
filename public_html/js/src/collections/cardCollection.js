/**
 * Created by rikimaru on 16.04.15.
 */

define(
    [
        "backbone",
        "models/game/card",
        "lodash"
    ], function(Backbone, cardModel, _) {

        return Backbone.Collection.extend({
            model : cardModel,

            getLength : function() {
                var length = 0;
                _.forEach(this.models, function(Model, key) {
                    if (!Model.get("deleted")) {
                        length++;
                    }
                });
                return length;
            }
        });
    }
);