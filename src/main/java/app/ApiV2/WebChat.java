package app.ApiV2;

import app.util.AccountCache;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by andreybondar on 18.03.15.
 */
public class WebChat {
    private static WebChat chatInstance;
    private WebChat() {}
    private AccountCache cache = new AccountCache();


    public static WebChat getChatInstance() {
        if (chatInstance == null) {
            chatInstance = new WebChat();
        }
        return chatInstance;
    }

    public void sendMessege(JSONObject json) {
        JSONObject response = new JSONObject();
        response.put("status", 0);
        JSONObject responseBody = new JSONObject();
        responseBody.put("author", json.get("author").toString());
        responseBody.put("message", json.get("message").toString());
        response.put("body", responseBody);
        String jsonResp = response.toString();

        HashMap sessions = cache.getAllSessions();
        try {
            for (Object userConnections : sessions.values()) {
                for (Object connection : (Set) userConnections) {
                    ((Session) connection).getRemote().sendString(jsonResp);
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
            Set userConnections = cache.getUserSessions(recivierID);
            for (Object connection : (Set) userConnections) {
                ((Session) connection).getRemote().sendString(jsonResp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
