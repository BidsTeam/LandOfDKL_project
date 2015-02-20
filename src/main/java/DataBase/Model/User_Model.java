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
        ResultSet rs = get(json);
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

    public ResultSet get(JSONObject json) {
        ResultSet rs = null;
        if (json.get("action").toString() == "find_user" ||
                json.get("action").toString() == "create_new_user") {
            String findSQL = "SELECT * from users where username = " + "'" + json.get("username").toString() + "';";
            try {
//                dbConnection = this.getDBConnection();
//                Statement statement = dbConnection.createStatement();
                rs = statement.executeQuery(findSQL);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return rs;
    }

    public JSONObject isLogin(JSONObject json) {
        String username = json.get("username").toString();
        String password = json.get("password").toString();
        ResultSet rs = get(json);
        JSONObject jsonResult = new JSONObject();
        try {
            if (rs.next()) {
                if (username == rs.getString("username") && password == rs.getString("password")) {
                    jsonResult.put("result", "success");
                    jsonResult.put("message", "OK");
                    return jsonResult;
                }
                else {
                    jsonResult.put("result", "fail");
                    jsonResult.put("message", "Wrong login or pas");
                    return jsonResult;
                }
            }
            else {
                jsonResult.put("result", "fail");
                jsonResult.put("message", "Wrong login or pas");
                return jsonResult;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            jsonResult.put("result", "fail");
            jsonResult.put("message", "Unknown error");
            return jsonResult;
        }
    }
}


