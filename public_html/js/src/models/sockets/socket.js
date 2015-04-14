/**
 * Created by rikimaru on 18.03.15.
 */

define(["backbone", "config"], function(Backbone, Config) {

    function onEvent(event) {
        var data = JSON.parse(event.data);
        switch( data.action ) {
            case "public_message" : this.trigger("publicMessageReceived", data.body); break;
            case "private_message" : this.trigger("privateMessageReceived", data.body); break;
            case "newChatUsers" : this.trigger("newChatUsers", data.usernames); break;
        }
    }

    function onError(msg) {
        console.log(msg);
    }

    function onClose(msg) {
        console.log(msg);
    }

    return new (Backbone.Model.extend({

        connection : null,

        initialize : function(options) {

            this.connect(options.address);
            this.connection.onopen = function() {
                console.log("Socket connect success");
            };
            this.connection.onmessage = onEvent.bind(this);
            this.connection.onerror = onError.bind(this);
            this.connection.onclose = onClose.bind(this);
        },

        connect : function(address) {
            this.connection = new WebSocket(address);
        },

        send : function(msg) {
            this.connection.send(msg);
        },

        close : function() {
            this.connection.close();
        }

    }))({address : Config.socketChatUrl});

});