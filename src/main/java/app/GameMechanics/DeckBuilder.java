package app.GameMechanics;


import DAO.logic.CardLogic;
import org.hibernate.Session;
import org.json.JSONArray;
import service.DBService;

import java.util.*;

public class DeckBuilder {
    private DBService dbService;

    public DeckBuilder(DBService dbService) {
        this.dbService = dbService;
    }

    public boolean buildDeck(int userID, JSONArray jsonArray) {
        int dragonCounter = 0;
        int knightCounter = 0;
        int ladyCounter = 0;
        List<Integer> userDeck= new ArrayList<>(0);

        for (int i = 0; i < jsonArray.length(); i++) {
            Session session = dbService.getSession();
            CardLogic card = dbService.getCardService(session).getCard((int) (jsonArray.get(i)));
            session.close();
            userDeck.add(card.getId());
            switch (card.getCardType()) {
                case "dragon": {
                    dragonCounter++;
                    break;
                }
                case "knight": {
                    knightCounter++;
                    break;
                }
                case "lady": {
                    ladyCounter++;
                    break;
                }
            }
        }
        if (ladyCounter > 5 || knightCounter > 5 || dragonCounter > 5) {
            return false;
        } else if ((ladyCounter + knightCounter + dragonCounter) == 15) {
            Session session = dbService.getSession();
            dbService.getCardService(session).setUserDeck(userID, userDeck);
            session.close();
            return true;
        } else {
            return false;
        }
    }
}
