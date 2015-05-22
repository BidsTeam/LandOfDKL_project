define(
    [
        "templates/main_page",
        "paginator",
        "pageView",
        "models/user",
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

            _construct : function(options) {
            },

            render: function(){
            },

            enterGame : function(e) {
                e.preventDefault();
                router.navigate("game", {trigger: true, replace: true});
            }

        });

        return new mainPageView({pageHtml : mainPageTmpl()});
    }
);