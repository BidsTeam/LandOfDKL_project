/**
 * Created by rikimaru on 19.03.15.
 */

define(
    [
        "backbone",
        "paginator",
        "app"
    ],
    function(Backbone, Paginator, App) {

        var pageRouter = App.getRouter();

        return Backbone.View.extend({

            pageId : "",

            initialize : function(options) {
                var id = Paginator.appendPage(options.pageHtml);
                this.setElement("#"+id);
                this.pageId = id;
                if (this._construct) {
                    this._construct(options);
                }
            },

            go : function() {
                Paginator.changePage(this.pageId);
                pageRouter.trigger("changePage");
                pageRouter.trigger("changePage_"+this.pageId);
            }

        });
    }
);