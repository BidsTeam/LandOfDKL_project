package app.utlit;

import DAO.logic.User;

import java.util.LinkedHashMap;

/**
 * Created by andreybondar on 04.03.15.
 */
public class AccountCache {
    static int logedCounter = 0;

    static LinkedHashMap <Integer, User> CachedAccounts = new LinkedHashMap<>();

    public void putUser(User user) {
        logedCounter++;
        int userID = user.getId();
        System.out.println("putted");
        CachedAccounts.put(userID, user);
    }

    public User getUser(int id) {
        System.out.println("test3");
        return CachedAccounts.get(id);
    }

}
