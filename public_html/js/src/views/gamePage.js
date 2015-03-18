/**
 * Created by rikimaru on 12.03.15.
 */
define(['backbone', '../../templates/game_page'], function(Backbone, gamePageTmpl) {

    var gamePage = Backbone.View.extend({
        el : "#template-container",
        render : function() {
            $(".logo-container__logo").hide();
            this.$el.html(gamePageTmpl());
        }
    });

    return new gamePage();
});