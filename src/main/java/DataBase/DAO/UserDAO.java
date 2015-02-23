package DataBase.DAO;

import DataBase.DataSource;
import org.json.simple.JSONObject;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDAO {
    private Connection connection = null;
    private Statement statement = null;
    public UserDAO(){

    }

    public boolean add(JSONObject json) {
        int rs = get(json);
        try {
            if (rs != 0) {
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
                try {
                    connection = DataSource.getInstance().getConnection();
                    statement = connection.createStatement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String insertSQL = "INSERT INTO users "
                        + "(username, password) " + "VALUES"
                        + "('" + json.get("username").toString() + "','" + json.get("password").toString() + "')";
                statement.executeUpdate(insertSQL);
                System.out.println("user" + json.get("username").toString() + "created");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() +" In User_Model1");
            return false;
        }
        if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
        if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public int get(JSONObject json) {
        ResultSet rs = null;
        int result = 0;
        if (json.get("action").toString() == "find_user" ||
                json.get("action").toString() == "create_new_user") {
            try {
                connection = DataSource.getInstance().getConnection();
                statement = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String findSQL = "SELECT * from users where username = " + "'" + json.get("username").toString() + "';";
            try {
                rs = statement.executeQuery(findSQL);
                if(rs.next()){
                    result = rs.getInt("id");
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage() + " In User_Model get");
            }
        }
        if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
        if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
        if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        return result;
    }

    public JSONObject isLogin(JSONObject json) {
        String username = json.get("username").toString();
        String password = json.get("password").toString();
        int rs = get(json);
        JSONObject jsonResult = new JSONObject();

//                if (username == rs.getString("username") && password == rs.getString("password")) {
        if (rs == 0){
            jsonResult.put("result", "success");
            jsonResult.put("message", "OK");
            return jsonResult;
        } else {
            jsonResult.put("result", "fail");
            jsonResult.put("message", "Wrong login or pas");
            return jsonResult;
        }
    }
}


