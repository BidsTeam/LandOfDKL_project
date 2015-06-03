package DAO.Impl;

import DAO.CardDAO;
import DAO.logic.CardLogic;
import DAO.logic.UserCardLogic;
import DAO.logic.UserLogic;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        session.update(user);
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

    public void setUserDeck(int userID, List<Integer> deck) {
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            UserLogic user = (UserLogic) session.createQuery("from UserLogic U where U.id = " + userID).uniqueResult();
            Query query = session.createQuery("from UserCardLogic UCL WHERE UCL.pk.user.id = " + userID);
            List result = query.list();
            for (Object ucl : result) {
                session.delete(ucl);
            }
            List<Integer> alreadyExists = new ArrayList<>();
            for (int cardID : deck) {
                if (alreadyExists.contains(cardID)) {
                    UserCardLogic userCardLogic = (UserCardLogic) session.createQuery("from UserCardLogic UCL WHERE UCL.pk.card.id = " + cardID +
                            " and UCL.pk.user.id = " + userID).uniqueResult();
                    userCardLogic.setCount(userCardLogic.getCount() + 1);
                    session.save(userCardLogic);
                } else {
                    CardLogic card = (CardLogic) session.createQuery("from CardLogic C WHERE C.id =" + cardID).uniqueResult();
                    UserCardLogic userCard = new UserCardLogic();
                    userCard.setUser(user);
                    userCard.setCard(card);
                    userCard.setCount(1);
                    userCard.setInHand(true);
                    session.save(userCard);
                    alreadyExists.add(cardID);
                }
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.getInstance().getLogger(this.getClass()).error("Error in setUserDeck");
            tx.rollback();
        }
    }
}
