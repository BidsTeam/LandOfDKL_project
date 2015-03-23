package DAO;

import DAO.Impl.UserDAOImpl;

public class Factory {

    private static UserDAO userDAO = null;
    private static Factory instance = null;
    private Factory () {}

    public static synchronized Factory getInstance(){
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }


    public UserDAO getUserDAO(){
        if (userDAO == null){
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }
}