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
        _.forEach(result,function(val,key){
            //todo из-за паролей, пришлось хуйню какую-то сотворить здесь. Есть идеи как оставить все как было до этого коммита, и чтобы без этого велосипеда
            if (val.result != true){
                this.showInputErrors(this.$el.find("[name="+val.key+"]"), val.result)
            } else {
                this.resetInputErrors(this.$el.find("[name="+val.key+"]"));
            }
        },this)
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
