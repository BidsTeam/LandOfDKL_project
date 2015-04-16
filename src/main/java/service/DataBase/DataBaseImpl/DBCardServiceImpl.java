package service.DataBase.DataBaseImpl;

import DAO.CardDAO;
import DAO.Impl.CardDAOImpl;
import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.SessionFactory;
import service.DataBase.DBCardService;

public class DBCardServiceImpl implements DBCardService {
    CardDAO cardDAO;

    public DBCardServiceImpl(SessionFactory sessionFactory) { cardDAO = new CardDAOImpl(sessionFactory); }

    public CardLogic getCard(int id) {
        return cardDAO.getCard(id);
    }



    public boolean addCard(CardLogic card) {
        try {
            if (cardDAO.getCardByName(card.getName()) == null) {
                cardDAO.addCard(card);
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }

    }


    public boolean addCardToUser(UserLogic user, CardLogic card) {
        try {
            cardDAO.addCardToUser(user, card);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getCardCounter() {
        return cardDAO.getCardCounter();
    }
}
