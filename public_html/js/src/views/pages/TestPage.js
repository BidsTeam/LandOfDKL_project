/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "paginator",
        "pageView",
        "templates/test_page",
        "views/game/cardFactory"
    ],
    function(
        Paginator,
        PageView,
        TestPageTmpl,
        CardFactory
    ){
        return new (PageView.extend({

            _construct : function(options){
                CardFactory.createCard({type : "knight"});
                this.bind("changePage_"+this.pageId, function() {
                    $(".logo-container__logo").hide();
                }, this);
            },

            render: function(){
            }

        }))({pageHtml : TestPageTmpl()});
    }
);