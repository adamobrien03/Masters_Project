package test.udp.one2many;

import org.junit.Test;
import udp.MulticastPublisher;
import udp.one2many.one2many_client;
import udp.one2many.one2many_server;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class one2many_test {
    private one2many_client client;
    private one2many_server server;


    @Test
    public void startUDPConnection() throws Exception {
        //Start up client - it should broadcast out UDP messages.
        client = new one2many_client();
        client.multicastSendPacket();

        MulticastPublisher.multicast("Sending");
//        client.receiveMessage();

//        server = new one2many_server(4321);
//
//        for(int i = 0; i < 6; i++){
//            server.sendMessage("Message: " + i );
//            Thread.sleep(1000);
//        }
    }
}
