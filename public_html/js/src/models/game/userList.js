/**
 * Created by andreybondar on 03.04.15.
 */
define(
    [
        "backbone",
        "collections/socketsPool"
    ],
    function(Backbone, socketsPool) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.Model.extend({

            initialize : function(options) {
            },

            receive : function(newUserList) {
                this.trigger("newChatUsers", newUserList.usernames);
            }

        }))();
    }
);
