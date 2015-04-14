/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "models/sockets/socket"
    ], function(Backbone, Socket) {

        return new (Backbone.Model.extend({

            initialize : function(options) {
            }

        }))();
    }
)