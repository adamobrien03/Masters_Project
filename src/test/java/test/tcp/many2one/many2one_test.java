package test.tcp.many2one;

import tcp.many2one.many2one_client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class many2one_test {
    public many2one_client many2one_client_1;
    public many2one_client many2one_client_2;

    @Before
    public void setup() throws IOException {
        many2one_client_1 = new many2one_client("127.0.0.1", 6666);
        many2one_client_2 = new many2one_client("127.0.0.1", 6666);

        many2one_client_1.startConnection();
        many2one_client_2.startConnection();
    }

    @After
    public void tearDown() throws IOException {
        many2one_client_1.stopConnection();
        many2one_client_2.stopConnection();
    }

    @Test
    public void givenClient_whenServerEchosMessage_thenCorrect() throws IOException {
        String resp1 = many2one_client_1.sendMessage("hello");
        String resp2 = many2one_client_1.sendMessage("world");
        String resp3 = many2one_client_1.sendMessage("!");
        String resp4 = many2one_client_1.sendMessage(".");

        assertEquals("hello", resp1);
        assertEquals("world", resp2);
        assertEquals("!", resp3);
        assertEquals("bye", resp4);
    }

    @Test
    public void givenClient_whenServerEchosMessage_thenCorrect2() throws IOException {
        String resp1 = many2one_client_2.sendMessage("hello");
        String resp2 = many2one_client_2.sendMessage("world");
        String resp3 = many2one_client_2.sendMessage("!");
        String resp4 = many2one_client_2.sendMessage(".");

        assertEquals("hello", resp1);
        assertEquals("world", resp2);
        assertEquals("!", resp3);
        assertEquals("bye", resp4);
    }
}
