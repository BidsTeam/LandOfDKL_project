var SignupView = Backbone.View.extend({
    el : "#app-container",


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

    initialize: function() {
        this.model = Models.signupModel;
    },

    render: function(tpl){
        this.$el.html(TemplateList[tpl]());
    },

    getInput: function(name) {
        return this.$el.find('[name="' + name + '"]');
    },

    onFormSubmit: function(e) {
        e.preventDefault();
        var model = this.model;

        this.$el.find('input[name]').each(function() {
            model.set(this.name, this.value);
        });
        //todo this.model.save(); Я настрою потом sync
    },

    onInputChange: function(e) {
        this.model.set(e.target.name, e.target.value);
        var result = this.model.validateOne(e.target.name, e.target.value);
        if (result !== true) this.showInputErrors(e.target, ["error"])
    },

    resetInputErrors: function(target) {
        var $target = $(target);
        $target.removeClass('invalid');
        $target.parent().find('.error').remove();
    },

    showInputErrors: function(target, errorList) { // Скорее всего лишний метод(имеется ввиду массив error), нужно подумать
        var $target = $(target);
        var errorHTML = '';

        this.resetInputErrors(target);

        for (var i = 0; i < errorList.length; i++) {
            errorHTML += this.templates.error({error: errorList[i]});
        }

        $target.addClass('invalid');
        $target.parent().append(errorHTML);
    }
});


//Forms.signupForm = new EnterFormView({
//
//    el : "#signup-form",
//
//    fields : {
//        email : new FormFieldView({
//            el: "#signup-email-field",
//            validate: function () {
//                if( !emailRegexp.test(this.getVal()) ) {
//                    this.setInvalid();
//                    return false;
//                } else {
//                    this.setSuccess();
//                    return true;
//                }
//            }
//        }),
//        password : new FormFieldView({
//            el: "#signup-password-field",
//            validate: function() {
//                if (this.getVal() == "") {
//                    this.setInvalid();
//                    return false;
//                } else {
//                    this.setSuccess();
//                    return true;
//                }
//            }
//        }),
//        username : new FormFieldView({
//            el : "#signup-username-field",
//            validate: function() {
//                if (this.getVal() == "") {
//                    this.setInvalid();
//                    return false;
//                } else {
//                    this.setSuccess();
//                    return true;
//                }
//            }
//        })
//    },
//
//    validate : function() {
//        var valid = true;
//        for( var key in this.fields ) {
//            if( !this.fields[key].isValid() ) {
//                valid = false;
//                break;
//            }
//        }
//        if( $("#signup-password-field").val() !== $("#signup-password-repeat-field").val() ) {
//            valid = false;
//        }
//
//        if( valid ) {
//            this.$el.find("input[type='submit']").removeAttr("disabled");
//            return true;
//        } else {
//            this.$el.find("input[type='submit']").attr("disabled", "disabled");
//            return false;
//        }
//    }
//});
//
//Forms.signupForm.bind("sendingStart", function() {
//    this.$el.find("input[type='submit']").attr("disabled", "disabled");
//}, Forms.signupForm);
//
//Forms.signupForm.bind("sendingFailed", function() {
//    this.$el.find("input[type='submit']").removeAttr("disabled");
//}, Forms.signupForm);
//
//$('#signup-form').ajaxForm({
//    beforeSubmit : function(data, form, options) {
//        if( !Forms.signupForm.validate() ) {
//            Forms.signupForm.trigger("sendingFailed");
//            return false;
//        }
//        Forms.signupForm.trigger("sendingStart");
//        return true;
//    },
//    success : function(msg) {
//        alert("Ты зареган! Смотри в консоль");
//        console.log(msg);
//    },
//    error : function(msg) {
//        Forms.signupForm.trigger("sendingFailed");
//        console.log(msg);
//        alert("Регистрация провалена! Смотри в консоль");
//    }
//});
//},