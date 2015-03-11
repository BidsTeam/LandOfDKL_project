package app.util;

import DAO.logic.UserLogic;

import java.util.HashMap;


public class AccountCache {
    private static int logedCounter = 0;

    private static HashMap<Integer, UserLogic> CachedAccounts = new HashMap<>();

    public void putUser(UserLogic user) {
        logedCounter++;
        int userID = user.getId();
        System.out.println("putted");
        CachedAccounts.put(userID, user);
    }

    public UserLogic getUser(int id) {
        return CachedAccounts.get(id);
    }

    public int getLogedCounter() {
        return logedCounter;
    }


}
