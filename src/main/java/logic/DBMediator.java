package logic;

import org.json.simple.JSONObject;

import java.net.SocketPermission;
import java.sql.*;

/**
 * класс прослойка для работы с базами данных
 **/
public class DBMediator {

    Connection dbConnection;
    Statement statement;
    public DBMediator() {
        //joining the Db driver
        dbConnection = getDBConnection();
        try {
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            dbConnection = DriverManager.getConnection("jdbc:mysql://89.188.104.45/land_of_dkl_db", "land_of_dkl_user", "V0WTy7TL");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

//    public void CreateTableUsers() {
//        String createTableSql = "CREATE TABLE IF NOT EXISTS USERS  ("
//                + "USER_ID NUMBER(5) NOT NULL AUTO_INCREMENT, "
//                + "USERNAME VARCHAR(20) NOT NULL, "
//                + "PASSWORD VARCHAR NOT NULL, "
//                + "CREATED_DATE DATE NOT NULL DEFAULT CURRENT_TIMESTAMP , " + "PRIMARY KEY (USER_ID) "
//                + ")";
//
//        Connection dbConnection = null;
//        Statement statement = null;
//
//        try {
//            dbConnection = getDBConnection();
//            statement = dbConnection.createStatement();
//
//            // выполнить SQL запрос
//            statement.execute(createTableSql);
//            System.out.println("Table \"USERS\" is created!");
//
//            if (statement != null) {
//                statement.close();
//            }
//            if (dbConnection != null) {
//                dbConnection.close();
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void insertTableSQL(JSONObject json) {
        if (json.get("action").toString() == "create_new_user") {
            ResultSet rs = selectTableSQL(json);
            try {
                if (rs.next()) {
                    //TODO - send that to back to server
                    return;
                } else {
                    //System.out.println("before");
                    String insertSQL = "INSERT INTO users "
                            + "(username, password) " + "VALUES"
                            + "('" + json.get("username").toString() + "','" + json.get("password").toString() + "')";
                    //System.out.println("after");
//                  dbConnection = this.getDBConnection();
//                   Statement statement = dbConnection.createStatement();
                    statement.executeUpdate(insertSQL);
                   // System.out.println("after longer");
                    System.out.println("user" + json.get("username").toString() + "created");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public ResultSet selectTableSQL(JSONObject json) {
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


}
