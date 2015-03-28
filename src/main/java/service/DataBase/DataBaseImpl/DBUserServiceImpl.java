package service.DataBase.DataBaseImpl;


import DAO.logic.UserLogic;
import DAO.UserDAO;
import DAO.Impl.UserDAOImpl;
import service.DataBase.DBUserService;

import java.util.List;

public class DBUserServiceImpl implements DBUserService {

    UserDAO userDAO;

    public DBUserServiceImpl(){
        userDAO = new UserDAOImpl();
    }

    public void addUser(UserLogic user){
        try {
            userDAO.addUser(user);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
    }

    public void updateUser(UserLogic user){
        try {
            userDAO.updateUser(user);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
    }

    public UserLogic getUserById(int id){
        UserLogic result = null;
        try {
            result = userDAO.getUserById(id);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }

    public UserLogic getUserByUsername(String username){
        UserLogic result = null;
        try {
            result = userDAO.getUserByUsername(username);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }

    public UserLogic getUserByAuth(String username, String password){
        UserLogic result = null;
        try {
            result = userDAO.getUserByAuth(username, password);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }

    public List getAllUsers(){
        List result = null;
        try {
            result = userDAO.getAllUsers();
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }

    public void deleteUser(UserLogic user){
        try {
            userDAO.deleteUser(user);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
    }

    public int getUserCounter(){
        int result = -1;
        try {
            result = userDAO.getUserCounter();
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }

    public List<UserLogic> getAllUserRating(int count){
        List<UserLogic> result = null;
        try {
            result = userDAO.getAllUserRating(count);
        } catch (Exception e){
            System.out.println("tralalalalla");
        }
        return result;
    }
}
