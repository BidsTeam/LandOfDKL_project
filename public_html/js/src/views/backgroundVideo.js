/**
 * Created by rikimaru on 19.04.15.
 */

define(
    [
        "backbone",
        "jquery-video",
        "jquery",
        "paginator",
        "lodash",
        "views/loading"
    ], function(Backbone, Video, $, Paginator, _, loadingView) {

        var pageIdListToShowBackground = [
            "main-page",
            "auth-page",
            "signup-page",
            "scoreboard-page"
        ];

        return new (Backbone.View.extend({

            el : ".bgndVideo",

            initialize : function(options) {
                this.$el.YTPlayer();

                loadingView.show();
                setTimeout(loadingView.hide.bind(loadingView), 3000);

                Paginator.bind("CHANGE_PAGE", this.toggleVideo, this);
            },

            toggleVideo : function(pageId) {
                if (_.indexOf(pageIdListToShowBackground, pageId) != -1) {
                    this.$el.show();
                } else {
                    this.$el.hide();
                }
            }

        }))();
    }
);