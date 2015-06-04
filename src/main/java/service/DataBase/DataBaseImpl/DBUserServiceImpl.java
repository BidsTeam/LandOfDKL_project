package service.DataBase.DataBaseImpl;


import DAO.logic.UserLogic;
import DAO.UserDAO;
import DAO.Impl.UserDAOImpl;
import messageSystem.MessageSystem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import service.DataBase.DBUserService;
import util.LogFactory;

import java.util.List;

public class DBUserServiceImpl implements DBUserService {

    UserDAO userDAO;

    public DBUserServiceImpl(Session session){
        userDAO = new UserDAOImpl(session);
    }

    public boolean addUser(UserLogic user){
        if (getUserByUsername(user.getUsername()) != null) {
           return false;
        } else {
            userDAO.addUser(user);
            return true;
        }
    }

    public void updateUser(UserLogic user){
        userDAO.updateUser(user);
    }

    public UserLogic getUserById(int id){
        UserLogic result = null;
        result = userDAO.getUserById(id);
        return result;
    }

    public UserLogic getUserByUsername(String username){
        UserLogic result = null;
        result = userDAO.getUserByUsername(username);
        return result;
    }

    public UserLogic getUserByAuth(String username, String password, MessageSystem messageSystem){
        UserLogic result = null;
        result = userDAO.getUserByAuth(username, password,messageSystem);
        return result;
    }

    public List getAllUsers(){
        List result = userDAO.getAllUsers();
        return result;
    }

    public void deleteUser(UserLogic user){
        userDAO.deleteUser(user);
    }

    public int getUserCounter(){
        int result = userDAO.getUserCounter();
        return result;
    }

    public List<UserLogic> getAllUserRating(int count){
        List<UserLogic> result = userDAO.getAllUserRating(count);
        return result;
    }
}
