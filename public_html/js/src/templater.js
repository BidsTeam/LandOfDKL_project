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

        var _templateList = {
            mainPage : mainPage,
            authPage : authPage,
            scoreboardPage : scoreboardPage,
            gamePage : gamePage,
            signupPage : signupPage
        };

        return {
            getTemplate: function (templateName, args) {
                return _templateList[templateName](args);
            }
        };
});