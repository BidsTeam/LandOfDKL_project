/**
 * Created by rikimaru on 11.03.15.
 */
define(['backbone', "templater", "views/main"], function(Backbone, Templater, mainView) {

    var scoreboardView = Backbone.View.extend({
        initialize : function(options) {
        },
        render : function() {
            $.ajax({
                url : "http://localhost:8080/api/user/top",
                data : {count : 10},
                success : function(msg) {
                    var data = JSON.parse(msg);
                    console.log(data.response);
                    mainView.renderHtml(Templater.getTemplate("scoreboardPage", data.response));
                }
            });
        }
    });

    return new scoreboardView();
});