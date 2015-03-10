/**
 * Created by rikimaru on 08.03.15.
 */
define(
    "templater",
    [
        "../templates/main_page",
        "../templates/auth_page",
        "../templates/scoreboard_page",
        "../templates/game_page",
        "../templates/signup_page"
    ],
    function(
        mainPage,
        authPage,
        scoreboardPage,
        gamePage,
        signupPage
    ) {


    var Templater = function() {
        var self = this;

        self.templateList = {
            mainPage : mainPage,
            authPage : authPage,
            scoreboardPage : scoreboardPage,
            gamePage : gamePage,
            signupPage : signupPage
        };

        this.getTemplate = function(templateName, args) {
            return self.templateList[templateName](args);
        }
    };

    return new Templater();
});