/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        'backbone',
        "lodash",
        "jquery",
        "api"
    ], function(Backbone, _, $, Api) {

        var API = new Api("/api/");

        var User = Backbone.Model.extend({

            initialize : function() {
            },

            sync : function(method, model, params) {

                var methodMap = {

                    create : function() {
                        return API.apiRequest("POST", "auth/signup", model.toJSON())
                            .then(function(msg) {
                                msg = JSON.parse(msg);
                                model.set(msg, {silent : true});
                                model.unset("password", {silent : true});
                                model.trigger("signup");
                                return msg;
                            }, function(err) {
                                return err;
                            });
                    },

                    read : function() {
                        return API.apiRequest("POST", "auth/signin", model.toJSON())
                            .then(function(msg) {
                               msg = JSON.parse(msg);
                               if (msg.status == "200") {
                                   model.set(msg, {silent : true});
                                   model.unset("password", {silent : true});
                                   localStorage.setItem("user", JSON.stringify(model.toJSON()));
                               }
                               return msg;
                           }, function(err) {
                               return err;
                           });
                    },

                    update : function() {},

                    "delete" : function() {
                        return API.apiRequest("POST", "auth/drop", {})
                            .then(function(msg) {
                                model.trigger("logout");
                            });
                    }
                };

                if (_.size(model.toJSON()) == 0) {
                    method = "delete";
                }

                return methodMap[method]();
            },

            isAuth : function() {
                return API.apiRequest("GET", "auth/isauth", {});
            },

            logout : function() {
                this.clear();
                return this.save();

            },

            login : function(params) {
                this.set(params);
                return this.fetch();
            },

            signup : function(params) {
                this.set(params);
                return this.save();
            }

        });
        return new User();
    }
);