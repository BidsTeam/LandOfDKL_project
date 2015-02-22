package DataBase.Controller;

import DataBase.Model.User_Model;
import DataBase.View.User_View;

import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean checkLogin(JSONObject json) {
        String password = json.get("password").toString();
        ResultSet rs = model.getByUsername(json);
        try {
            if (password == rs.getString("password"))
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
