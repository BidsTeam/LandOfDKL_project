package service.DataBase;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

import javax.smartcardio.Card;

/**
 * Created by andreybondar on 14.04.15.
 */
public interface DBCardService {
    public CardLogic getCard(int id);
    public void addCardToUser(UserLogic user, CardLogic card);
    public void addCard(CardLogic card);
    public int getCardCounter();
}
