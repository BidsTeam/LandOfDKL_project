package service.DataBase;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

import java.util.List;


public interface DBCardService {
    public CardLogic getCard(int id);
    public void addCardToUser(UserLogic user, CardLogic card);
    public void addCard(CardLogic card);
    public List<CardLogic> getAllCardsInfo();
    public void setUserDeck(int userID, List<Integer> deck);
    public List<CardLogic> getUserDeck(int userID);
}
