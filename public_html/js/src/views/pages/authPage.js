define(
    [
        "pageView",
        "templates/auth_page",
        "routers/page_router",
        "models/user",
        "views/loading",
        "api"
    ], function(pageView, authPageTmpl, router, User, loading, API) {

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

            loading.showAfterTimeout(2000);

            User.login({
                login : $("#auth-login-field").val(),
                password : $("#auth-password-field").val()
            }).then(function(msg) {
                $(e.target).removeAttr("disabled");
                loading.clearTimeoutAndCloseIfOpened();
                if (msg.status == 404) {
                    alert(msg.response.error);
                    return;
                } else {
                    router.navigate("game", {trigger: true, replace: true});
                }
            }, function(err) {
                loading.clearTimeoutAndCloseIfOpened();
                alert("Ошибка");
            });

        }
    });

    return new authPage({pageHtml : authPageTmpl()});
});