package DAO.Impl;

import DAO.logic.CardLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.LogFactory;

import javax.swing.*;

/**
 * Created by andreybondar on 11.04.15.
 */
public class CardDAOImpl {

    private SessionFactory sessionFactory;

    public CardDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
}
