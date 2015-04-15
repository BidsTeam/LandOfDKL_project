/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/sockets/battlefieldConnection"
    ], function(Backbone, connection) {

        return new (Backbone.Model.extend({

            initialize : function(options) {
            },

            beginBattle : function(playerId) {
                connection.beginBattle(playerId);
            },

            step : function() {
                //connection.
            }
        }))();
    }
);