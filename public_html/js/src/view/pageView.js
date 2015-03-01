console.log("popados");
PageView = Backbone.View.extend({
    el : "#app-container",
    initialize : function(options) {
        this.model.bind('change', this.render, this);
    },
    template_list: {
        "mainPage": Util.templateFromUrl('/templates/main_page.tpl'),
        "authPage": Util.templateFromUrl('/templates/auth_page.tpl'),
        "scoreboardPage": Util.templateFromUrl('/templates/scoreboard_page.tpl'),
        "gamePage": Util.templateFromUrl('/templates/game_page.tpl'),
        signupPage : Util.templateFromUrl("/templates/signup_page.tpl")
    },
    render : function() {
        var template = this.model.get("template");
        this.$el.html(this.template_list[template]());
        return this;
    }
});
