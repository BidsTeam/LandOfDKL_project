/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "paginator",
        "pageView",
        "templates/test_page",
        "views/game/card"
    ],
    function(
        Paginator,
        PageView,
        TestPageTmpl,
        CardView
    ){
        return new (PageView.extend({

            _construct : function(options){
                CardView.createCard({type : "knight"});
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").hide();
                }, this);
            },

            render: function(){
            }

        }))({pageHtml : TestPageTmpl()});
    }
);