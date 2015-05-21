package StubClasses;

import app.GameMechanics.Player;
import app.WebSocket.CustomWebSocket;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import service.DBService;
import util.RPS;

import java.util.HashSet;
import java.util.List;

/**
 * Created by andreybondar on 20.05.15.
 */
public class MockSocketService implements WebSocketService {

    @Override
    public void sendPublicMessage(JSONObject json) {

    }

    @Override
    public void sendPrivateMessage(JSONObject json, int receiverID) {

    }

    @Override
    public void putNewSocket(int userID, CustomWebSocket webSocket) {

    }

    @Override
    public void removeSocket(int userID, CustomWebSocket webSocket) {

    }

    @Override
    public void sendJson(HashSet<CustomWebSocket> userSockets, JSONObject json) {

    }

    @Override
    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameID) {

    }

    @Override
    public void notifyGameOver(Player firstPlayer, Player secondPlayer, RPS.RPSResult winner) {

    }

    @Override
    public void notifyActionSet(Player playerSetter, Player playerObserver) {

    }

    @Override
    public void notifyActionsReveal(Player firstPlayer, int firstCardID, Player secondPlayer, int secondCardID) {

    }

    @Override
    public void notifyUserEnter(int userID) {

    }

    @Override
    public void notifyUserExit(int userID) {

    }

    @Override
    public DBService getDbService() {
        return null;
    }

    @Override
    public void greetUser(int userID) {

    }

    @Override
    public void notifyGameState(Player firstPlayer, Player secondPlayer, int firstPlayerHealth, int secondPlayerHealth) {

    }

    @Override
    public void notifyReconnectPossibility(int userID) {

    }

    @Override
    public void notifyReconnect(JSONObject gameState, List<Integer> deck, int userID) {

    }
}
