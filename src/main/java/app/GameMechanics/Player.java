package app.GameMechanics;

import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.Api.User;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Player {
    private UserLogic userInfo;
    private HashSet<Session> userConnections;
    private String username;
    private int userID;
    //private AccountMap cache = AccountMap.getInstance();


    public Player(UserLogic user) {
        username = user.getUsername();
        userID = user.getId();
        userConnections = AccountMap.getInstance().getUserSessions(user.getId());
    }

    public String getUsername() {
        return username;
    }
    public int getUserID() {return userID; }

    public void sendResponse(JSONObject json) {
        try {
            if (!userConnections.isEmpty()){
                for (Session connection : userConnections) {
                    connection.getRemote().sendString(json.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal(e);
        }
    }



}