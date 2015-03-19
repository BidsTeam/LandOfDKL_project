
define(
    "views/game",
    [
        "backbone",
        "templater"
    ]
    ,
    function(Backbone, Templater){

        var GameView = Backbone.View.extend({
            el : "#template-container",
            events: {
                "click .chat__socket_button" : "socket_connect"
            },
            initialize: function() {
            },
            render: function(tpl){
                $(".logo-container").addClass("hide")
                this.$el.html(Templater.getTemplate(tpl));
            },
            socket_connect : function() {
                console.log("button clicked")
            }
        });

        return new GameView();

    }
);