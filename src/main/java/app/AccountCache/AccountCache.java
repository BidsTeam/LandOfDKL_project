package app.AccountCache;

import DAO.logic.UserLogic;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class AccountCache {

    private static AccountCache accountCache = null;

    public static AccountCache getInstance(){
        if (accountCache == null){
            accountCache = new AccountCache();
        }
        return accountCache;
    }

    private AccountCache(){
    }

    private int loggedCounter = 0;
    private HashMap<Integer, UserLogic> CachedAccounts = new HashMap<>();
    private HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public void putUser(UserLogic user) {
        loggedCounter++;
        int userID = user.getId();
        CachedAccounts.put(userID, user);
    }



    public UserLogic getUser(int id) {
        return CachedAccounts.get(id);
    }

    public int getLoggedCounter() {
        return loggedCounter;
    }

    public void setLoggedCounter(int val){
        loggedCounter = val;
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

    public void removeSession(int userID, Session session) {
        Set sessions = connections.get(userID);
        sessions.remove(session);
    }

    public HashMap getAllSessions() {
        return connections;
    }

    public Set getUserSessions(int id) {
        return connections.get(id);
    }


}
