define(
    [
        'pageView',
        'templates/game_page',
        "models/sockets/chatSocket",
        "views/game/chat",
        "jquery"
    ],function(pageView, gamePageTmpl, chatSocket, chatView, $) {

        var gamePage = pageView.extend({

            events : {
                "click #send-msg-to-chat" : "sendMsgToChat",
                "keydown #chat-input" : "sendMsgToChat"
            },

            _construct : function() {
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").hide();
                }, this);

                this.chatView = new chatView({chatContainerId : "chat-content"});
            },

            render : function() {
            },

            sendMsgToChat : function(e) {
                if(e.type === "click" || e.keyCode == 13) {
                    var msg = $("#chat-input").val();
                    chatSocket.sendPublic(msg);
                    $("#chat-input").val("");
                }
            }
        });

        return new gamePage({pageHtml : gamePageTmpl()});
    }
);