package za.co.wethinkcode.robot.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public String dbUrl;
    public Connection connection;

    public DatabaseConnection(String dbUrl){
        this.dbUrl = dbUrl;
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl);
    }

    public void disconnect() throws SQLException {
        this.connection.close();
    }

    public Connection getConnection() {
        return this.connection;
    }
}
