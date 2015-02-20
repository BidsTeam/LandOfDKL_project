package DataBase;

import java.sql.*;
import DataBase.Controller.User;

/**
 * класс прослойка для работы с базами данных
 **/
public class DB {

    private static Connection dbConnection;
    private static Statement statement;

    public static Statement getStatement(){
        try{
            if (dbConnection == null || dbConnection.isClosed()) {
                new DB();
            }
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
        return statement;
    };

    private DB() {
        //joining the Db driver
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            dbConnection = DriverManager.getConnection("jdbc:mysql://89.188.104.45/land_of_dkl_db", "land_of_dkl_user", "V0WTy7TL");
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}