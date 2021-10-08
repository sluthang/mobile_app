package za.co.wethinkcode.robot.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void connectionTest() throws SQLException {
        DatabaseConnection conn = new DatabaseConnection("jdbc:sqlite::memory:");
        conn.connect();
        assertFalse(conn.getConnection().isClosed());
    }

    @Test
    void disconnectionTest() throws SQLException {
        DatabaseConnection conn = new DatabaseConnection("jdbc:sqlite::memory:");
        conn.connect();
        conn.disconnect();
        assertTrue(conn.getConnection().isClosed());

    }
}