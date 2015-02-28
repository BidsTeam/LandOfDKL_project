$(function(){

    window.Game = {};

    Game.Config = {
        name : "Land Of Lady, Dragon and Knight",
        authUrl : "/api/v1/auth/signin"
    };

    Game.Views = {};
    Game.Models = {};
    Game.Routers = {};
    var Forms = {};

    var emailRegexp = new RegExp("[a-zA-Z]+[a-zA-z0-9]+@[a-zA-z]+\.[a-z]{2,}");

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

	Game.Views.PageView = Backbone.View.extend({
		el : "#app-container",
		initialize : function(options) {
			this.model.bind('change', this.render, this);
		},
		template_list: {
			"mainPage": _.templateFromUrl('/templates/main_page.tpl'),
        	"authPage": _.templateFromUrl('/templates/auth_page.tpl'),
        	"scoreboardPage": _.templateFromUrl('/templates/scoreboard_page.tpl'),
        	"gamePage": _.templateFromUrl('/templates/game_page.tpl'),
            signupPage : _.templateFromUrl("/templates/signup_page.tpl")
		},
		render : function() {
			var template = this.model.get("template");
        	this.$el.html(this.template_list[template]());
        	return this;
		}
	});

    Game.Views.formFieldView = Backbone.View.extend({
        events : {
            "input" : "render"
        },
        initialize : function(options) {
            this.model = new (Backbone.Model.extend({}))();
            this.validate = options.validate.bind(this);
            this.model.bind("change", this.changeValidStatus, this);
            this.trigger("change");
        },
        render : function() {
            this.model.set({val : this.$el.val()});
        },
        setInvalid : function() {
            this.$el.addClass("invalid");
        },
        setSuccess : function() {
            this.$el.removeClass("invalid");
        },
        changeValidStatus : function() {
            if( this.validate() ) {
                this.validStatus = true;
            } else {
                this.validStatus = false;
            }
        },
        isValid : function() {
            return this.validStatus;
        },
        getVal : function() {
            return this.model.get("val");
        }
    });

    Game.Views.enterFormView = Backbone.View.extend({
        events : {
            "input" : "validate"
        },
        initialize : function(options) {
            this.validate = options.validate.bind(this);
            this.fields = options.fields;
        }
    });

    Game.Models.User = Backbone.View.extend({

    });

	Game.Models.PageModel = Backbone.Model.extend({
		defaults : {
			"template" : "mainPage"
		}
	});

	Game.Routers.GameRouter = Backbone.Router.extend({

		routes: {
			"(#)(/)" : "mainPageInit",
			"game(/)" : "gamePageInit",
			"auth(/)" : "authPageInit",
			"signup(/)" : "signupPageInit",
			"scoreboard" : "scoreboardPageInit"
		},

		initialize : function() {
			this.pageModel = new Game.Models.PageModel();
			this.view = new Game.Views.PageView({model : this.pageModel});
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

            Forms.authForm = new Game.Views.enterFormView({
                el : "#auth-form",

                fields : {
                    username : new Game.Views.formFieldView({
                        el : "#auth-login-field",
                        validate : function() {
                            if (this.getVal() == "") {
                                this.setInvalid();
                                return false;
                            } else {
                                this.setSuccess();
                                return true;
                            }
                        }
                    }),
                    password : new Game.Views.formFieldView({
                        el : "#auth-password-field",
                        validate : function() {
                            if (this.getVal() == "") {
                                this.setInvalid();
                                return false;
                            } else {
                                this.setSuccess();
                                return true;
                            }
                        }
                    })
                },

                validate : function() {
                    var valid = true;
                    for( var key in this.fields ) {
                        if( !this.fields[key].isValid() ) {
                            valid = false;
                            break;
                        }
                    }

                    if( valid ) {
                        this.$el.find("input[type='submit']").removeAttr("disabled");
                        return true;
                    } else {
                        this.$el.find("input[type='submit']").attr("disabled", "disabled");
                        return false;
                    }
                }
            });
            //
            Forms.authForm.bind("sendingStart", function() {
                this.$el.find("input[type='submit']").attr("disabled", "disabled");
            }, Forms.authForm);

            Forms.authForm.bind("sendingFailed", function() {
                this.$el.find("input[type='submit']").removeAttr("disabled");
            }, Forms.authForm);
            //
			$('#auth-form').ajaxForm({
                beforeSubmit : function(data, form, options) {
                    if( !Forms.authForm.validate() ) {
                        Forms.authForm.trigger("sendingFailed");
                        return false;
                    }
                    Forms.authForm.trigger("sendingStart");
                    return true;
                },
				success : function(msg) {
                    alert("Ты тип авторизован! Глянь в консоль чтобы увидеть че пришло в ответе!");
					console.log(msg);
				},
                error : function(msg) {
                    Forms.authForm.trigger("sendingFailed");
                    alert("АВТОРИЗАЦИЯ ПРОВАЛЕНА! Глянь в консоль чтобы увидеть че пришло в ответе!");
                    console.log(msg);
                }
			});
		},

		signupPageInit : function() {
            this.pageModel.set({template : "signupPage"});

            Forms.signupForm = new Game.Views.enterFormView({

                el : "#signup-form",

                fields : {
                    email : new Game.Views.formFieldView({
                        el: "#signup-email-field",
                        validate: function () {
                            if( !emailRegexp.test(this.getVal()) ) {
                                this.setInvalid();
                                return false;
                            } else {
                                this.setSuccess();
                                return true;
                            }
                        }
                    }),
                    password : new Game.Views.formFieldView({
                        el: "#signup-password-field",
                        validate: function() {
                            if (this.getVal() == "") {
                                this.setInvalid();
                                return false;
                            } else {
                                this.setSuccess();
                                return true;
                            }
                        }
                    }),
                    username : new Game.Views.formFieldView({
                        el : "#signup-username-field",
                        validate: function() {
                            if (this.getVal() == "") {
                                this.setInvalid();
                                return false;
                            } else {
                                this.setSuccess();
                                return true;
                            }
                        }
                    })
                },

                validate : function() {
                    var valid = true;
                    for( var key in this.fields ) {
                        if( !this.fields[key].isValid() ) {
                            valid = false;
                            break;
                        }
                    }
                    if( $("#signup-password-field").val() !== $("#signup-password-repeat-field").val() ) {
                        valid = false;
                    }

                    if( valid ) {
                        this.$el.find("input[type='submit']").removeAttr("disabled");
                        return true;
                    } else {
                        this.$el.find("input[type='submit']").attr("disabled", "disabled");
                        return false;
                    }
                }
            });

            Forms.signupForm.bind("sendingStart", function() {
                this.$el.find("input[type='submit']").attr("disabled", "disabled");
            }, Forms.signupForm);

            Forms.signupForm.bind("sendingFailed", function() {
                this.$el.find("input[type='submit']").removeAttr("disabled");
            }, Forms.signupForm);

            $('#signup-form').ajaxForm({
                beforeSubmit : function(data, form, options) {
                    if( !Forms.signupForm.validate() ) {
                        Forms.signupForm.trigger("sendingFailed");
                        return false;
                    }
                    Forms.signupForm.trigger("sendingStart");
                    return true;
                },
                success : function(msg) {
                    alert("Ты зареган! Смотри в консоль");
                    console.log(msg);
                },
                error : function(msg) {
                    Forms.signupForm.trigger("sendingFailed");
                    console.log(msg);
                    alert("Регистрация провалена! Смотри в консоль");
                }
            });
		},
		scoreboardPageInit : function() {
			this.pageModel.set({template:"scoreboardPage"});
		}
	});

	new Game.Routers.GameRouter();
	Backbone.history.start();
});