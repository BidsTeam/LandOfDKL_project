/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        "backbone",
        "models/sockets/chat",
        "templates/new_chat_message"
    ], function(Backbone, chat, msgTmpl) {

        return Backbone.View.extend({

            initialize : function(options) {
                this.setElement("#"+options.chatContainerId);
                chat.bind("privateMessageReceived", this.renderPrivate, this);
                chat.bind("publicMessageReceived", this.renderPublic, this);
            },

            render : function(message) {
                this.$el.append(msgTmpl(message));
                this.$el.scrollTop(this.$el.get()[0].scrollHeight);
            },

            renderPrivate : function(message) {
                message.access = "private";
                this.render(message);
            },

            renderPublic : function(message) {
                message.access = "public";
                this.render(message);
            }
        });
    }
);