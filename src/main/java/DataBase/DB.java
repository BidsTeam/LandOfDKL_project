package DataBase;

import java.sql.*;
import DataBase.Controller.User;

/**
 * класс прослойка для работы с базами данных
 **/
public class DB {

    private static Connection dbConnection;
    private static Statement statement;
    private static DB db;
    public static Statement getStatement(){
        //Statement stat = null;
        try{
            if (dbConnection == null || dbConnection.isClosed()) {
                db = new DB();
            }
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
        return statement;
//        try {
//             stat = dbConnection.createStatement();
//        } catch(Exception e){
//            System.out.println("DB GetStatement Error");
//
//        }
//        return stat; //todo  Ололо говнокодер, возвращаю null , хотя приложение поидеи должно умереть
    }

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