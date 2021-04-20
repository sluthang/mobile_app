package za.co.wethinkcode.server;

import static org.junit.Assert.*;
import org.junit.Test;
import za.co.wethinkcode.robot.server.Server.MultiServer;

import java.io.IOException;

public class ServerTests {

    @Test
    public void testServer() {
        try {
            MultiServer.main(null);
            assertTrue(true);
        } catch (IOException e) {
            fail();
        }
    }
}
