FormFieldView = Backbone.View.extend({
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
