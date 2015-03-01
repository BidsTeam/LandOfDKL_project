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
		},

		gamePageInit : function() {
		},

		authPageInit : function() {

		},

		signupPageInit : function() {
            Views.signupView.render("signupPage");

        },
		scoreboardPageInit : function() {

		}
	});
    Models = {
        signupModel: new SignupModel()
    };
    Views = {
        signupView: new SignupView()
    };

	router = new Router();
	Backbone.history.start();
});