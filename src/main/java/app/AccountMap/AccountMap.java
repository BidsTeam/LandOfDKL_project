package app.AccountMap;

import DAO.logic.UserLogic;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class AccountMap {

    private static AccountMap AccountMap = null;

    private HashMap<Integer, UserLogic> cachedAccounts = new HashMap<>();

    public static AccountMap getInstance(){
        if (AccountMap == null){
            AccountMap = new AccountMap();
        }
        return AccountMap;
    }

    private AccountMap(){
    }

    public void putUser(UserLogic user) {
        int userID = user.getId();
        cachedAccounts.put(userID, user);
    }

    public UserLogic getUser(int id) {
        return cachedAccounts.get(id);
    }

    public int getLoggedCounter() {
        return cachedAccounts.size();
    }




}
