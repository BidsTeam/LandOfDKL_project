define(
    "views/main",
    [
        "backbone",
        "templater"
    ]
    ,
    function(Backbone, Templater){

        var MainView = Backbone.View.extend({
            el : "#template-container",
            events: {
            },
            initialize: function() {
            },
            render: function(tpl){
                if (tpl != "gamePage"){ // Говнокод
                    $(".logo-container").removeClass("hide")
                } else {
                    $(".logo-container").addClass("hide")
                }
                this.$el.html(Templater.getTemplate(tpl));
            }
        });

        return new MainView();

    }
);