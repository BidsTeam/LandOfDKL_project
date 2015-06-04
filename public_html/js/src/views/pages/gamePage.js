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
        "views/game/battle",
        "views/game/deckBuilder"
    ], function(pageView, gamePageTpl, chat, chatView, $, User, userList, userListView, loading, pageRouter, Config, socketsPool, battleModel, battleView,deckBuilder) {

        var Socket;

        var gamePage = pageView.extend({

            events : {
                "click .chat__send-button" : "sendMsgToChat",
                "keydown .input-container__input-field" : "sendMsgToChat",
                "click a[action=logout]" : "logout",
                "click a[action=findGame]" : "findBattle",
                "click a[action=buildDeck]" : "buildDeck",
                "click a[action=concede]" : "concede"
            },

            _construct : function() {
                new userListView({listContainerSelector : ".chat__players-in-room-list"});
                new chatView({chatContainerSelector : ".chat"});
                Socket = socketsPool.getSocketByName("socketActionsUrl");
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

            concede : function() {
                Socket.send(JSON.stringify({
                    action : "gameAction",
                    gameAction : "concede"
                }));
            },

            buildDeck : function() {
                deckBuilder.render();
            },

            continueBattle : function(msg) {
                this.go();
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

        return new gamePage({pageHtml : gamePageTpl({})});
    }
);