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
                if( msg.length == 0 ) {
                    return;
                }
                var data = {action : "publicMessage", message : msg};
                this.send(JSON.stringify(data));
            },

            sendPrivate : function(msg) {
                var data = {action : "privateMessage", message : msg};
                this.send(data);
            }
        });

        return new chatSocket({address : Config.socketChatUrl});
    }
);