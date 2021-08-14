package udp.one2many;

import udp.DatagramChannelBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.StandardCharsets;

public class one2many_server extends Thread{
    protected MulticastSocket multicastSocket = null;
    public static DatagramChannel server_channel = null;

    protected byte[] buffer = new byte[256];
    protected byte[] received_buffer = new byte[256];

    protected InetAddress group = null;

    private DatagramPacket packet;
    private DatagramPacket received_packet;
    private InetAddress address = null;

    private static DatagramSocket socket;
    private byte[] buf;

    private PrintWriter out;
    private BufferedReader in;

    public one2many_server(int port) throws IOException {
        //Start UDP Server
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        server_channel = DatagramChannelBuilder.bindChannel(address);

        NetworkInterface ni = NetworkInterface.getByName("lo");
        server_channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
        server_channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        InetAddress group = InetAddress.getByName("230.0.0.0");
        MembershipKey key = server_channel.join(group, ni);
    }

    //UDP Methods
    public void run() {
        try {
            while (true) {
                sendMessage("Hello");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String receiveMessage(DatagramChannel server) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAddress = server.receive(buffer);
        String message = extractMessage(buffer);

        System.out.println("Client at #" + remoteAddress + "  sent: " + message);

        return message;
    }

    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String msg = new String(bytes);

        return msg;
    }

    public void sendMessage(String msg) throws IOException {
        //UDP message has to be in a specific message structure.
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());


        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4321);
        socket.send(packet);
        socket.close();
    }

    public static void main(String[] args) throws Exception {
        one2many_server server = new one2many_server(4321);
        server.run();
        }
    }