var MainView = Backbone.View.extend({
    el : "#app-container",


    events: {
    },


    initialize: function() {
    },

    render: function(tpl){
        this.$el.html(TemplateList[tpl]());
    }
});
