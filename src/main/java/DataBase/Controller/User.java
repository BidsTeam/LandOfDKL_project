package DataBase.Controller;

import DataBase.DAO.UserDAO;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private UserDAO model;

    public User(){
        model = new UserDAO();
    }

    public void add(JSONObject json){
        model.add(json);
    }
    public int get(JSONObject json){
        return model.get(json); // Поидеи User_View должна распарсить ResultSet и вернуть уже удобную форму ответа
    }
}
