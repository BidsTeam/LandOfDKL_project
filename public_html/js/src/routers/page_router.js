define(
    "routers/page_router",
    [
        "backbone",
        "views/main",
        "views/signup"
    ],
    function(Backbone, mainView, signupView) {
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
                mainView.render("mainPage");
            },

            gamePageInit : function() {
                mainView.render("gamePage");
            },

            authPageInit : function() {
                mainView.render("authPage");
            },

            signupPageInit : function() {
                signupView.render("signupPage");

            },
            scoreboardPageInit : function() {
                mainView.render("scoreboardPage");
            }
        });

        var router = new Router();
        Backbone.history.start();
        return router;
    }
);