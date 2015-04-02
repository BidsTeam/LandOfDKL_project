define(
    [
        "backbone",
        "models/User"
    ],
    function(
        Backbone,
        User
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
                User.bind("logout", this.mainPageInit(), this);
            },

            mainPageInit : function() {
                require(["views/pages/mainPage"], function(mainPageView) {
                    mainPageView.go();
                });
            },

            gamePageInit : function() {
                require(['views/pages/gamePage'], function(gamePageView) {
                    gamePageView.go();
                });
            },

            authPageInit : function() {
                require(['views/pages/authPage'], function(authPageView) {
                    authPageView.go();
                });
            },

            signupPageInit : function() {
                require(['views/pages/signupPage'], function(signupPageView) {
                    signupPageView.go();
                });

            },
            scoreboardPageInit : function() {
                require(['views/pages/scoreboardPage'], function(scoreboardPageView) {
                    scoreboardPageView.go();
                });
            }
        });

        var router = new Router();
        Backbone.history.start();
        return router;
    }
);