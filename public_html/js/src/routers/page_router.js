define(
    [
        "backbone",
        "views/mainPage",
        "views/signupPage",
        "views/scoreboardPage",
        "views/gamePage",
        "views/authPage"
    ],
    function(
        Backbone,
        mainPageView,
        signupPageView,
        scoreboardPageView,
        gamePageView,
        authPageView
    ) {
        Router = Backbone.Router.extend({

            routes: {
                "(#)(/)" : "mainPageInit",
                "game(/)" : "gamePageInit",
                "auth(/)" : "authPageInit",
                "signup(/)" : "signupPageInit",
                "scoreboard" : "scoreboardPageInit",
                "test" : "testPage"
            },

            initialize : function() {
            },

            testPage : function() {
                require(["models/sockets/chatSocket"], function(chatSocket) {
                    console.log(chatSocket);
                });
            },

            mainPageInit : function() {
                mainPageView.render();
            },

            gamePageInit : function() {
                gamePageView.render();
            },

            authPageInit : function() {
                authPageView.render();
            },

            signupPageInit : function() {
                signupPageView.render();

            },
            scoreboardPageInit : function() {
                scoreboardPageView.render();
            }
        });

        var router = new Router();
        Backbone.history.start();
        return router;
    }
);