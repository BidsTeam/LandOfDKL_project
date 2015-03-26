package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;

/**
 * Created by andreybondar on 25.03.15.
 */
public class GameSession {
    private Player firstPlayer;
    private Player secondPlayer;
    private int gameID;
    private WebSocketService webSocketService;

    public GameSession(Player playerOne, Player playerTwo, int id, WebSocketService webSocketService) {
        firstPlayer = playerOne;
        secondPlayer = playerTwo;
        gameID = id;
        this.webSocketService = webSocketService;
        webSocketService.notifyNewGame(firstPlayer, secondPlayer, gameID);
        webSocketService.notifyGameOver(firstPlayer, secondPlayer, true);
    }

}