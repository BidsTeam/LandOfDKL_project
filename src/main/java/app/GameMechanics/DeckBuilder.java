package app.GameMechanics;


import DAO.logic.CardLogic;
import app.Admin.Card;
import org.json.JSONArray;
import org.json.JSONObject;
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
            CardLogic card = dbService.getCardService(dbService.getSession()).getCard((int) (jsonArray.get(i)));
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
            dbService.getCardService(dbService.getSession()).setUserDeck(userID, userDeck);
            return true;
        } else {
            return false;
        }
    }
}
