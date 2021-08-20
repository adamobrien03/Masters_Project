package test.udp.many2many;

import org.junit.Test;
import authentication_one2one.udp.udp_client;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class many2many_test {
    private DatagramChannel client;

    @Test
    public void startUDPConnection() throws Exception {
        //Start up client - it should broadcast out UDP messages.
        client = udp_client.startClient();

        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 5555);

        udp_client.sendMessage(client, "Hello server", serverAddress);
    }
}
