package service.DataBase;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

import javax.smartcardio.Card;


public interface DBCardService {
    public CardLogic getCard(int id);
    public boolean addCardToUser(UserLogic user, CardLogic card);
    public boolean addCard(CardLogic card);
    public int getCardCounter();
}
