package app.util;

import DAO.logic.User;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class AccountCache {
    private static int logedCounter = 0;

    private static HashMap<Integer, User> CachedAccounts = new LinkedHashMap<>();

    public void putUser(User user) {
        logedCounter++;
        int userID = user.getId();
        System.out.println("putted");
        CachedAccounts.put(userID, user);
    }

    public User getUser(int id) {
        return CachedAccounts.get(id);
    }

    public int getLogedCounter() {
        return logedCounter;
    }


}
