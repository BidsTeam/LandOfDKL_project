/**
 * Created by rikimaru on 22.05.15.
 */

define(
    [
        "backbone",
        "models/game/playerInGame",
        "collections/socketsPool"
    ], function(Backbone, PlayerInGameClass, socketsPool) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        function _onAcceptStepEvent(msg) {
            if (!msg.isSetter) {
                this.trigger("STEP");
            }
        }

        return PlayerInGameClass.extend({

            _construct : function(attrs) {
                Socket.bind("game_action_set", _onAcceptStepEvent.bind(this));
            }

        });
    }
);