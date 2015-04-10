define(
    [
        'pageView',
        'templates/game_page',
        "models/sockets/chat",
        "views/game/chat",
        "jquery",
        "models/User",
        "models/sockets/userList",
        "views/game/userList"
    ],function(pageView, gamePageTmpl, chat, chatView, $, User, userList, userListView) {

        var gamePage = pageView.extend({

            events : {
                "click .chat__send-button" : "sendMsgToChat",
                "keydown .chat__input" : "sendMsgToChat",
                "click a[action=logout]" : "logout"
            },

            _construct : function() {
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").hide();
                }, this);

                this.chatView = new chatView({chatContainerId : "chat-content"});
                this.userListview = new userListView({listContainerId : "players-in-room-list"});
                this.beginBattle();
            },

            render : function() {
            },

            beginBattle : function() {
                require(['views/game/battle'], function(battleView) {
                    battleView.beginBattle();
                });
            },

            sendMsgToChat : function(e) {
                if(e.type === "click" || e.keyCode == 13) {
                    var chatContainer = $(e.target).parent();
                    var msg = $(chatContainer).find(".chat__input").val();
                    if( $(chatContainer).hasClass("chat__input-container__private") ) {
                        var receiver = $(chatContainer).find(".chat__receiver").val();
                        chat.sendPrivate(msg, receiver);
                    } else {
                        chat.sendPublic(msg);
                    }
                    $(chatContainer).find("input[type=text]").val("");
                }
            },

            logout : function(e) {
                User.logout();
            }
        });

        return new gamePage({pageHtml : gamePageTmpl()});
    }
);