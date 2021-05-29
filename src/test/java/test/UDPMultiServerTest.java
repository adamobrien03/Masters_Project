package test;

import udp.DatagramClient;
import udp.many2many.DatagramChannelClient;
import udp.original_client_withTCPandUDP;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import static org.junit.Assert.assertEquals;

public class UDPMultiServerTest {
        private original_client_withTCPandUDP client;

    @Test
    public void whenClientSendsAndServerReceivesUDPPacket_thenCorrect() throws IOException {
        //DatagramChannel server = DatagramChannelServer.startServer();
        DatagramChannel client = DatagramChannelClient.startClient();

        String msg1 = "Hello, this is a Baeldung's DatagramChannel based UDP client!";
        String msg2 = "Hi again!, Are you there!";

        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7001);

        DatagramClient.sendMessage(client, msg1, serverAddress);
        DatagramClient.sendMessage(client, msg2, serverAddress);

//        assertEquals("Hello, this is a Baeldung's DatagramChannel based UDP client!", DatagramServer.receiveMessage(server));
//        assertEquals("Hi again!, Are you there!", DatagramServer.receiveMessage(server));
    }

//        @Test
//        public void whenBroadcasting_thenDiscoverExpectedServers() throws Exception {
//            //starts UDP Server
//            new Server(5555, true).start();
//            new Server(6666, true).start();
//
//            client = new Client();
//            client.discoverAvailableServers("hello server");
//
//            int serversDiscovered = client.serversDiscovered;
//            System.out.println(serversDiscovered);
//
//            client.startConnection();
//        }
//
//        @Test
//        public void multipleServers_started() throws Exception {
//            new Server().start();
//
//            client = new Client();
//            client.discoverAvailableServers("hello server");
//
//            int serversDiscovered = client.serversDiscovered;
//            System.out.println(serversDiscovered);
//
//            client.startConnection();
//        }
//
//        @After
//        public void tearDown() throws IOException {
//            client.discoverAvailableServers("end");
//            client.stopUDPBroadcast();
//        }
}
