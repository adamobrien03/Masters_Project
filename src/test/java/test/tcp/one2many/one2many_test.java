package test.tcp.one2many;

import tcp.one2one.one2one_client;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class one2many_test {
    @Test
    public void one2many_tcpTest() throws IOException {
        //port matches the port in one2one_server.
        one2one_client cs1 = new one2one_client("127.0.0.1", 6666);
        one2one_client cs2 = new one2one_client("127.0.0.1", 7777);

        cs1.startConnection();
        String response = cs1.sendMessage("hello server");
        assertEquals("hello client", response);


        cs2.startConnection();
        String response2 = cs2.sendMessage("hello server");
        assertEquals("hello client", response2);
    }
}
