package app.util;

import DAO.logic.UserLogic;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class AccountCache {
    private static int logedCounter = 0;

    private static HashMap<Integer, UserLogic> CachedAccounts = new HashMap<>();
    private static HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public void putUser(UserLogic user) {
        logedCounter++;
        int userID = user.getId();
        CachedAccounts.put(userID, user);
    }

    public void putNewSession(int userID, Session newSession) {
        Set bufSet = connections.get(userID);
        if (bufSet == null) {
            bufSet = new HashSet<>();
            bufSet.add(newSession);
            connections.put(userID, bufSet);
        } else {
            bufSet.add(newSession);
        }
    }

    public HashMap getAllSessions() {
        return connections;
    }

    public Set getUserSessions(int id) {
        return connections.get(id);
    }

    public UserLogic getUser(int id) {
        return CachedAccounts.get(id);
    }

    public int getLogedCounter() {
        return logedCounter;
    }


}
