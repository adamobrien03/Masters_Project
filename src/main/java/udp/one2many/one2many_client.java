package udp.one2many;

import udp.DatagramChannelBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.MulticastChannel;

public class one2many_client extends Thread {
    protected Socket clientSocket;
    public MulticastSocket multicastSocket = null;
    private static final String MULTICAST_INTERFACE = "eth0";
    private static final int MULTICAST_PORT = 4321;
    private static final String MULTICAST_IP = "230.0.0.0";
    public InetAddress group = InetAddress.getByName(MULTICAST_IP);
    public byte[] buffer;
    public byte[] received_buffer;
    public int serverPort;
    private int serversDiscovered;

    public static DatagramChannel client_channel;


    public one2many_client() throws Exception {
        client_channel = DatagramChannelBuilder.bindChannel(null);
        client_channel.configureBlocking(false);

        NetworkInterface ni = NetworkInterface.getByName(MULTICAST_INTERFACE);
        client_channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
        client_channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        InetAddress inetAddress = InetAddress.getByName(MULTICAST_IP);

        MembershipKey key = client_channel.join(group, ni);
    }

    public void discoverAvailableServers(String msg) throws IOException {
        System.out.println("Checking available servers");

        try {
            while (true) {
                copyMessageOnBuffer(msg);
                multicastSendPacket();
                receivePackets();

                multicastReceivePacket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyMessageOnBuffer(String msg) {
        buffer = msg.getBytes();
    }

    private void multicastReceivePacket() throws IOException {
        System.out.println("Checking if there are any packets to receive");

        DatagramPacket received_packet = new DatagramPacket(received_buffer, received_buffer.length);
        multicastSocket.receive(received_packet);

        System.out.println("Got the packet from the server");

        serverPort = received_packet.getPort();
        System.out.println("ServerPort is: " + serverPort);

    }

    private void multicastSendPacket() throws IOException {
        //trying to send a UDP message to a group, without a defined port; then grab the port/location it came from. 08/06/2021

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 0);

        multicastSocket.send(packet);

        System.out.println("Sending out UDP on port: " + String.valueOf(packet.getPort()));

        System.out.println("Sending out UDP packet: " + new String(packet.getData()));
    }

    private void receivePackets() throws IOException {
            receivePacket();
            serversDiscovered++;
            System.out.println("How many servers have we found: " + serversDiscovered);
    }

    private void receivePacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        multicastSocket.receive(packet);
        System.out.println("Receiving in UDP packet: " + new String(packet.getData()));

        //checkUDPReceivedMessage(new String(packet.getData()));
    }

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
        //UDP message has to be in a specific message structure.
        String newData = "New String to write to file..."
                + System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();

        client.send(buf, serverAddress);
    }

    public static void send(DatagramChannel channel) throws IOException {
        String newData = "New String to write to file..."
                + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();

        int bytesSent = channel.send(buf, null);
    }

    public static void receiveMessage() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        client_channel.receive(byteBuffer);
        byteBuffer.flip();

        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, byteBuffer.limit());

        System.out.println("Received Message: " + new String(bytes));
    }
}
