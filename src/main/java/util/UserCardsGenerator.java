package util;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import service.DBService;
import sun.rmi.runtime.Log;

import java.util.Random;

/**
 * Created by andreybondar on 14.04.15.
 */
public class UserCardsGenerator {
    DBService dbService = null;

    public UserCardsGenerator(DBService dbService) {
        this.dbService = dbService;
    }

    public void generate(int userID) {
        UserLogic user = dbService.getUserService().getUserById(userID);
        int cardCount = dbService.getCardService().getCardCounter();
        LogFactory.getInstance().getLogger(this.getClass()).debug(cardCount);
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            CardLogic card = dbService.getCardService().getRandomCard();
            LogFactory.getInstance().getLogger(this.getClass()).debug("CARDID - " + card.getId());
            dbService.getCardService().addCardToUser(user, card);
        }
    }

}
