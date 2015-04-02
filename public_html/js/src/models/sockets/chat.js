/**
 * Created by rikimaru on 18.03.15.
 */

define(
    [
        "backbone",
        "models/sockets/socket"
    ],
    function(Backbone, Socket) {

        return new (Backbone.Model.extend({

            initialize : function(options) {
                Socket.bind("publicMessageReceived", this.receivePublicMessage, this);
                Socket.bind("privateMessageReceived", this.receivePrivateMessage, this);
            },

            sendPublic : function(msg) {
                if( msg.length == 0 ) {
                    return;
                }

                var data = {action : "publicMessage", message : msg};
                Socket.send(JSON.stringify(data));
            },

            sendPrivate : function(msg, receiver) {
                if( msg.length == 0 ) {
                    return;
                }

                var data = {action : "privateMessage", message : msg, receiverName : receiver};
                Socket.send(JSON.stringify(data));
            },

            receivePublicMessage : function(msg) {
                this.trigger("publicMessageReceived", msg);
            },

            receivePrivateMessage : function(msg) {
                this.trigger("privateMessageReceived", msg);
            }

        }))();
    }
);