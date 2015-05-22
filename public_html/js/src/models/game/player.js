/**
 * Created by rikimaru on 21.05.15.
 */

define(
    [
        "backbone",
        "collections/socketsPool"
    ], function(Backbone, socketsPool) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        function _onChangeHealth() {
            if (this.get("health") <= 0) {
                this.trigger("HEALTH_END");
            }
        }

        return Backbone.Model.extend({

             defaults : {
                 name : "",
                 startHealth : 20, //вообще должно передаваться с сервера
                 health : 0,
                 type : ""
             },

            initialize : function(attrs) {
                this.bind("change:health", _onChangeHealth.bind(this));
                Socket.bind("newGameState", this.update, this);
            },

            decreaseHealth : function(minusHp) {
                var currentHealth = this.get("health");

                this.set({
                    health : currentHealth - minusHp
                });
            },

            update : function(attrs) {
                this.set({
                    health : attrs[this.get("type")+"Health"]
                });
            }
        });
    }
);