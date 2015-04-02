/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        'backbone',
        "lodash",
        "jquery",
        "config"
    ], function(Backbone, _, $, Config) {

        var User = Backbone.Model.extend({

            defaults : {
                is_admin : false,
                username : "",
                id : 0,
                level : 1
            },

            initialize : function(attrs) {

            },

            build : function(params) {
                _.forEach(params, function(val, key) {
                    var paramObj = {};
                    paramObj[key] = val;
                    this.set(paramObj);
                }, this);
            },

            isAuth : function(authCallback) {
                $.ajax({
                    url : Config.apiUrl+"/auth/isauth",
                    type : "GET",
                    data : {},
                    success : function(msg) {
                        msg = JSON.parse(msg);
                        authCallback(msg);
                    }
                });
            },

            logout : function() {
                $.ajax({
                    url : Config.apiUrl+"/auth/drop",
                    type : "GET",
                    data : {},
                    success : function(msg) {
                        this.trigger("logout");
                    }.bind(this)
                });
            }

        });

        return new User();
    }
);