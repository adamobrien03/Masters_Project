package test.udp.one2many;

import org.junit.Test;
import udp.one2many.one2many_client;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class one2many_test {
    private one2many_client client;

    @Test
    public void startUDPConnection() throws Exception {
        //Start up client - it should broadcast out UDP messages.
        client = new one2many_client();

        //String msg = "Hello, this is a Baeldung's DatagramChannel based UDP client!";
        //It sends to localhost, port 7001 - we would have to discover this later.
        //InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7001);
    }
}
