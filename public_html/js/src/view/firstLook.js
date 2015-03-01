var EnterFormView = Backbone.View.extend({
    events : {
        "input" : "validate"
    },
    initialize : function(options) {
        this.validate = options.validate.bind(this);
        this.fields = options.fields;
    }
});
