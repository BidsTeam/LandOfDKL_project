/**
 * Created by andreybondar on 03.04.15.
 */
define(
    [
        "backbone",
        "models/socket"
    ],
    function(Backbone, Socket) {

        return new (Backbone.Model.extend({
            initialize : function(options) {
                Socket.bind("newChatUsers", this.receive, this);
            },
            receive : function(newUserList) {
                this.trigger("newChatUsers", newUserList.usernames);
            }

        }))();
    }
);
