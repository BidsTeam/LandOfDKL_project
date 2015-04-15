package util;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import service.DBService;

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
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int cardNumber = random.nextInt(cardCount-1) + 1;
            CardLogic card = dbService.getCardService().getCard(cardNumber);
            dbService.getCardService().addCardToUser(user, card);
        }
    }

}
