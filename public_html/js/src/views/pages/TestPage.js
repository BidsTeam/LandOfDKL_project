/**
 * Created by rikimaru on 10.04.15.
 */

define(
    [
        "paginator",
        "pageView",
        "templates/test_page",
        "models/test"
    ],
    function(
        Paginator,
        PageView,
        TestPageTmpl,
        testModel
    ){
        return new (PageView.extend({

            _construct : function(options){
            },

            render: function(){
            }

        }))({pageHtml : TestPageTmpl()});
    }
);