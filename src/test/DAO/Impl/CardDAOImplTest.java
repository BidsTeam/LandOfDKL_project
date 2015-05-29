package DAO.Impl;

import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import DAO.logic.UserLogic;
import TestSetups.TestsCore;
import messageSystem.MessageSystem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;
import util.ServiceWrapper;

import java.util.List;

import static org.junit.Assert.*;

public class CardDAOImplTest extends TestsCore {

    static DBService dbService = new DBServiceImpl(sessionFactory);
    final static MessageSystem messageSystem = new MessageSystem();
    ServiceWrapper serviceWrapper = new ServiceWrapper(dbService,messageSystem);

    @Test
    public void testAddCard() throws Exception {
        Session session = dbService.getSession();
        Transaction tx = session.beginTransaction();
        //(String name, int attack, String cardType
        CardLogic card = new CardLogic("butthurt",10,"dragon");
        dbService.getCardService(session).addCard(card);
        CardLogic resultCard = dbService.getCardService(session).getCard(card.getId());
        tx.rollback();
        session.close();
        assertEquals(resultCard.getName(), "butthurt");
    }

    @Test
    public void testGetCard() throws Exception {
        CardLogic cardlogic = dbService.getCardService(dbService.getSession()).getCard(1);
        assertEquals("123", cardlogic.getName());
    }


    @Test
    public void testGetAllCardsInfo() throws Exception {
        List<CardLogic> cardList= dbService.getCardService(dbService.getSession()).getAllCardsInfo();
        assertFalse(cardList.isEmpty());
    }

    @Test
    public void testGetUserDeck() throws Exception {
    }
}