define(
    [
        "backbone",
        "../../templates/main_page"
    ],
    function(
        Backbone,
        mainPageTmpl
    ){
        var mainPageView = Backbone.View.extend({
            el : "#template-container",
            events: {
            },
            initialize: function() {
            },
            render: function(){
                $(".logo-container__logo").show();
                this.$el.html(mainPageTmpl());
            }

        });
        return new mainPageView();
    }
);