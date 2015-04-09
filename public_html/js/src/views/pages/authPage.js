define(
    [
        "pageView",
        "templates/auth_page",
        "config",
        "routers/page_router",
        "models/User",
        "views/loading"
    ], function(pageView, authPageTmpl, Config, router, User, loading) {

    var authPage = pageView.extend({

        events : {
            "click #auth-submit-button" : "auth"
        },

        _construct : function(options) {
            this.bind("changePage_"+this.pageId, function() {
                $(".logo-container__logo").show();
            }, this);
        },

        render : function() {
        },

        auth : function(e) {
            $(e.target).attr("disabled", "disabled");
            e.preventDefault();
            var data = {
                login : $("#auth-login-field").val(),
                password : $("#auth-password-field").val()
            };

            $.ajax({
                type : "POST",
                data : data,
                url : Config.apiUrl+"/auth/signin",
                beforeSend : function() {
                    loading.show();
                },
                success : function(msg) {
                    User.build(JSON.parse(msg).response);
                    router.navigate("game", {trigger: true, replace: true});
                },
                error : function(msg) {
                    alert("Ошибка");
                },
                complete : function(msg) {
                    $(e.target).removeAttr("disabled");
                    loading.hide();
                }
            });

        }
    });

    return new authPage({pageHtml : authPageTmpl()});
});