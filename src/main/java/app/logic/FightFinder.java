package app.logic;

import org.json.JSONObject;

import java.util.LinkedHashMap;

//import java.sql.*;
/**
 * Класс который будет хранить все идущие битвы, который будет искать их, и распредялять всё это дело
 */
public class FightFinder {
    LinkedHashMap<Integer, FightInstance > listOfGames;

    public void createNewGame(JSONObject json) {
        FightInstance newFight = new FightInstance();
        listOfGames.put(newFight.gameID, newFight);
        //send back to server number of game
    }

    public void giveDamage(JSONObject json) {
        int match_id = (int) json.get("match_id");
        FightInstance currentGame = listOfGames.get(match_id);
        //int damage = (int) json.get("damage");
        //здесь будет будет в зависимости от пакета что-то делаться
    }
}
