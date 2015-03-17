package app.AccountCache;

import DAO.logic.UserLogic;

import java.util.HashMap;


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

    public void putUser(UserLogic user) {
        loggedCounter++;
        int userID = user.getId();
        System.out.println("putted");
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


}
