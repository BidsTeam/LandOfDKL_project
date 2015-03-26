package app.GameMechanics;

import org.json.JSONObject;

/**
 * Created by andreybondar on 25.03.15.
 */
public class GameSession {
    private Player firstPlayer;
    private Player secondPlayer;
    private int gameID;

    public GameSession(Player playerOne, Player playerTwo, int id) {
        firstPlayer = playerOne;
        secondPlayer = playerTwo;
        gameID = id;
        playersGreeting(firstPlayer, secondPlayer);
    }

    private void playersGreeting(Player firstPlayer, Player secondPlayer) {
        JSONObject responseForFirst = new JSONObject();
        responseForFirst.put("action", "new_game");
        responseForFirst.put("gameID", gameID);
        responseForFirst.put("opponent_name", secondPlayer.getUsername());

        JSONObject responseForSecond = new JSONObject();
        responseForSecond.put("action", "new_game");
        responseForSecond.put("gameID", gameID);
        responseForSecond.put("opponent_name", firstPlayer.getUsername());

        firstPlayer.sendResponse(responseForFirst);
        secondPlayer.sendResponse(responseForSecond);
    }
}