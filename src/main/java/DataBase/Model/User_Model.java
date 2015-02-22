package DataBase.Model;

import DataBase.DB;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class User_Model {
    private Statement statement;

    public User_Model(Statement stat){
        statement = stat;
    }

    public boolean add(JSONObject json) {
        ResultSet rs = getByUsername(json);
        try {
            if (rs.next()) {
                System.out.println("User already exits");
                //TODO - send that to back to server
                return false;
            }
            else {
                if (json.get("password") == null || json.get("password").toString() == ""){
                    return false;
                }
                if (json.get("username") == null || json.get("username").toString() == ""){
                    return false;
                }
                String insertSQL = "INSERT INTO users "
                        + "(username, password) " + "VALUES"
                        + "('" + json.get("username").toString() + "','" + json.get("password").toString() + "')";
                statement.executeUpdate(insertSQL);
                System.out.println("user" + json.get("username").toString() + "created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public ResultSet getByUsername(JSONObject json) {
        ResultSet rs = null;
        String findSQL = "SELECT * from users where username = " + "'" + json.get("username").toString() + "';";
        try {
            rs = statement.executeQuery(findSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}


