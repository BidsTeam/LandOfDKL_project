define(
    [
        "pageView",
        "templates/auth_page",
        "routers/page_router",
        "models/user",
        "views/loading"
    ], function(pageView, authPageTmpl, router, User, loading) {

    var authPage = pageView.extend({

        events : {
            "click #auth-submit-button" : "auth"
        },

        _construct : function(options) {
        },

        render : function() {
        },

        auth : function(e) {
            $(e.target).attr("disabled", "disabled");
            e.preventDefault();

            loading.show();

            User.login({
                login : $("#auth-login-field").val(),
                password : $("#auth-password-field").val()
            }).then(function(msg) {
                $(e.target).removeAttr("disabled");
                loading.hide();
                router.navigate("game", {trigger: true, replace: true});
            }, function(err) {
                alert("Ошибка");
            });

        }
    });

    return new authPage({pageHtml : authPageTmpl()});
});