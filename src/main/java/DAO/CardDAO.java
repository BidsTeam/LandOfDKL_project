package DAO;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

import java.util.List;

/**
 * Created by andreybondar on 04.04.15.
 */
public interface CardDAO {
    public CardLogic getCard(int id);
    public void addCard(CardLogic card);
    public void addCardToUser(UserLogic user, CardLogic card);
    public int getCardCounter();
    public CardLogic getRandomCard();
    public List<Integer> getUserDeck(UserLogic user);
    public List<CardLogic> getAllCardsInfo();
}
