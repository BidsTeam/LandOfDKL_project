/**
 * Created by rikimaru on 12.03.15.
 */
define(['backbone', '../../templates/game_page', "models/sockets/chatSocket", "jquery"], function(Backbone, gamePageTmpl, chat, $) {

    var gamePage = Backbone.View.extend({

        el : "#template-container",

        events : {
            "click #send-msg-to-chat" : "sendMsgToChat"
        },

        initialize : function() {
        },

        render : function() {
            $(".logo-container__logo").hide();
            this.$el.html(gamePageTmpl());
        },

        sendMsgToChat : function() {
            var msg = $("#chat-input").val();
            chat.sendPublic(msg);
        }
    });

    return new gamePage();
});