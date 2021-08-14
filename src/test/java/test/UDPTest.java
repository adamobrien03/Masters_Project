package test;

import udp.original_client_withTCPandUDP;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UDPTest {
    private original_client_withTCPandUDP client;

    @Test
    public void startUDPConnection() throws Exception {
        //Start up client - it should broadcast out UDP messages.
        //Adding host and port for TCPServer connection later - have to edit this.
        client = new original_client_withTCPandUDP();
        client.discoverAvailableServers("Hello_there_you", 1111);

        System.out.println("We found this many servers: ");

    }
}
