define(
    [
        "pageView",
        "templates/auth_page",
        "routers/page_router",
        "models/user",
        "views/loading",
        "api",
        "alert"
    ], function(pageView, authPageTmpl, router, User, loading, API, Alert) {

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

            loading.showAfterTimeout(2000, {
                cancelHandler : function() {
                    $(e.target).removeAttr("disabled");
                }
            });

            User.login({
                login : $("#auth-login-field").val(),
                password : $("#auth-password-field").val()
            }).then(function(msg) {
                $(e.target).removeAttr("disabled");
                loading.clearTimeoutAndCloseIfOpened();
                if (msg.status == 404) {
                    Alert.alert(msg.response.error);
                } else {
                    router.navigate("game", {trigger: true, replace: true});
                }
            }, function(err) {
                $(e.target).removeAttr("disabled");
                loading.clearTimeoutAndCloseIfOpened();
                Alert.alert(err.statusText);
            });

        }
    });

    return new authPage({pageHtml : authPageTmpl()});
});