/**
 * Created by rikimaru on 12.03.15.
 */
define(["backbone", "../../templates/auth_page", "config"], function(Backbone, authPageTmpl, Config) {

    var authPage = Backbone.View.extend({
        el : "#template-container",

        events : {
            "click #auth-submit-button" : "auth"
        },

        render : function() {
            $(".logo-container__logo").show();
            this.$el.html(authPageTmpl());
        },

        auth : function(e) {
            e.preventDefault();
            var data = {
                login : $("#auth-login-field").val(),
                password : $("#auth-password-field").val()
            };

            $.ajax({
                type : "POST",
                data : data,
                url : Config.authUrl,
                success : function(msg) {
                    console.log(msg);
                },
                error : function(msg) {
                    console.log(msg);
                }
            });

        }
    });

    return new authPage();
});