package app.AccountMap;

import DAO.logic.UserLogic;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class AccountMap {

    private static AccountMap AccountMap = null;

    public static AccountMap getInstance(){
        if (AccountMap == null){
            AccountMap = new AccountMap();
        }
        return AccountMap;
    }

    private AccountMap(){
    }

    private HashMap<Integer, UserLogic> CachedAccounts = new HashMap<>();
    private HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public void putUser(UserLogic user) {
        int userID = user.getId();
        CachedAccounts.put(userID, user);
    }

    public UserLogic getUser(int id) {
        return CachedAccounts.get(id);
    }

    public int getLoggedCounter() {
        return CachedAccounts.size();
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

    public HashMap<Integer, Set<Session>> getAllSessions() {
        return connections;
    }

    public Set<Session> getUserSessions(int id) {
        return connections.get(id);
    }


}
