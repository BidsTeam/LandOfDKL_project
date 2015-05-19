/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        "backbone",
        "models/game/chat",
        "templates/chat/new_chat_message",
        "templates/chat/message-to"
    ], function(Backbone, chat, msgTmpl, msgToTmpl) {

        return Backbone.View.extend({

            initialize : function(options) {
                this.setElement(options.chatContainerSelector);
                this.model = chat;

                this.model.bind("privateMessageReceived", this.renderPrivate, this);
                this.model.bind("publicMessageReceived", this.renderPublic, this);
                this.model.bind("change:receiver", this.setReceiver, this);
                this.model.bind("SEND_PRIVATE", this.renderMyPrivate, this);

                this.$el.on("click", function(e) {
                    if (e.target.className == "author-name") {
                        this.model.setReceiverForPrivate($(e.target).html());
                    }
                    if (e.target.className == "message-to__close-button") {
                        this.model.unset("receiver");
                    }
                    if ($(e.target).hasClass("input-container")) {
                        this.$(".input-container__input-field").focus();
                    }
                }.bind(this));

            },

            render : function(message) {
                var $chatContent = this.$(".content-container__content");
                $chatContent.append(msgTmpl(message));
                $chatContent.scrollTop($chatContent.get()[0].scrollHeight);
            },

            renderPrivate : function(message) {
                message.access = "private";
                this.render(message);
            },

            renderPublic : function(message) {
                message.access = "public";
                this.render(message);
            },

            setReceiver : function(model, receiver) {
                if (this.model.has("receiver")) {
                    this.removeReceiver();
                    this.$(".input-container").prepend(msgToTmpl({name:receiver}));
                } else {
                    this.removeReceiver();
                }
            },

            removeReceiver : function() {
                this.$(".input-container__message-to").remove();
            },

            renderMyPrivate : function(data) {

            }
        });
    }
);