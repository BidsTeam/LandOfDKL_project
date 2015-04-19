/**
 * Created by rikimaru on 18.03.15.
 */

define(
    [
        "backbone",
        "config",
        "views/loading"
    ], function(Backbone, Config, loading) {

    function onEvent(event) {
        var data = JSON.parse(event.data);
        this.trigger(data.action, data);
    }

    function onError(msg) {
        this.trigger("error", msg);
    }

    function onClose(msg) {
        this.trigger("closed", msg);
    }

    return new (Backbone.Model.extend({

        connection : null,

        initialize : function(options) {

            loading.show();

            this.connect(options.address);
            this.connection.onopen = function() {
                loading.hide();
                this.trigger("SOCKET_CONNECTED_OK");
            }.bind(this);

            this.connection.onmessage = onEvent.bind(this);
            this.connection.onerror = onError.bind(this);
            this.connection.onclose = onClose.bind(this);
        },

        connect : function(address) {
            this.connection = new WebSocket(address);
            if (this.connection.readyState != 4) {
                loading.hide();
                this.trigger("SOCKET_CONNECT_ERROR");
            }
        },

        send : function(msg) {
            this.connection.send(msg);
        },

        close : function() {
            this.connection.close();
        }

    }))({address : Config.socketChatUrl});

});