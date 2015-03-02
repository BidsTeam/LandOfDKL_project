$(function(){
    TemplateList = {
        "mainPage":     Util.templateFromUrl('/templates/main_page.tpl'),
        "authPage":     Util.templateFromUrl('/templates/auth_page.tpl'),
        "scoreboardPage":Util.templateFromUrl('/templates/scoreboard_page.tpl'),
        "gamePage":     Util.templateFromUrl('/templates/game_page.tpl'),
        "signupPage" :  Util.templateFromUrl("/templates/signup_page.tpl")
    };
    var Config = {
        name : "Land Of Lady, Dragon and Knight",
        authUrl : "/api/v1/auth/signin"
    };


    var Forms = {};


	Router = Backbone.Router.extend({

		routes: {
			"(#)(/)" : "mainPageInit",
			"game(/)" : "gamePageInit",
			"auth(/)" : "authPageInit",
			"signup(/)" : "signupPageInit",
			"scoreboard" : "scoreboardPageInit"
		},

		initialize : function() {
		},

		mainPageInit : function() {
            Views.mainView.render("mainPage");
		},

		gamePageInit : function() {
            Views.mainView.render("gamePage");
		},

		authPageInit : function() {
            Views.mainView.render("authPage");
		},

		signupPageInit : function() {
            Views.signupView.render("signupPage");

        },
		scoreboardPageInit : function() {
            Views.mainView.render("scoreboardPage");
		}
	});
    Models = {
        signupModel: new SignupModel()
    };
    Views = {
        signupView: new SignupView(),
        mainView: new MainView()
    };

	router = new Router();
	Backbone.history.start();
});