package test.tcp.many2one;

import tcp.many2one.many2one_client;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class many2one_test_2 {
    public many2one_client es1;
    public many2one_client es2;

    @Test
    public void givenClient1_whenServerResponds_thenCorrect() throws IOException {
        many2one_client client1 = new many2one_client("127.0.0.1", 6666);
        client1.startConnection();
        String msg1 = client1.sendMessage("hello");
        String msg2 = client1.sendMessage("world");
        String terminate = client1.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "bye");
    }


    @Test
    public void givenClient2_whenServerResponds_thenCorrect() throws IOException {
        many2one_client client2 = new many2one_client("127.0.0.1", 6666);
        client2.startConnection();
        String msg1 = client2.sendMessage("hello");
        String msg2 = client2.sendMessage("world");
        String terminate = client2.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "bye");
    }

}
