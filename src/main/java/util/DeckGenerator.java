package util;


import org.hibernate.Session;
import service.DBService;

import java.util.Arrays;
import java.util.List;

public class DeckGenerator {
    final private static List<Integer> deckVarOne = Arrays.asList(8, 8, 11, 11, 13, 9, 9, 9, 12, 15, 10, 10, 14, 14, 16);
    final private static List<Integer> deckVarTwo = Arrays.asList(8, 8, 11, 11, 13, 9, 9, 9, 12, 15, 10, 10, 14, 14, 16);
    final private static List<Integer> deckVarThree = Arrays.asList(8, 8, 11, 11, 13, 9, 9, 9, 12, 15, 10, 10, 14, 14, 16);

    public static void generateDeckForNewUser(int userID, DBService dbService) {
        int deckNumber = userID % 3;
        Session session = dbService.getSession();
        switch (deckNumber) {
            case 0 : {
                dbService.getCardService(session).setUserDeck(userID, deckVarOne);
                break;
            }
            case 1 : {
                dbService.getCardService(session).setUserDeck(userID, deckVarTwo);
                break;
            }
            case 2 : {
                dbService.getCardService(session).setUserDeck(userID, deckVarThree);
                break;
            }
        }
        session.close();
    }
}
