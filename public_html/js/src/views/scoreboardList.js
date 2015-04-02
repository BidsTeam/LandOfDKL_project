/**
 * Created by rikimaru on 12.03.15.
 */
define(["backbone", "templates/scoreboard_list"], function(Backbone, listTmpl) {

    var scoreboardList = Backbone.View.extend({

        events : {},

        initialize : function(options) {
            this.setElement("#rating-list");
        },

        render : function(){

            var getThis = function() {
                return this;
            }.bind(this);

            $.ajax({

                url : "http://"+location.host+"/api/user/top",
                data : {count : 10},

                beforeSend : function() {
                    getThis().$el.html("<img class='ajax-loader-indicator' src='images/ajax-loader.gif'>");
                },

                success : function(msg) {
                    var data = JSON.parse(msg);
                    getThis().$el.html(listTmpl(data.response));
                },

                error : function(msg) {
                    console.log(msg);
                    getThis().$el.html("Error!");
                },

                complete : function(msg) {
                    $(".ajax-loader-indicator").remove();
                }
            });
        }
    });

    return new scoreboardList();
});