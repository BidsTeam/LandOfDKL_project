/**
 * Created by rikimaru on 11.03.15.
 */
define(['backbone', "views/scoreboardList", "../../templates/scoreboard_page"], function(Backbone, scoreboardlistView, scoreboardPageTmpl) {

    var scoreboardPageView = Backbone.View.extend({
        el : "#template-container",
        initialize : function(options) {
        },
        render : function() {
            $(".logo-container__logo").show();
            this.$el.html(scoreboardPageTmpl());
            scoreboardlistView.render();
        }
    });

    return new scoreboardPageView();
});