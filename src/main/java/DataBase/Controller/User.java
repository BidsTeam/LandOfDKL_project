package DataBase.Controller;

import DataBase.Model.User_Model;
import DataBase.View.User_View;

import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private User_Model model;
    private User_View view;

    public User(Statement statement){
        model = new User_Model(statement);
        view = new User_View();
    }

    public void add(JSONObject json){
        model.add(json);
    }
    public ResultSet get(JSONObject json){
        return model.get(json); // Поидеи User_View должна распарсить ResultSet и вернуть уже удобную форму ответа
    }
}
