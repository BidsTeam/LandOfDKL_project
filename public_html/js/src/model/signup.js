SignupModel = Backbone.Model.extend({
    url: '/signup',

    defaults: {
        username: '',
        email: '',
        password: '',
        repeat_password: ''
    },

    specialChars:  "<>@!#$%^&*()_+[]{}?:;|'\"\\,./~`-=", //todo Вынести куда-нибудь, ведь валидация много где нжуно

    validateOne: function(key,val){
        // Вообще есть библиотека https://github.com/thedersen/backbone.validation , хз нужна она или нет, пока решил не нагружать проект
        // У нее лучше решение проблем с паролями
        // http://jsfiddle.net/thedersen/c3kK2/ Демо
        var obj = [];
        switch(key) {
            case "email": {
                var emailRegexp = /^[a-zA-Z]+[a-zA-Z0-9_-]*@[a-zA-Z]+\.[a-z]{2,}$/;
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
                    if (!Util.isValidString((val))){
                        obj.push({key:key,result:"Please not use special chars"});
                    } else {
                        obj.push({key: key, result: true});
                        obj.push({key: "repeat_password", result: true});
                    }
                }
                break;
            }
            case "repeat_password":{
                if ( (this.get("password") != val) || (val == "") ) {
                    obj.push({key:key,result:"Password's not match"});
                    obj.push({key:"password",result:"Password's not match"});
                } else {
                    if (!Util.isValidString((val))){
                        obj.push({key:key,result:"Please not use special chars"});
                    }
                    obj.push({key:key,result:true});
                    obj.push({key:"repeat_password",result:true});
                }
                break;
            }
            default: {
                if (!Util.isValidString((val))) {
                    obj.push({key:key,result:"Not empty field"})
                } else {
                    obj.push({key: key, result: true})
                }
                break;
            }
        }
        return obj;
    },

    validateForm: function(fields) {
        var bool = true;
        _.forEach(fields,function(val,key){
            var validateResult = this.validateOne(key,val);
            _.forEach(validateResult,function(val,key){
                if (val.result !== true){
                    bool = false;
                    return false; // return выходит из forEach, а не функции
                }
            })
            if (!bool){
                return false;
            }

        },this);
        return bool;
    }
});
