define(
    [
        'pageView',
        'templates/game_page',
        "models/sockets/chat",
        "views/game/chat",
        "jquery"
    ],function(pageView, gamePageTmpl, chat, chatView, $) {

        var gamePage = pageView.extend({

            events : {
                "click .chat__send-button" : "sendMsgToChat",
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
                    var chatContainer = $(e.target).parent();
                    var msg = $(chatContainer).find(".chat__input").val();
                    if( $(chatContainer).hasClass("private") ) {
                        var receiver = $(chatContainer).find(".chat__receiver").val();
                        chat.sendPrivate(msg, receiver);
                    } else {
                        chat.sendPublic(msg);
                    }
                    $(chatContainer).find("input[type=text]").val("");
                }
            }
        });

        return new gamePage({pageHtml : gamePageTmpl()});
    }
);