define("config", [], function() {

    var Config = {
        name : "Land Of Lady, Dragon and Knight",
        authUrl : "/api/auth/signin",
        socketChatUrl : "ws://" + location.host + "/socket"
    };

    return Config;
});