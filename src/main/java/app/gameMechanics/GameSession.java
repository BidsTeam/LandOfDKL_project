package app.gameMechanics;

import org.json.JSONObject;


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
        for ( int i = 0 ; i < 2 ; i++){
            JSONObject response = new JSONObject();
            response.put("action", "new_game");
            response.put("gameID", gameID);
            if (i == 0) {
                response.put("opponent_name", secondPlayer.getUsername());
                firstPlayer.sendResponse(response);
            } else {
                response.put("opponent_name", firstPlayer.getUsername());
                secondPlayer.sendResponse(response);
            }
        }
    }
}
