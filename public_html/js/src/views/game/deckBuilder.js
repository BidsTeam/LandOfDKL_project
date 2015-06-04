/**
 * Created by rikimaru on 11.04.15.
 */

define(
    [
        "backbone",
        "../pages/gamePage",
        "jquery",
        "jquery-ui",
        "views/game/card",
        "views/loading",
        "collections/socketsPool",
        "alert",
        "templates/deck_page"
    ],
    function(
        Backbone,
        gamePage,
        $,
        Ui,
        CardViewClass,
        loading,
        socketsPool,
        Alert,
        deckBuilderTpl
    ) {

        var Socket = socketsPool.getSocketByName("socketActionsUrl");

        return new (Backbone.View.extend({
            initialize : function() {
                //this.model = battleModel;

            },


            render : function() {
                this.setElement(deckBuilderTpl());
                $("#game-area").html(this.$el);
            }

        }))();
    }
);