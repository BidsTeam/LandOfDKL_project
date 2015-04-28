/**
 * Created by rikimaru on 18.03.15.
 */

define(
    [
        "backbone",
        "models/socket"
    ],
    function(Backbone, Socket) {

        return new (Backbone.Model.extend({

            initialize : function(options) {
                Socket.bind("public_message", this.receivePublicMessage, this);
                Socket.bind("private_message", this.receivePrivateMessage, this);
            },

            send : function(msg) {
                var data;
                if( msg.length == 0 ) {
                    return;
                }
                if (this.has("receiver")) {
                    var receiver = this.get("receiver");
                    data =  {action : "privateMessage", message : msg, receiverName : receiver};
                    this.trigger("SEND_PRIVATE", data);
                    this.unset("receiver");
                } else {
                    data = {action : "publicMessage", message : msg};
                }
                Socket.send(JSON.stringify(data));
            },

            setReceiverForPrivate : function(receiver) {
                this.set({receiver : receiver});
            },

            receivePublicMessage : function(msg) {
                this.trigger("publicMessageReceived", msg.body);
            },

            receivePrivateMessage : function(msg) {
                this.trigger("privateMessageReceived", msg.body);
            }

        }))();
    }
);