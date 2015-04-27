define(
    [
        "pageView",
        "models/signup",
        "templates/signup_page",
        "config",
        "jquery",
        "views/loading"
    ],
    function(pageView, signupModel, signupTmpl, Config, $, loading) {

        var SignupView = pageView.extend({

            events: {
                'submit': 'onFormSubmit',
                'change input[type!="submit"]': 'onInputChange',
                'blur input[type!="submit"]': 'onInputChange',
                'focus input': function(e) {
                    this.resetInputErrors(e.target);
                }
            },

            templates: {
                'error': _.template('<span class="error"><%=error%></span>')
            },

            _construct: function() {
                this.model = signupModel;
            },

            render: function(){
            },

            getInput: function(name) {
                return this.$el.find('[name="' + name + '"]');
            },

            onFormSubmit: function(e) {
                e.preventDefault();
                var model = this.model;

                this.$el.find('input[name]').each(function(key,val) {
                    $(val).trigger("change"); //Идеологически неправильно имитировать действия пользователя, но ничего умнее не придумал
                });

                this.$("input[type=submit]").attr("disabled", "disabled");

                $.ajax({
                    type: "POST",
                    url: Config.apiUrl+"/auth/signup",
                    data: model.toJSON(),
                    success: function(msg) {
                        console.log(msg);
                    },
                    beforeSend : function() {
                        loading.show();
                    },
                    complete : function(msg) {
                        this.$("input[type=submit]").removeAttr("disabled");
                        loading.hide();
                    }.bind(this)
                });
                //todo this.model.save(); Я настрою потом sync
            },

            onInputChange: function(e) {
                this.model.set(e.target.name, e.target.value);
                var result = this.model.validateOne(e.target.name, e.target.value);
                _.forEach(result,function(val,key){
                    //todo из-за паролей, пришлось фигню какую-то сотворить здесь. Есть идеи как оставить все как было до этого коммита, и чтобы без этого велосипеда
                    if (val.result != true){
                        this.showInputErrors(this.$el.find("[name="+val.key+"]"), val.result)
                    } else {
                        this.resetInputErrors(this.$el.find("[name="+val.key+"]"));
                    }
                },this);
                this.checkForm();
            },

            checkForm : function(){
                var valid = this.model.validateForm(this.model.toJSON());
                if( valid ) {
                    this.$el.find("[type=submit]").removeAttr("disabled");
                } else {
                    this.$el.find("[type=submit]").attr("disabled", "disabled");
                }
            },

            resetInputErrors: function(target) {
                var $target = $(target);
                $target.parent().removeClass('has-error');
                $target.parent().prev('.error').remove();
            },

            showInputErrors: function(target, error) {
                var $target = $(target);
                var errorHTML = '';

                this.resetInputErrors(target);
                errorHTML += this.templates.error({error: error});
                $target.parent().addClass('has-error');
                $target.parent().before(errorHTML);
            }
        });

        return new SignupView({pageHtml : signupTmpl()});
    }
);
