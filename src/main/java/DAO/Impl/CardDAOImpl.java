package DAO.Impl;

import DAO.CardDAO;
import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import util.HibernateUtil;
import util.LogFactory;

import javax.smartcardio.Card;
import javax.swing.*;

/**
 * Created by andreybondar on 11.04.15.
 */
public class CardDAOImpl implements CardDAO {

    private SessionFactory sessionFactory;

    public CardDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCard(CardLogic card) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(card);
            session.getTransaction().commit();
            session.close();
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // close автоматически делает transaction ROLLBACK
            }
        }
    }

    public CardLogic getCard(int id) {
        Session session = null;
        CardLogic card = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            card = (CardLogic) session.get(CardLogic.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getCard");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return card;
    }

    public void addCardToUser(UserLogic user, CardLogic card) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        user.getCards().add(card);
        session.update(user);

        session.getTransaction().commit();
        session.close();
    }

    public int getCardCounter() {
        Session session = null;
        int counter = 0;
        try {
            session = sessionFactory.openSession();
            CardLogic card = (CardLogic) session.createCriteria(CardLogic.class).
                    addOrder(Order.desc("id")).
                    setMaxResults(1).
                    uniqueResult();
            counter = card.getId();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getCardCounter");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return counter;
    }
}
