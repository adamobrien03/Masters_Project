//package test;
//
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class MulticastTest {
//    @Test
//    public void whenBroadcasting_thenDiscoverExpectedServers() throws Exception {
//        new Server().start();
//
//        client = new Client();
//        client.discoverAvailableServers("hello server");
//
//        int serversDiscovered = client.serversDiscovered;
//        System.out.println(serversDiscovered);
//
//        client.startConnection();
//    }
//
//    @Test
//    public void multipleServers_started() throws Exception {
//        new Server().start();
//
//        client = new Client();
//        client.discoverAvailableServers("hello server");
//
//        int serversDiscovered = client.serversDiscovered;
//        System.out.println(serversDiscovered);
//
//        client.startConnection();
//    }
//
//    @After
//    public void tearDown() throws IOException {
//        client.discoverAvailableServers("end");
//        client.stopUDPBroadcast();
//    }
//}
