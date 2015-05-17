package service.DataBase;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

import java.util.List;


public interface DBCardService {
    public CardLogic getCard(int id);
    public void addCardToUser(UserLogic user, CardLogic card);
    public void addCard(CardLogic card);
    public int getCardCounter();
    public CardLogic getRandomCard();
    public List<Integer> getUserDeck(UserLogic user);
    public List<CardLogic> getAllCardsInfo();
}
