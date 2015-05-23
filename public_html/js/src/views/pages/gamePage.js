define(
    [
        'pageView',
        'templates/game_page',
        "models/game/chat",
        "views/game/chat",
        "jquery",
        "models/user",
        "models/game/userList",
        "views/game/userList",
        "views/loading",
        "routers/page_router",
        "config",
        "collections/socketsPool",
        'models/game/battle',
        "views/game/battle"
    ],function(pageView, gamePageTmpl, chat, chatView, $, User, userList, userListView, loading, pageRouter, Config, socketsPool, battleModel, battleView) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        var gamePage = pageView.extend({

            events : {
                "click .chat__send-button" : "sendMsgToChat",
                "keydown .input-container__input-field" : "sendMsgToChat",
                "click a[action=logout]" : "logout",
                "click a[action=findGame]" : "findBattle"
            },

            _construct : function() {
                this.chatView = new chatView({chatContainerSelector : ".chat"});
                this.userListview = new userListView({listContainerSelector : ".chat__players-in-room-list"});
                Socket.bind("reconnect", this.reconnectToBattle, this);
                Socket.bind("currentGameState", this.continueBattle, this);
            },

            render : function() {
            },

            findBattle : function() {
                loading.show();
                if (Config.testMode) {
                    battleModel.beginBattle({opponentName : "testPlayer"});
                } else {
                    battleModel.searchBattle();
                }
            },

            continueBattle : function(msg) {
                battleModel.beginBattle(msg);
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

            reconnectToBattle : function(msg) {
                Socket.send(JSON.stringify({
                    action : "reconnect"
                }));
            },

            logout : function(e) {
                User.logout().then(function() { pageRouter.navigate("/", {replace : true, trigger : true})});
            }
        });

        return new gamePage({pageHtml : gamePageTmpl()});
    }
);