SignupModel = Backbone.Model.extend({
    url: '/signup',

    defaults: {
        username: '',
        email: '',
        password: '',
        repeat_password: ''
    },

    validateOne: function(key,val){
        // Вообще есть библиотека https://github.com/thedersen/backbone.validation , хз нужна она или нет, пока решил не нагружать проект
        // У нее лучше решение проблем с паролями
        // http://jsfiddle.net/thedersen/c3kK2/ Демо
        var obj = [];
        switch(key) {
            case "email": {
                var emailRegexp = /^[a-zA-Z]+[a-zA-Z0-9_-]*@[a-zA-Z]+\.[a-z]{2,}$/;
                console.log(val);
                if ( !emailRegexp.test(val)  || val == "") {
                    obj.push({key:key,result:"Please enter valid email. \n Example: SBTeam@mail.ru"});
                } else {
                    obj.push({key:key,result:true});
                }
                break;
            }
            case "password":{
                //todo Сердце чует, не так нужно с паролями работать, что-то здесь не так !
                if ( (this.get("repeat_password") != "") && (val != this.get("repeat_password")) ) {
                    obj.push({key:key,result:"Password's not match"});
                    obj.push({key:"repeat_password",result:"Password's not match"});
                } else {
                    obj.push({key:key,result:true});
                    obj.push({key:"repeat_password",result:true});
                }
                break;
            }
            case "repeat_password":{
                if ( (this.get("password") != val) || (val == "") ) {
                    console.log("false");
                    obj.push({key:key,result:"Password's not match"});
                    obj.push({key:"repeat_password",result:"Password's not match"});
                } else {
                    obj.push({key:key,result:true});
                    obj.push({key:"repeat_password",result:true});
                }
                break;
            }
            default: {
                if (!val) {
                    obj.push({key:key,result:"Not empty field"})
                }
                break;
            }
        }
        return obj;
    },

    validate: function(fields) {
        var bool = true;
        _.forEach(fields,function(val,key){
            console.log(key);
        });
        return bool;
    }
});
