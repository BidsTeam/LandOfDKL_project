define("config", [], function() {

    var Config = {
        name : "Land Of Lady, Dragon and Knight",
        authUrl : "/api/v1/auth/signin",
        socketChatUrl : "ws://localhost:8080/chat"
    };

    return Config;
});