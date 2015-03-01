SignupModel = Backbone.Model.extend({
    url: '/signup',

    defaults: {
        username: '',
        email: '',
        password: '',
        repeat_password: ''
    },

    validateOne: function(key,val){
        var bool = true;
        switch(key) {
            case "email":
            {
                var emailRegexp = /^[a-zA-Z]+[a-zA-Z0-9_-]*@[a-zA-Z]+\.[a-z]{2,}$/;
                console.log(val);
                if ( !emailRegexp.test(val)  || val == "") {
                    console.log(false);
                    bool = false;
                }
                break;
            }
            default:
            {
                if (!val) {
                    bool = false;
                }
                break;
            }
        }
        return bool;
    },

    validate: function(fields) {
        var bool = true;
        _.forEach(fields,function(val,key){
            console.log(key);
        });
        return bool;
    }
});
