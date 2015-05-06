package service.DataBase.DataBaseImpl;

import DAO.CardDAO;
import DAO.Impl.CardDAOImpl;
import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.SessionFactory;
import service.DataBase.DBCardService;

import java.util.List;

public class DBCardServiceImpl implements DBCardService {
    CardDAO cardDAO;

    public DBCardServiceImpl(SessionFactory sessionFactory) { cardDAO = new CardDAOImpl(sessionFactory); }

    public CardLogic getCard(int id) {
        return cardDAO.getCard(id);
    }

    public void addCard(CardLogic card) {
        cardDAO.addCard(card);
    }

    public void addCardToUser(UserLogic user, CardLogic card) {
        cardDAO.addCardToUser(user, card);
    }

    public int getCardCounter() {
        return cardDAO.getCardCounter();
    }

    public CardLogic getRandomCard() {
        return cardDAO.getRandomCard();
    }

    public List<Integer> getUserDeck(UserLogic user) {
        return cardDAO.getUserDeck(user);
    }

    public List<CardLogic> getAllCardsInfo() {
        return cardDAO.getAllCardsInfo();
    }
}
