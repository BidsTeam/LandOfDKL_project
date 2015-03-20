/**
 * Created by rikimaru on 20.03.15.
 */

define(
    [
        'backbone',
        "lodash"
    ], function(Backbone, _) {

        var User = Backbone.Model.extend({

            defaults : {
                is_admin : false,
                isAuth : false,
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
            }
        });

        return new User();
    }
);