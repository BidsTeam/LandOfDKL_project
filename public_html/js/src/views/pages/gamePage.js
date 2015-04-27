define(
    [
        'pageView',
        'templates/game_page',
        "models/game/chat",
        "views/game/chat",
        "jquery",
        "models/User",
        "models/game/userList",
        "views/game/userList"
    ],function(pageView, gamePageTmpl, chat, chatView, $, User, userList, userListView) {

        var gamePage = pageView.extend({

            events : {
                "click .chat__send-button" : "sendMsgToChat",
                "keydown .input-container__input-field" : "sendMsgToChat",
                "click a[action=logout]" : "logout"
            },

            _construct : function() {
                this.chatView = new chatView({chatContainerId : "chat-container"});
                this.userListview = new userListView({listContainerId : "players-in-room-list"});

                //this.beginBattle();
            },

            render : function() {
            },

            beginBattle : function() {
                require(
                    [
                        'models/game/battle',
                        "views/game/battle"
                    ],function(battleModel, battleView) {
                    battleModel.searchBattle();
                });
            },

            sendMsgToChat : function(e) {
                if(e.type === "click" || e.keyCode == 13) {
                    var chatContainer = $(".input-container__input-field");
                    var msg = $(chatContainer).val();
                    chat.send(msg);
                    $(chatContainer).val("");
                    $(".message-to").remove();
                }
            },

            logout : function(e) {
                User.logout();
            }
        });

        return new gamePage({pageHtml : gamePageTmpl()});
    }
);