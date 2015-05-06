/**
 * Created by andreybondar on 03.04.15.
 */
define(
    [
        "backbone",
        "models/game/userList",
        "templates/chat/chat_users"
    ], function(Backbone, userList, listTmpl) {

        return Backbone.View.extend({

            initialize : function(options) {
                this.setElement(options.listContainerSelector);
                userList.bind("newChatUsers", this.render, this);
            },

            render : function(userList) {
                this.$el.empty();
                this.$el.append(listTmpl(userList));
                this.$el.scrollTop(this.$el.get()[0].scrollHeight);
            }

        });
    }
);
