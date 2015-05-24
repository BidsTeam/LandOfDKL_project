package util;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import org.hibernate.Session;
import service.DBService;
import sun.rmi.runtime.Log;

import java.util.Random;


public class UserCardsGenerator {
    DBService dbService = null;

    public UserCardsGenerator(DBService dbService) {
        this.dbService = dbService;
    }

    public void generate(int userID) {
        Session session = dbService.getSession();
        try {
            UserLogic user = dbService.getUserService(session).getUserById(userID);
            int cardCount = dbService.getCardService(session).getCardCounter();
            LogFactory.getInstance().getLogger(this.getClass()).debug(cardCount);
            Random random = new Random();
            for (int i = 0; i < 15; i++) {
                CardLogic card = dbService.getCardService(session).getRandomCard();
                LogFactory.getInstance().getLogger(this.getClass()).debug("CARDID - " + card.getId());
                dbService.getCardService(session).addCardToUser(user, card);
            }
        }finally {
            dbService.closeSession(session);
        }
    }

}
