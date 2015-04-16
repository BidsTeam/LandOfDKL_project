package DAO;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

public interface CardDAO {
    public CardLogic getCard(int id);
    public void addCard(CardLogic card);
    public void addCardToUser(UserLogic user, CardLogic card);
    public int getCardCounter();
    public CardLogic getCardByName(String name);
}
