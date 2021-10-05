package za.co.wethinkcode.robot.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public String dbUrl;

    public DatabaseConnection(String dbUrl){
        this.dbUrl = dbUrl;
    }

    public Connection connect() throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(dbUrl);
        
        return connection;
    }

    public void disconnect(Connection connection) throws SQLException {
        connection.close();
    }
}
