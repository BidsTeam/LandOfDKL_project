package DataBase;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {

    private static DataSource datasource;
    private ComboPooledDataSource cpds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://89.188.104.45/land_of_dkl_db");
        cpds.setUser("land_of_dkl_user");
        cpds.setPassword("V0WTy7TL");

        cpds.setIdleConnectionTestPeriod(4); // Очень важная настройка, иначе мускуль рвет коннект и все, пизда
    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
        }
        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}