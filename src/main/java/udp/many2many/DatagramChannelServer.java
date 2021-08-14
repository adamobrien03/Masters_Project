package udp.many2many;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannelServer {
    static byte[] buf;

    public static DatagramChannel startServer(int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        DatagramChannel server = DatagramChannelBuilder.bindChannel(address);

        System.out.println("Server started at #" + address);

        return server;
    }

    public static String receiveMessage(DatagramChannel server) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = server.receive(buffer);
        String message = extractMessage(buffer);

        System.out.println("Client at #" + remoteAdd + "  sent: " + message);

        return message;
    }

    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String msg = new String(bytes);

        return msg;
    }

    public static void main(String[] args) throws IOException {
        DatagramChannel server = startServer(5555);

        while (true) {
            buf = receiveMessage(server).getBytes();
            System.out.write(buf);
            }
    }
}
