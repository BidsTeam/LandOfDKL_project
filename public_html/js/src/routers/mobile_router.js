/**
 * Created by rikimaru on 26.05.15.
 */

define(
    [
        "backbone",
        "models/user"
    ], function(Backbone, User) {

        return new (Backbone.Router.extend({

            routes : {
                "(#)(/)" : "appInit",
                "auth" : "auth",
                "game" : "game"
            },

            initialize : function() {

            },

            appInit : function() {
                this.navigate("game", {trigger : true, replace : true});
            },

            auth : function() {
                require(['views/pages/authPage'], function(authPageView) {
                    authPageView.go();
                });
            },

            game : function() {
                var _this = this;
                User.isAuth().then(

                    function(msg) {
                        msg = JSON.parse(msg);
                        if (msg.isAuth) {
                            require(['views/pages/mobile/gamePage'], function(gamePageView) {
                                gamePageView.go();
                            });
                        } else {
                            _this.navigate("auth", {trigger : true, replace : true});
                        }
                    },

                    function(err) {

                    }
                );
            }
        }))();
    }
);