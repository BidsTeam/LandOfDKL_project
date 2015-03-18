/**
 * Created by rikimaru on 08.03.15.
 */
define(
    "templater",
    [
        //"../templates/main_page",
        //"../templates/auth_page",
        //"../templates/scoreboard_page",
        //"../templates/game_page",
        //"../templates/signup_page"
    ],
    function(
        //mainPage,
        //authPage,
        //scoreboardPage,
        //gamePage,
        //signupPage
    ) {


    var Templater = function() {
        var self = this;

        this.getTemplate = function(templateName, args) {
            var resHtml = "";
            require(["../templates/"+templateName], function(tmpl) {
                resHtml = tmpl(args);
            });
            return resHtml;
        }
    };

    return new Templater();
});