package udp.one2many;

import udp.DatagramChannelBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class one2many_server extends Thread{
    protected MulticastSocket multicastSocket = null;
    public static DatagramChannel channel = null;

    protected byte[] buffer = new byte[256];
    protected byte[] received_buffer = new byte[256];

    protected InetAddress group = null;

    private DatagramPacket packet;
    private DatagramPacket received_packet;
    private InetAddress address = null;
    private int packetPort;

    private PrintWriter out;
    private BufferedReader in;

    public one2many_server(int port) throws IOException {
        //Start UDP Server
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        channel = DatagramChannelBuilder.bindChannel(address);

//        multicastSocket = new MulticastSocket(port);
//        multicastSocket.setReuseAddress(true);
//        group = InetAddress.getByName("230.0.0.0");
//        multicastSocket.joinGroup(group);
    }

    //UDP Methods
    public void run() {
        try {
            while (true) {
                InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7777);
                sendMessage(channel, "Hello", serverAddress);
//                sendPacket();
//                receivePacket();
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

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
        //UDP message has to be in a specific message structure.
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        client.send(buffer, serverAddress);
    }

//    public void sendPacket() throws IOException {
//        String message = "Hello";
//        buffer = message.getBytes();
//
//        packet = new DatagramPacket(buffer, buffer.length, group, 7777);
//        multicastSocket.send(packet);
//    }
//
//    public void receivePacket() throws IOException {
//        received_packet = new DatagramPacket(received_buffer, received_buffer.length);
//        multicastSocket.receive(received_packet);
//
//        if (received_packet.getPort() != 6666) {
//            System.out.println("Received " + packet.getPort() + " into the server");
//            address = received_packet.getAddress();
//            checkReceivedUDPMessage(new String(received_packet.getData(), 0, received_packet.getLength()));
//        }
//    }
//
//    public void returnUDPPacket(String message) throws IOException {
//        packetPort = received_packet.getPort();
//
//        received_buffer = message.getBytes();
//        received_packet = new DatagramPacket(received_buffer, received_buffer.length, address, packetPort);
//
//        multicastSocket.send(received_packet);
//    }
//
//    public void checkReceivedUDPMessage(String message) throws IOException {
//        if (message.contains("hello")) {
//            //parse the received Packet information from the client
//            returnUDPPacket("Server says hi");
//            System.out.println("Received a packet from a client with correct word, sending back to: " + address);
//        }else if(message.contains("end")) {
//            System.out.println("Client wants to end communication, sending end");
//            returnUDPPacket("end");
//            multicastSocket.leaveGroup(group);
//            multicastSocket.close();
//        }
//        }else{
//            System.out.println("Correct message not sent to this server, sending end");
//            returnUDPPacket("end");
//        }
//    }

    public static void main(String[] args) throws Exception {
         one2many_server server = new one2many_server(7777);
         server.run();
        }
    }