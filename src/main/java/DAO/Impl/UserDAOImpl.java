package DAO.Impl;

import DAO.UserDAO;
import DAO.logic.UserLogic;
import app.util.AccountCache;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private AccountCache accountCache = new AccountCache();
    @Override
    public void addUser(UserLogic user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
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
    public void updateUser(UserLogic user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public UserLogic getUserById(int id) throws SQLException {
        Session session =  null;
        UserLogic user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (UserLogic) session.get(UserLogic.class, id); //todo session.load используется только если экземпляр уже был найден (спроси подробнее расскажу)
            //todo спросить практическую реализацию. Если у нас много разных сессий (ведь фабрика отдает), как не обкакаться
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }
    @Override
    public UserLogic getUserByUsername(String username) throws SQLException {

        Session session = null;
        UserLogic user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (UserLogic) session.createCriteria( UserLogic.class ).
                    add( Restrictions.eq("username", username) ).
                    uniqueResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
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
            session = HibernateUtil.getSessionFactory().openSession();
            List<UserLogic> result  =  session.createCriteria( UserLogic.class ).
                    add(Restrictions.eq("username", username)).
                    add( Restrictions.eq("password", password) ).
                    list();
            if (result.size() > 0) {
                user = result.get(0);
                accountCache.putUser(user);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public List<UserLogic> getAllUsers() throws SQLException {
        // В будущем нужно переделать на кеширующий criteria
        // todo ОБожеКакЯНенавижуORM
        Session session = null;
        List<UserLogic> userList = new ArrayList<UserLogic>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            userList = session.createCriteria(UserLogic.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userList;
    }

    @Override
    public void deleteUser(UserLogic user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
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
            session = HibernateUtil.getSessionFactory().openSession();
            UserLogic user = (UserLogic) session.createCriteria(UserLogic.class).
                    addOrder(Order.desc("id")).
                    setMaxResults(1).
                    uniqueResult();
            counter = user.getId();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
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
            session = HibernateUtil.getSessionFactory().openSession();
            userList = session.createCriteria(UserLogic.class).addOrder(Order.desc("level")).setMaxResults(count).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userList;
    }


}