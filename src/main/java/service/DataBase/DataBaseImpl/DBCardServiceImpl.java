package service.DataBase.DataBaseImpl;

import DAO.CardDAO;
import DAO.Impl.CardDAOImpl;
import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import service.DataBase.DBCardService;

import java.util.List;

public class DBCardServiceImpl implements DBCardService {
    CardDAO cardDAO;

    public DBCardServiceImpl(Session session) { cardDAO = new CardDAOImpl(session); }

    public CardLogic getCard(int id) {
        return cardDAO.getCard(id);
    }

    public void addCard(CardLogic card) {
        cardDAO.addCard(card);
    }

    public void addCardToUser(UserLogic user, CardLogic card) {
        cardDAO.addCardToUser(user, card);
    }

    public List<Integer> getUserDeck(UserLogic user) {
        return cardDAO.getUserDeck(user);
    }

    public List<CardLogic> getAllCardsInfo() {
        return cardDAO.getAllCardsInfo();
    }
    public void setUserDeck(int userID, List<Integer> deck) {
        cardDAO.setUserDeck(userID, deck);
    }
}
