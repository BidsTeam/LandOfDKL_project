/**
 * Created by rikimaru on 19.05.15.
 */
define(
    [
        "templates/mobile-page",
        "pageView",
        "collections/socketsPool"
    ],
    function(
        mobilePageTmpl,
        PageView
    ){
        return new (PageView.extend({

            _construct : function(options){
            }

        }))({pageHtml : mobilePageTmpl()});
    }
);