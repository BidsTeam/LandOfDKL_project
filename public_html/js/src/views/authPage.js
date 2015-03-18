/**
 * Created by rikimaru on 12.03.15.
 */
define(["backbone", "../../templates/auth_page"], function(Backbone, authPageTmpl) {

    var authPage = Backbone.View.extend({
        el : "#template-container",
        render : function() {
            $(".logo-container__logo").show();
            this.$el.html(authPageTmpl());
        }
    });

    return new authPage();
});