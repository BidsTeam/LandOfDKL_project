package app.ApiV2;

import app.AccountMap.AccountMap;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.Set;

public class WebChat {
    private static WebChat chatInstance;
    private WebChat() {}
    private AccountMap cache = AccountMap.getInstance();


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
            LogFactory.getInstance().getSessionLogger().error("WebChat/sendMessage",e);
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
            LogFactory.getInstance().getSessionLogger().fatal(e);
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
            if (!userConnections.isEmpty()){
                for (Session connection : userConnections) {
                    connection.getRemote().sendString(jsonResp);
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal(e);
        }
    }
}
