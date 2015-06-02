define(
    [
        "pageView",
        "templates/auth_page",
        "models/user",
        "views/loading",
        "api",
        "alert",
        "jquery",
        "app"
    ], function(pageView, authPageTmpl, User, loading, API, Alert, $, App) {

        var router = App.getRouter();

        return new (pageView.extend({

            events : {
                "click #auth-submit-button" : "auth"
            },

            _construct : function(options) {
                router.bind("changePage_auth-page", this.focus, this);
            },

            render : function() {
            },

            focus : function() {
                $("#auth-login-field").focus();
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
                        Alert.alert(msg.response.error, {boxClass : "alert__alert-box_err"});
                    } else {
                        router.navigate("game", {trigger: true, replace: true});
                    }
                }, function(err) {
                    $(e.target).removeAttr("disabled");
                    loading.clearTimeoutAndCloseIfOpened();
                    Alert.alert(err.statusText, {boxClass : "alert__alert-box_err"});
                });

            }
        }))({pageHtml : authPageTmpl()});
});