/**
 * Created by rikimaru on 26.05.15.
 */

define(
    [
        "backbone",
        "pageView",
        "templates/mobile/game_page",
        "views/game/card"
    ], function(Backbone, PageView, MobileGamePageTemplate, cardView) {

        return new (PageView.extend({

            _construct : function(options) {
                var card;
                for (var i = 0; i < 15; i++) {
                    card = new cardView({cardId : 9});
                    this.$(".cards-container").append(card.$el);
                }
            }

        }))({pageHtml : MobileGamePageTemplate()});
    }
);