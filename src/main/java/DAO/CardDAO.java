package DAO;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;

/**
 * Created by andreybondar on 04.04.15.
 */
public interface CardDAO {
    public CardLogic getCard(int id);
    public void addCard(CardLogic card);
    public void addCardToUser(UserLogic user, CardLogic card);
}
