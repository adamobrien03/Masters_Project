package test.udp.one2one;

import org.junit.Test;
import udp.one2one.one2one_client;

public class one2one_test {
    private one2one_client client;

    @Test
    public void startUDPConnection() throws Exception {
        //Start up client - it should broadcast out UDP messages.
        client = new one2one_client();
        client.discoverAvailableServers("Hello_there_you");

        System.out.println("We found this many servers: " + client.serversDiscovered);
    }
}
