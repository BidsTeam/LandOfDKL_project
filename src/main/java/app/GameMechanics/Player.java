package app.GameMechanics;


import DAO.logic.UserLogic;
import java.util.List;


public class Player {
    private String username;
    private int userID;
    private int health;
    private final int MAX_HEALTH = 20;
    private List<Integer> deck;
    //private AccountMap cache = AccountMap.getInstance();


    public Player(UserLogic user) {
        username = user.getUsername();
        userID = user.getId();
        health = MAX_HEALTH;
    }


    public String getUsername() {
        return username;
    }
    public int getUserID() {return userID; }

    public int getHealth() { return health; }
    public boolean takeDamage(int damage) {
        if (health - damage > 0) {
            health -= damage;
            return true;
        } else {
            health = 0;
            return false;
        }
    }

    public void setUserDeck(List<Integer> userDeck) {
        deck = userDeck;
    }

    public int getCard(int cardNumber) {
        int cardID = deck.get(cardNumber);
        deck.remove(cardNumber);
        deck.add(cardNumber, -1);
        return cardID;
    }






}
