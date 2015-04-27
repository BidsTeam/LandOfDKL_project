define(
    [
        "templates/main_page",
        "paginator",
        "pageView",
        "models/User",
        "routers/page_router",
        "views/loading",
        "views/backgroundVideo"
    ],
    function(
        mainPageTmpl,
        Paginator,
        PageView,
        User,
        router,
        loading,
        backgroundVideoView
    ){
        var mainPageView = PageView.extend({

            events: {
                "click a[action=enter]" : "enterGame"
            },

            _construct : function(options){
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