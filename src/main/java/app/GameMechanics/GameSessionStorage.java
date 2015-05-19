package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import service.DBService;

import java.util.HashMap;


public class GameSessionStorage {
    private HashMap<Integer, GameSession> gameSessionHashMap;
    private int idCounter;

    public GameSessionStorage() {
        gameSessionHashMap = new HashMap<>();
        //В будущем будет брать последний id из базы данных
        idCounter = 0;
    }

//    public int putGameSession(GameSession gameSession) {
//        gameSessionHashMap.put(++idCounter, gameSession);
//        return idCounter;
//    }

    public int newGameSession(Player firstPlayer, Player secondPlayer, WebSocketService webSocketService, DBService dbService) {
        idCounter++;
        GameSession gameSession = new GameSession(firstPlayer, secondPlayer, idCounter, webSocketService, dbService);
        gameSessionHashMap.put(idCounter, gameSession);
        return idCounter;
    }

    public int getNextGameID() {
        return idCounter + 1;
    }

    public GameSession getGameSessionByID(int userID) {
        return gameSessionHashMap.get(userID);
    }

}