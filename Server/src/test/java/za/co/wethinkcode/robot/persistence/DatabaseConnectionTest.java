package za.co.wethinkcode.robot.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void connectionTest() throws SQLException {
        DatabaseConnection conn = new DatabaseConnection("jdbc:sqlite:memory:");
        Connection connection = conn.connect();
        assertFalse(connection.isClosed());
    }

    @Test
    void disconnectionTest() throws SQLException {
        DatabaseConnection conn = new DatabaseConnection("jdbc:sqlite:memory:");
        Connection connection = conn.connect();
        conn.disconnect(connection);
        assertTrue(connection.isClosed() );

    }
}