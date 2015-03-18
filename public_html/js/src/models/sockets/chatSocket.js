/**
 * Created by rikimaru on 18.03.15.
 */

define(
    [
        "models/sockets/socket",
        "config"
    ],
    function(Socket, Config) {

        var chatSocket = Socket.extend({
            
            sendPublic : function(msg) {

            },

            sendPrivate : function(msg) {

            }
        });

        return new chatSocket({address : Config.socketChatUrl});
    }
);