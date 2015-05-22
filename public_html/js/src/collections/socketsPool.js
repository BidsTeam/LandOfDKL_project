/**
 * Created by rikimaru on 20.05.15.
 */
define(
    [
        "backbone",
        "models/socket",
        "config"
    ], function(Backbone, SocketModel, Config) {

        return new (Backbone.Collection.extend({
            model : SocketModel,

            getSocketByName : function(name) {
                var socket = this.findWhere({name : name});
                if (socket) {
                    return socket;
                } else {
                    this.add([{
                        name : name,
                        address : Config.socketAddressList[name]
                    }]);
                    return this.at(this.models.length-1);
                }
            }
        }))();
    }
);