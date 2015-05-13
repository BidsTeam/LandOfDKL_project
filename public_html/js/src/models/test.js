/**
 * Created by rikimaru on 28.04.15.
 */

define(
    [
        "backbone"
    ], function(Backbone) {

        return new (Backbone.Model.extend({

            defaults : {
                name : "roma"
            },

            url : "http://localhost:8080",

            sync : function(method, model, options) {
                console.log(arguments);
            }

        }))();

    }
);