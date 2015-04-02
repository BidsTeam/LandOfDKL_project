define("config", [], function() {

    var Config = {
        name : "Land Of Lady, Dragon and Knight",
        apiUrl : "/api",
        socketChatUrl : "ws://" + location.host + "/socket"
    };

    return Config;
});