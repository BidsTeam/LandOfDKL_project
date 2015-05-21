/**
 * Created by rikimaru on 19.03.15.
 */

define(
    [
        "backbone",
        "paginator",
        "routers/page_router"
    ],
    function(Backbone, Paginator, pageRouter) {


        var pageView = Backbone.View.extend({

            pageId : "",

            initialize : function(options) {
                var id = Paginator.appendPage(options.pageHtml);
                this.setElement("#"+id);
                this.pageId = id;
                this._construct(options);
            },

            go : function() {
                Paginator.changePage(this.pageId);
                pageRouter.trigger("changePage");
                pageRouter.trigger("changePage_"+this.pageId);
            }

        });

        return pageView;
    }
);