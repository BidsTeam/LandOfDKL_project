/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        "backbone",
        "models/sockets/chatSocket",
        "templates/new_chat_message"
    ], function(Backbone, chatSocket, msgTmpl) {

        var chatView = Backbone.View.extend({

            initialize : function(options) {
                this.setElement("#"+options.chatContainerId);
                chatSocket.bind("recieve", this.render, this);
            },

            render : function(message) {
                console.log("recieved");
                console.log(message);
                var msgObj = JSON.parse(message.data);
                var msgBody = msgObj.body;
                var time = new Date();

                time = {
                    minutes : String(time.getMinutes()),
                    seconds : String(time.getSeconds()),
                    hours : String(time.getHours())
                };

                time = time.hours + ":" + time.minutes + ":" + time.seconds;

                this.$el.append(msgTmpl(msgBody));
                this.$el.scrollTop(this.$el.get()[0].scrollHeight);
            }
        });

        return chatView;
    }
);