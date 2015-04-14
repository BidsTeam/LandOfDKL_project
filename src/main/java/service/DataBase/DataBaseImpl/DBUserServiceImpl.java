package service.DataBase.DataBaseImpl;


import DAO.logic.UserLogic;
import DAO.UserDAO;
import DAO.Impl.UserDAOImpl;
import org.hibernate.SessionFactory;
import service.DataBase.DBUserService;
import util.LogFactory;

import java.util.List;

public class DBUserServiceImpl implements DBUserService {

    UserDAO userDAO;

    public DBUserServiceImpl(SessionFactory sessionFactory){
        userDAO = new UserDAOImpl(sessionFactory);
    }

    public boolean addUser(UserLogic user){
        if (getUserById(user.getId()) != null) {
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

    public UserLogic getUserByAuth(String username, String password){
        UserLogic result = null;
        result = userDAO.getUserByAuth(username, password);
        return result;
    }

    public List getAllUsers(){
        List result = null;
        result = userDAO.getAllUsers();
        return result;
    }

    public void deleteUser(UserLogic user){
        userDAO.deleteUser(user);
    }

    public int getUserCounter(){
        int result = -1;
        result = userDAO.getUserCounter();
        return result;
    }

    public List<UserLogic> getAllUserRating(int count){
        List<UserLogic> result = null;
        result = userDAO.getAllUserRating(count);
        return result;
    }
}
