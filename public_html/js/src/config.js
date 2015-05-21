define("config", [], function() {

    var Config = {
        name : "Land Of Lady, Dragon and Knight",
        apiUrl : "/api",
        socketAddressList : {
            socketActionsUrl : "ws://" + location.host + "/socket",
            mobileUrl : "ws://" + location.host + "/socket"
        },

        testMode : true
    };

    return Config;
});