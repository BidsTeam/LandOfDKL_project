package DAO.Impl;

import DAO.CardDAO;
import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import sun.rmi.runtime.Log;
import util.HibernateUtil;
import util.LogFactory;

import javax.smartcardio.Card;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
                session.close();
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

    public CardLogic getRandomCard() {
        Session session = null;
        CardLogic card = null;
        try {
            session = sessionFactory.openSession();
            List<CardLogic> cards = session.createQuery("from CardLogic").list();
            Collections.shuffle(cards);
            LogFactory.getInstance().getLogger(this.getClass()).debug(cards.get(0).getName());
            return cards.get(0);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            LogFactory.getInstance().getLogger(this.getClass()).error("Could not find random card");
        }
    }

    public List<CardLogic> getAllCardsInfo() {
        Session session = null;
        List<CardLogic> cards = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            cards = session.createQuery("from CardLogic").list();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            return cards;
        }
    }

    public List<Integer> getUserDeck(UserLogic user) {
        //TODO - это временная заглушка отдающая станадартные карты - когда доделаем работу бд переделаю
        CardLogic mockKnight = getCard(8);
        CardLogic mockDragon = getCard(9);
        CardLogic mockLady = getCard(10);
        List<Integer> deck = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            deck.add(mockKnight.getId());
        }
        for (int i = 0; i < 5; i++) {
            deck.add(mockDragon.getId());
        }
        for (int i = 0; i < 5; i++) {
            deck.add(mockLady.getId());
        }
        return deck;
    }
}
