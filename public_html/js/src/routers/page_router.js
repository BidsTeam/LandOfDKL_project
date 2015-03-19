define(
    [
        "backbone"
    ],
    function(
        Backbone
    ) {
        Router = Backbone.Router.extend({

            routes: {
                "(#)(/)" : "mainPageInit",
                "game(/)" : "gamePageInit",
                "auth(/)" : "authPageInit",
                "signup(/)" : "signupPageInit",
                "scoreboard" : "scoreboardPageInit"
            },

            initialize : function() {
            },

            mainPageInit : function() {
                require(["views/pages/mainPage"], function(mainPageView) {
                    mainPageView.render();
                    mainPageView.go();
                });
            },

            gamePageInit : function() {
                require(['views/pages/gamePage'], function(gamePageView) {
                    gamePageView.render();
                    gamePageView.go();
                });
            },

            authPageInit : function() {
                require(['views/pages/authPage'], function(authPageView) {
                    authPageView.render();
                    authPageView.go();
                });
            },

            signupPageInit : function() {
                require(['views/pages/signupPage'], function(signupPageView) {
                    signupPageView.render();
                    signupPageView.go();
                });

            },
            scoreboardPageInit : function() {
                require(['views/pages/scoreboardPage'], function(scoreboardPageView) {
                    scoreboardPageView.render();
                    scoreboardPageView.go();
                });
            }
        });

        var router = new Router();
        Backbone.history.start();
        return router;
    }
);