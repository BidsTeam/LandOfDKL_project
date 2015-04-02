define(
    [
        "templates/main_page",
        "paginator",
        "pageView",
        "models/User",
        "routers/page_router"
    ],
    function(
        mainPageTmpl,
        Paginator,
        PageView,
        User,
        router
    ){
        var mainPageView = PageView.extend({

            events: {
                "click a[action=enter]" : "enterGame"
            },

            _construct : function(options){
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").show();
                }, this);
            },

            render: function(){
            },

            enterGame : function(e) {
                e.preventDefault();
                User.isAuth(
                    function(msg) {
                        if (msg.isAuth) {
                            router.navigate("game", {trigger: true, replace: true});
                        } else {
                            router.navigate("auth", {trigger : true, replace : true});
                        }
                    }
                );
            }

        });

        return new mainPageView({pageHtml : mainPageTmpl()});
    }
);