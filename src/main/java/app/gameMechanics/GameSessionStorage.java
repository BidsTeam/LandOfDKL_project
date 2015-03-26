package app.GameMechanics;

import java.util.HashMap;

/**
 * Created by andreybondar on 25.03.15.
 */
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

    public void newGameSession(Player firstPlayer, Player secondPlayer) {
        idCounter++;
        GameSession gameSession = new GameSession(firstPlayer, secondPlayer, idCounter);
        gameSessionHashMap.put(idCounter, gameSession);
    }



}
