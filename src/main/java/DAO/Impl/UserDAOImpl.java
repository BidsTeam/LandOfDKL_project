package DAO.Impl;

import DAO.UserDAO;
import DAO.logic.User;
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
    public void addUser(User user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
//            if (getUserByUsername(user.getUsername()) == null) {
            session.save(user);
//            } else {
//                throw new Exception("Username busy"); // todo переделать на собственный тип Exception
//            }
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // close автоматически делает transaction ROLLBACK
            }
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
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
    public User getUserById(int id) throws SQLException {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.get(User.class, id); //todo session.load используется только если экземпляр уже был найден (спроси подробнее расскажу)
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
    public User getUserByUsername(String username) throws SQLException {

        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.createCriteria( User.class ).
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
    public User getUserByAuth(String username, String password) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<User> result  =  session.createCriteria( User.class ).
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
    public List<User> getAllUsers() throws SQLException {
        // В будущем нужно переделать на кеширующий criteria
        // todo ОБожеКакЯНенавижуORM
        Session session = null;
        List<User> user_list = new ArrayList<User>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user_list = session.createCriteria(User.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user_list;
    }

    @Override
    public void deleteUser(User user) throws SQLException {
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
            User user = (User) session.createCriteria(User.class).
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


}