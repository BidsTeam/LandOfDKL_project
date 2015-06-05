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
        return new (Backbone.Router.extend({

            routes: {
                "(#)(/)" : "mainPageInit",
                "game(/)" : "gamePageInit",
                "auth(/)" : "authPageInit",
                "signup(/)" : "signupPageInit",
                "deckbuilder(/)" : "deckBuilderPageInit",
                "scoreboard" : "scoreboardPageInit",
                "test" : "TestPage",
                "mobile-events" : "MobileEventsPageInit"
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

            deckBuilderPageInit: function(){
                require(['../views/game/deckBuilder'],function (deckBuilderPageView){
                    deckBuilderPageView.go();
                })
            },

            scoreboardPageInit : function() {
                require(['views/pages/scoreboardPage'], function(scoreboardPageView) {
                    scoreboardPageView.go();
                });
            },

            MobileEventsPageInit : function() {
                require(['views/pages/MobileTestPage'], function(MobilePageView) {
                    MobilePageView.go();
                });
            }
        }))();
    }
);