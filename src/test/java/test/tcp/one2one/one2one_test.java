package test.tcp.one2one;

import org.junit.Test;
import tcp.one2one.one2one_client;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class one2one_test {
    @Test
    public void one2one_tcpTest() throws IOException {
        //port matches the port in one2one_server.
        one2one_client cs1 = new one2one_client("127.0.0.1", 6666);
        cs1.startConnection();
        String response = cs1.sendMessage("hello server");
        assertEquals("hello client", response);
    }
}
