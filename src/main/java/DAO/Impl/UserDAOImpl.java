package DAO.Impl;

import DAO.UserDAO;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.AccountMap.messages.MessageAuthenticate;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;
import util.LogFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private Session session;
    private final int DECK_SIZE = 15;

    public UserDAOImpl(Session session){
        this.session = session;
    }


    @Override
    public void addUser(UserLogic user)  {
        session.save(user);
    }

    @Override
    public void updateUser(UserLogic user) {
        session.update(user);
    }

    @Override
    public UserLogic getUserById(int id)  {
        UserLogic user = null;
        if (id != 0){
            user = (UserLogic) session.get(UserLogic.class, id);
        }
        return user;
    }
    @Override
    public UserLogic getUserByUsername(String username) {
        UserLogic user = null;
        user = (UserLogic) session.createCriteria( UserLogic.class ).
                add( Restrictions.eq("username", username) ).
                uniqueResult();
        return user;
    }

    @Override
    public UserLogic getUserByAuth(String username, String password, MessageSystem messageSystem) {
        UserLogic user = null;
        List<UserLogic> result  =  session.createCriteria( UserLogic.class ).
                add(Restrictions.eq("username", username)).
                add( Restrictions.eq("password", password) ).
                list();
        if (result.size() > 0) {
            user = result.get(0);
            Message msg = new MessageAuthenticate(messageSystem.getAddressService().getAccountServiceAddress(),user.getId());
            messageSystem.sendMessage(msg);
        }
        return user;
    }

    @Override
    public List<UserLogic> getAllUsers() {
        List<UserLogic> userList = new ArrayList<UserLogic>();
        userList = session.createCriteria(UserLogic.class).list();
        return userList;
    }

    @Override
    public void deleteUser(UserLogic user) {
        session.delete(user);
    }

    @Override
    public int getUserCounter() {
        int counter = 0;
        UserLogic user = (UserLogic) session.createCriteria(UserLogic.class).
                addOrder(Order.desc("id")).
                setMaxResults(1).
                uniqueResult();
        counter = user.getId();
        return counter;
    }

    @Override public List<UserLogic> getAllUserRating(int count) {
        if (count <= 0 ){
            count = 10;
        }
        List<UserLogic> userList = new ArrayList<UserLogic>();
        userList = session.createCriteria(UserLogic.class).addOrder(Order.desc("level")).setMaxResults(count).list();
        return userList;
    }

}