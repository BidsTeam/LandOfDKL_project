package app.ApiV2;

import app.AccountCache.AccountCache;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class WebChat {
    private static WebChat chatInstance;
    private WebChat() {}
    private AccountCache cache = AccountCache.getInstance();


    public static WebChat getChatInstance() {
        if (chatInstance == null) {
            chatInstance = new WebChat();
        }
        return chatInstance;
    }

    public void sendMessage(JSONObject json) {
        JSONObject response = new JSONObject();
        response.put("status", 0);
        JSONObject responseBody = new JSONObject();
        try {
            responseBody.put("author", json.get("author").toString());
            responseBody.put("message", json.get("message").toString());
        } catch (Exception e){
            //todo найти все дубликаты данного err print и вынести в функцию такую идеологию (в случае предусмотренных ошибок, а не глобальных)
            //todo С указанием ссылки(на файл) и еще кучей всего
            System.err.println(e.getMessage() + "File: " + e.getStackTrace()[2].getFileName() +" Line number: "+ e.getStackTrace()[2].getLineNumber());
            responseBody.put("author", "err");
            responseBody.put("message", "err");

        }
        response.put("body", responseBody);
        String jsonResp = response.toString();

        HashMap<Integer, Set<Session>> sessions = cache.getAllSessions();
        try {
            for (Set<Session> userConnections : sessions.values()) {
                for (Session connection : userConnections) {
                    connection.getRemote().sendString(jsonResp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPrivateMessage(JSONObject json, int recivierID) {
        JSONObject response = new JSONObject();
        response.put("status", 1);
        JSONObject responseBody = new JSONObject();
        responseBody.put("author",json.get("author"));
        responseBody.put("message", json.get("message"));
        response.put("body", responseBody);
        String jsonResp = response.toString();

        try {
            Set<Session> userConnections = cache.getUserSessions(recivierID);
            for (Session connection : userConnections) {
                 connection.getRemote().sendString(jsonResp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
