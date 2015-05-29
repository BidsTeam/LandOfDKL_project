package StubClasses;

import DAO.logic.UserLogic;
import messageSystem.MessageSystem;
import service.DataBase.DBUserService;

import java.util.ArrayList;
import java.util.List;


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
    public UserLogic getUserByAuth(String username, String password, MessageSystem messageSystem) {
        return new UserLogic("admin", "admin", "admin@mail.ru");
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
        List<UserLogic> userList = new ArrayList<>();
        userList.add(new UserLogic("test", "test", "test@mail.ru"));
        return userList;
    }
}
