/**
 * Created by rikimaru on 11.03.15.
 */
define(
    [
        'pageView',
        "templates/scoreboard_page"
    ],function(pageView, scoreboardPageTmpl) {

        var scoreboardPageView = pageView.extend({

            _construct : function(options) {

                require(['views/scoreboardList'], function(scoreboardListView) {
                    scoreboardListView.render();
                });
            },

            render : function() {
                require(['views/scoreboardList'], function(scoreboardListView) {
                    scoreboardListView.render();
                });
            }
        });

        return new scoreboardPageView({pageHtml : scoreboardPageTmpl()});
    }
);