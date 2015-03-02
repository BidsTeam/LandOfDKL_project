var MainView = Backbone.View.extend({
    el : "#template-container",


    events: {
    },


    initialize: function() {
    },

    render: function(tpl){
        if (tpl != "gamePage"){ // Говнокод, но лучше чем во всех темплейтах стоят дубликаты!!!!!!
            $(".logo-container").removeClass("hide")
        } else {
            $(".logo-container").addClass("hide")
        }
        this.$el.html(TemplateList[tpl]());
    }
});
