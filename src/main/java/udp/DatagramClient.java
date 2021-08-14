package udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramClient {
    public static DatagramChannel startClient() throws IOException {
        //We bind to null, so it broadcasts to everyone. We bind to listen for incoming.
        DatagramChannel client = DatagramChannelBuilder.bindChannel(null);
        client.configureBlocking(false);
        return client;
    }

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
        //UDP message has to be in a specific message structure.
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        client.send(buffer, serverAddress);
    }

    public static void main(String[] args) throws IOException {
        DatagramChannel client = startClient();
        String msg = "Hello, this is a Baeldung's DatagramChannel based UDP client!";
        //It sends to localhost, port 7001 - we would have to discover this later.
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7001);

        sendMessage(client, msg, serverAddress);

    }
}
