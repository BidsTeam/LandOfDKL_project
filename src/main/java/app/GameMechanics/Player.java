package app.GameMechanics;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.Api.User;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Player {
    private UserLogic userInfo;
    private HashSet<Session> userConnections;
    private String username;
    private int userID;
    private int health;
    private final int MAX_HEALTH = 20;
    private List<Integer> deck;
    //private AccountMap cache = AccountMap.getInstance();


    public Player(UserLogic user) {
        username = user.getUsername();
        userID = user.getId();
        userConnections = AccountMap.getInstance().getUserSessions(user.getId());
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

    public void sendResponse(JSONObject json) {
        try {
            if (!userConnections.isEmpty()){
                for (Session connection : userConnections) {
                    connection.getRemote().sendString(json.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).fatal(e);
        }
    }




}
