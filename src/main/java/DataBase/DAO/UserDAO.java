package DataBase.DAO;

import DataBase.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

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

        JSONObject def = new JSONObject();
        def.put("username","");
        def.put("password","");
        //def.put("test","");

        JSONObject queryObj = new JSONObject();
        // Этот цикл было бы круто перенести в какую-нибудь глобальную функцию
        boolean bool = false;
        for(String key : JSONObject.getNames(def)) {
            try {
                queryObj.put(key, json.get(key));
                bool = true;
            } catch (NullPointerException | JSONException e ) {
                queryObj.put(key, def.get(key));
            }
        }
        if(bool) {
            int rs = get(json);

            boolean flag = true;
            JSONObject result = new JSONObject();

            try {
                if (rs != 0) {
                    System.out.println("User already exits");
                    flag = false; //todo THROW EXCEPTION USER ALREADY EXISTS
                } else {

                    if (queryObj.get("password").toString() == "") {
                        flag = false; // todo THROW EXCEPTION Bla bla bla
                    }
                    if (queryObj.get("username").toString() == "") {
                        flag = false; // todo THROW EXCEPTION Bla bla bla
                    }
                    if (flag) {
                        boolean first = true;
                        String query = "";
                        for (String key : queryObj.getNames(def)) {
                            if (!first) {
                                query += ",";
                            }
                            query += key + "='" + queryObj.get(key) + "'";
                            first = false;
                        }
                        try {
                            connection = DataSource.getInstance().getConnection();
                            statement = connection.createStatement();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String insertSQL = "INSERT INTO users SET " + query;
                        statement.executeUpdate(insertSQL);
                        System.out.println("user" + queryObj.get("username").toString() + "created");
                    } else {
                        System.out.println("help2");
                        return false;
                    }
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage() + " In User_Model1");

                return false; // todo THROW
            }
        }else {
            System.out.println("help");
            return false;
        }
        if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
        if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        return true; //todo change format
    }


    public int get(JSONObject json) { //todo NOT INT, return Object
        ResultSet rs = null;
        int result = 0;
        JSONObject def = new JSONObject();
        def.put("username","");
        def.put("password","");
        def.put("test","");

        JSONObject queryObj = new JSONObject();
        // Этот цикл было бы круто перенести в какую-нибудь глобальную функцию.
        boolean bool = false;
        for(String key : JSONObject.getNames(def)) {
            try {
                queryObj.put(key, json.get(key));
                bool = true;
            } catch (NullPointerException | JSONException e ) {
//                queryObj.put(key, def.get(key));
            }
        }
        if (bool) {
            String query = "1 = 1 ";
            for (String key : JSONObject.getNames(queryObj)) {
                query += " AND " + key + " = '" + queryObj.get(key)+"'"; // Не проверил, будет-ли корректно с числовыми полями работать
            }
            try {
                connection = DataSource.getInstance().getConnection();
                statement = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String findSQL = "SELECT * from users where " + query;
            try {
                rs = statement.executeQuery(findSQL);
                if (rs.next()) {
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

    public JSONObject getByID(Integer id) {
        ResultSet rs = null;
        JSONObject result = new JSONObject();
        result.put("username", "");
        result.put("email", "");
        result.put("isAdmin", 0);
        //System.out.println(id.toString());
        try {
            connection = DataSource.getInstance().getConnection();
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id != 0) {
            String findSQL = "SELECT * from users where id = " + id.toString();
            //System.out.println(findSQL);
            try {
                rs = statement.executeQuery(findSQL);
                if (rs.next()) {
                    result.put("username", rs.getString("username"));
                    result.put("isAdmin", rs.getBoolean("is_admin"));
                    return result;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage() + " In User_Moder getByID");
                return result;
            }
        }
        return result; //говнокод, надо сделать так, чтобы он не возвращал пустой джейсон, а выкидывал отсюда эксепшн

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

    public int getRegisterCounter() {
        try {
            connection = DataSource.getInstance().getConnection();
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String countUsers = "SELECT MAX(id) AS id FROM users";
        try {
            ResultSet rs = statement.executeQuery(countUsers);
            if (rs.next()) {
                //System.out.println("????");
                int counter = 0;
                counter = rs.getInt("id");
                System.out.println(counter);
                return counter;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

}


