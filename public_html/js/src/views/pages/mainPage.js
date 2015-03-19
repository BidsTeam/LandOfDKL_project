define(
    [
        "templates/main_page",
        "paginator",
        "pageView"
    ],
    function(
        mainPageTmpl,
        Paginator,
        PageView
    ){
        var mainPageView = PageView.extend({

            events: {
            },

            _construct : function(options){
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").show();
                }, this);
            },

            render: function(){
            }

        });

        return new mainPageView({pageHtml : mainPageTmpl()});
    }
);