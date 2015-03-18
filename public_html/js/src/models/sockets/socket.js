/**
 * Created by rikimaru on 18.03.15.
 */

define(["backbone"], function(Backbone) {

    var Socket = Backbone.Model.extend({

        connection : null,
        msgQueue : null,

        initialize : function(options) {
            this.connect(options.address);
            this.connection.onopen = function() {

            };

            this.connection.onmessage = this.onmessage;
        },

        connect : function(address) {
            this.connection = new WebSocket(address);
        },

        send : function(msg) {
            this.connection.send(msg);
        },

        onmessage : function(event) {
            console.log(event.data);
        }


    });

    return Socket;
});