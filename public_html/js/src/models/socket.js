/**
 * Created by rikimaru on 18.03.15.
 */

define(
    [
        "backbone",
        "config",
        "views/loading",
        "models/user"
    ], function(Backbone, Config, loading, User) {

    function onEvent(event) {
        var data = JSON.parse(event.data);
        console.log(data);
        this.trigger(data.action, data);
    }

    function onError(msg) {
        this.trigger("error", msg);
    }

    function onClose(msg) {
        this.trigger("closed", msg);
    }

    function onOpen() {
        loading.hide();
        this.trigger("SOCKET_CONNECTED_OK");
    }

    return Backbone.Model.extend({

        connection : null,

        defaults : {
            name : ""
        },

        initialize : function(options) {

            if (options.name) {
                this.set({name : options.name});
            }

            User.bind("logout", this.close, this);
            this.bind("hello", this.saveCardsInformation, this);
            this.connect(options.address);

            require(['views/pages/gamePage'], function(gamePageView) {
                this.bind("reconnect", gamePageView.reconnectToBattle.bind(gamePageView), this);
                this.bind("currentGameState", gamePageView.continueBattle.bind(gamePageView), this);
            }.bind(this));

            require(['models/game/userList'], function(userList) {
                this.bind("newChatUsers", userList.receive.bind(userList));
            }.bind(this));

            this.connection.onopen = onOpen.bind(this);
            this.connection.onmessage = onEvent.bind(this);
            this.connection.onerror = onError.bind(this);
            this.connection.onclose = onClose.bind(this);
        },

        saveCardsInformation : function(cards) {
            var cardsJSON = JSON.stringify(cards.cards);
            localStorage.setItem("cards", cardsJSON);
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

    });

});