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

    private Session session;

    public CardDAOImpl(Session session) {
        this.session = session;
    }

    public void addCard(CardLogic card) {
        session.save(card);
    }

    public CardLogic getCard(int id) {
        CardLogic card = null;
        try{
            card = (CardLogic) session.get(CardLogic.class, id);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getCard");
        }
        return card;
    }

    public void addCardToUser(UserLogic user, CardLogic card) {
//        user.getCards().add(card);
        session.update(user);
    }

    public int getCardCounter() {
        int counter = 0;
        try {
            CardLogic card = (CardLogic) session.createCriteria(CardLogic.class).
                    addOrder(Order.desc("id")).
                    setMaxResults(1).
                    uniqueResult();
            counter = card.getId();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getCardCounter");
        }
        return counter;
    }

    public CardLogic getRandomCard() {
        CardLogic card = null;
        try {
            List<CardLogic> cards = session.createQuery("from CardLogic").list();
            Collections.shuffle(cards);
            LogFactory.getInstance().getLogger(this.getClass()).debug(cards.get(0).getName());
            return cards.get(0);
        } finally {
            LogFactory.getInstance().getLogger(this.getClass()).error("Could not find random card");
        }
    }

    public List<CardLogic> getAllCardsInfo() {
        List<CardLogic> cards;
        cards = session.createQuery("from CardLogic").list();
        return cards;
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
