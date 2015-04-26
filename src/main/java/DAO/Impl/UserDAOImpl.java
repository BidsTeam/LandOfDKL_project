package DAO.Impl;

import DAO.UserDAO;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
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
    private AccountMap accountMap = AccountMap.getInstance();
    private SessionFactory sessionFactory;
    private final int DECK_SIZE = 15;

    public UserDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void addUser(UserLogic user)  {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // close автоматически делает transaction ROLLBACK
            }
        }
    }

    @Override
    public void updateUser(UserLogic user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in updateUser");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public UserLogic getUserById(int id)  {
        Session session =  null;
        UserLogic user = null;
        try {
            session = sessionFactory.openSession();
            user = (UserLogic) session.get(UserLogic.class, id); //todo session.load используется только если экземпляр уже был найден (спроси подробнее расскажу)
            //todo спросить практическую реализацию. Если у нас много разных сессий (ведь фабрика отдает), как не обкакаться
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getUserById");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }
    @Override
    public UserLogic getUserByUsername(String username) {

        Session session = null;
        UserLogic user = null;
        try {
            session = sessionFactory.openSession();
            user = (UserLogic) session.createCriteria( UserLogic.class ).
                    add( Restrictions.eq("username", username) ).
                    uniqueResult();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getUserByUsername");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public UserLogic getUserByAuth(String username, String password) {
        Session session = null;
        UserLogic user = null;
        try {
            session = sessionFactory.openSession();
            List<UserLogic> result  =  session.createCriteria( UserLogic.class ).
                    add(Restrictions.eq("username", username)).
                    add( Restrictions.eq("password", password) ).
                    list();
            if (result.size() > 0) {
                user = result.get(0);
                accountMap.putUser(user);
            }
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getUserByAuth");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public List<UserLogic> getAllUsers() {
        // В будущем нужно переделать на кеширующий criteria
        // todo ОБожеКакЯНенавижуORM
        Session session = null;
        List<UserLogic> userList = new ArrayList<UserLogic>();
        try {
            session = sessionFactory.openSession();
            userList = session.createCriteria(UserLogic.class).list();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getAllUsers");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userList;
    }

    @Override
    public void deleteUser(UserLogic user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in deleteUser");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public int getUserCounter() {
        Session session = null;
        int counter = 0;
        try {
            session = sessionFactory.openSession();
            UserLogic user = (UserLogic) session.createCriteria(UserLogic.class).
                    addOrder(Order.desc("id")).
                    setMaxResults(1).
                    uniqueResult();
            counter = user.getId();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getUserCounter");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return counter;
    }

    @Override
    public List<UserLogic> getAllUserRating(int count) {
        Session session = null;
        if (count <= 0 ){
            count = 10;
        }
        List<UserLogic> userList = new ArrayList<UserLogic>();
        try {
            session = sessionFactory.openSession();
            userList = session.createCriteria(UserLogic.class).addOrder(Order.desc("level")).setMaxResults(count).list();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getAllUserRating");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userList;
    }

    public boolean isDeckFull(int userID) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            int cardCounter = ((Long) session.createSQLQuery("select count(*) from user_card where user_id =" + userID).
            uniqueResult()).intValue();
            if (cardCounter >= DECK_SIZE) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            return false;
        }
    }

}