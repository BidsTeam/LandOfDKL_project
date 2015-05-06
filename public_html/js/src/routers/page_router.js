define(
    [
        "backbone",
        "models/user",
        "views/backgroundVideo",
        "views/loading"
    ],
    function(
        Backbone,
        User,
        backgroundVideoView,
        loading
    ) {
        Router = Backbone.Router.extend({

            routes: {
                "(#)(/)" : "mainPageInit",
                "game(/)" : "gamePageInit",
                "auth(/)" : "authPageInit",
                "signup(/)" : "signupPageInit",
                "scoreboard" : "scoreboardPageInit",
                "test" : "TestPage"
            },

            initialize : function() {
            },

            mainPageInit : function() {
                require(["views/pages/mainPage"], function(mainPageView) {
                    mainPageView.go();
                });
            },

            gamePageInit : function() {
                var _this = this;
                loading.showAfterTimeout(1000);
                User.isAuth()
                    .then(function(msg) {
                        msg = JSON.parse(msg);
                        if (msg.isAuth) {
                            require(['views/pages/gamePage'], function(gamePageView) {
                                gamePageView.go();
                            });
                        } else {
                            _this.navigate("auth", {trigger : true, replace : true});
                        }
                        loading.clearTimeoutAndCloseIfOpened();
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
            },

            TestPage : function() {
                require(['views/pages/TestPage'], function(TestPageView) {
                    TestPageView.go();
                });
            }
        });

        var router = new Router();
        Backbone.history.start();
        return router;
    }
);