/**
 * Created by rikimaru on 18.03.15.
 */

define(["backbone"], function(Backbone) {

    var MAX_MESSAGE_SIZE = 200;

    var Socket = Backbone.Model.extend({

        connection : null,

        initialize : function(options) {

            this.connect(options.address);

            this.connection.onopen = function() {
                console.log("Socket connect success");
            };
            this.connection.onmessage = this.onEvent.bind(this);
            this.connection.onerror = this.onError;
            this.connection.onclose = this.onClose;
        },

        connect : function(address) {
            this.connection = new WebSocket(address);
        },

        send : function(msg) {

            this.connection.send(msg);
        },

        onEvent : function(event) {
            this.trigger("recieve", event);
        },

        onError : function(msg) {
            console.log(msg);
        },

        onClose : function(msg) {
            console.log(msg);
        },

        close : function() {
            this.connection.close();
        }

    });

    return Socket;
});