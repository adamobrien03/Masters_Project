package udp.one2many;

import udp.DatagramChannelBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class one2many_client extends Thread {
    protected Socket clientSocket;
//    private final DatagramSocket datagramSocket;
    public MulticastSocket multicastSocket = null;
    private static DatagramChannel client_channel;
    private DatagramPacket received_packet;

    //private final InetAddress group;
    private byte[] buffer;
    private byte[] received_buffer = new byte[256];

    private PrintWriter out;
    private BufferedReader in;
    public int serversDiscovered = 0;

    private int serverPort = 0;

    public one2many_client() throws Exception {
        client_channel = DatagramChannelBuilder.bindChannel(null);
        client_channel.configureBlocking(false);
        client_channel.open();
        receiveMessage();

//        this.clientSocket = new Socket();
//        this.datagramSocket = new DatagramSocket();
//        this.group = InetAddress.getByName("230.0.0.0");
//        this.multicastSocket = new MulticastSocket();
//        multicastSocket.setReuseAddress(true);
//        multicastSocket.joinGroup(group);
    }

//    public void discoverAvailableServers(String msg) throws IOException {
//        System.out.println("Checking available servers");
//
//        try {
//            while (true) {
//                copyMessageOnBuffer(msg);
//                multicastSendPacket();
//                receivePackets();
//
//                multicastReceivePacket();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void copyMessageOnBuffer(String msg) {
//        buffer = msg.getBytes();
//    }
//
//    private void multicastReceivePacket() throws IOException {
//        System.out.println("Checking if there are any packets to receive");
//
////        DatagramPacket receivedPacket = new DatagramPacket(received_buffer, received_buffer.length);
////        datagramSocket.receive(receivedPacket);
//
//        received_packet = new DatagramPacket(received_buffer, received_buffer.length);
//        multicastSocket.receive(received_packet);
//
//        System.out.println("Got the packet from the server");
//
//        serverPort = received_packet.getPort();
//        System.out.println("ServerPort is: " + serverPort);
//
//    }
//
//    private void multicastSendPacket() throws IOException {
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 4444);
//        multicastSocket.send(packet);
//        System.out.println("Sending out UDP packet: " + new String(packet.getData()));
//    }
//
//    private void receivePackets() throws IOException {
//            receivePacket();
//            serversDiscovered++;
//            System.out.println("How many servers have we found: " + serversDiscovered);
//    }

//    private void receivePacket() throws IOException {
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//        multicastSocket.receive(packet);
//        System.out.println("Receiving in UDP packet: " + new String(packet.getData()));
//
//        checkUDPReceivedMessage(new String(packet.getData()));
//    }

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
        //UDP message has to be in a specific message structure.
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        client.send(buffer, serverAddress);
    }

    public static void receiveMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);


        SocketAddress remoteAddress = client_channel.receive(buffer);
        System.out.println(buffer);
        System.out.println("Client at #" + remoteAddress + "  sent: ");

    }

    public void checkUDPReceivedMessage(String message) throws IOException {
        if (message.contains("hi")) {
            //parse the received Packet information
            System.out.println("We received a reply from the server: " + message);
        }else if(message.contains("end")){
            stopUDPBroadcast();
        }else{
            System.out.println("No port number found!");
        }
    }

    public void stopUDPBroadcast(){
        multicastSocket.close();
        System.out.println("Closing port");
    }
}
