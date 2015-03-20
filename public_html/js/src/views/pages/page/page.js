/**
 * Created by rikimaru on 19.03.15.
 */

define(
    [
        "backbone",
        "paginator"
    ],
    function(Backbone, Paginator) {


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
                this.trigger("changePage");
                this.trigger("changePage_"+this.pageId);
            }

        });

        return pageView;
    }
);