package StubClasses;

import DAO.logic.UserLogic;
import service.DataBase.DBUserService;

import java.util.List;

/**
 * Created by andreybondar on 21.05.15.
 */
public class DBUserServiceStub implements DBUserService {
    @Override
    public boolean addUser(UserLogic user) {
        if (user.getUsername() == "testUser") {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateUser(UserLogic user) {

    }

    @Override
    public UserLogic getUserById(int id) {
        return null;
    }

    @Override
    public UserLogic getUserByUsername(String username) {
        return null;
    }

    @Override
    public UserLogic getUserByAuth(String username, String password) {
        return null;
    }

    @Override
    public List getAllUsers() {
        return null;
    }

    @Override
    public void deleteUser(UserLogic user) {

    }

    @Override
    public int getUserCounter() {
        return 0;
    }

    @Override
    public List<UserLogic> getAllUserRating(int count) {
        return null;
    }

    @Override
    public boolean isDeckFull(int userID) {
        return false;
    }
}
