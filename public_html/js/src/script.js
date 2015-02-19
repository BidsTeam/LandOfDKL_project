$(function(){

	_.mixin({templateFromUrl: function (url, data, settings) {
	    var templateHtml = "";
	    this.cache = this.cache || {};
	    if (this.cache[url]) {
	        templateHtml = this.cache[url];
	    } else {
	        $.ajax({
	            url: url,
	            method: "GET",
	            async: false, // Говнокод, буду решать
	            beforeSend: function( xhr ) {
   					 xhr.overrideMimeType( "text/plain; charset=utf-8" ); // без этого, он попадает в error
  				},
	            success: function(data) {	            		            	
	                templateHtml = data;
	            },	            
	            error: function(data){	            	
	            	console.log("ERROR templateFromUrl");
	            }
	        });
	        this.cache[url] = templateHtml;
	    }
	    return _.template(templateHtml, data, settings);
	}});

	window.Game = {};

	Game.Config = {
		name : "Land Of Lady, Dragon and Knight",
		authUrl : "/api/v1/auth/signin"
	};


	Game.PageView = Backbone.View.extend({
		id : "app-container",
		initialize : function(options) {
			this.model.bind('change', this.render, this);
		},
		template_list: {
			"mainPage": _.templateFromUrl('/templates/main_page.js'),
        	"authPage": _.templateFromUrl('/templates/auth_page.js'),
        	"scoreboardPage": _.templateFromUrl('/templates/scoreboard_page.js'),
        	"gamePage": _.templateFromUrl('/templates/game_page.js')
		},
		render : function() {
			var template = this.model.get("template");    			
        	$("#"+this.id).html(this.template_list[template]());            	
        	return this;
		}
	});

	Game.PageModel = Backbone.Model.extend({
		defaults : {
			"template" : "mainPage"
		}
	});

	Game.GameRouter = Backbone.Router.extend({
		routes: {
			"(#)(/)" : "mainPageInit",
			"game(/)" : "gamePageInit",
			"auth(/)" : "authPageInit",
			"signup(/)" : "signupPageInit",
			"scoreboard" : "scoreboardPageInit"
		},

		initialize : function() {
			this.pageModel = new Game.PageModel();
			this.view = new Game.PageView({model : this.pageModel});
			this.pageModel.trigger("change");
		},
		mainPageInit : function() {
			this.pageModel.set({template:"mainPage"});
		},
		gamePageInit : function() {
			this.pageModel.set({template:"gamePage"});
		},
		authPageInit : function() {
			this.pageModel.set({template:"authPage"});
			$('#auth-form').ajaxForm({
				success : function(msg) {
					alert(msg);
				}
			});
		},
		signupPageInit : function() {

		},
		scoreboardPageInit : function() {
			this.pageModel.set({template:"scoreboardPage"});
		}
	});

	new Game.GameRouter();
	Backbone.history.start();
});