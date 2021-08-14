package udp.one2one;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class one2one_server{
    protected MulticastSocket multicastSocket = null;

    protected byte[] buffer = new byte[256];
    protected InetAddress group = null;

    private DatagramPacket packet;
    private InetAddress address = null;
    private int packetPort;

    private PrintWriter out;
    private BufferedReader in;

    private final int udpPortNumber = 5555;

    public one2one_server() throws IOException {
        //Start UDP Server
        multicastSocket = new MulticastSocket(udpPortNumber);
        multicastSocket.setReuseAddress(true);
        group = InetAddress.getByName("230.0.0.0");
        multicastSocket.joinGroup(group);
    }

    //UDP Methods
    public void run() {
        try {
            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                System.out.println("Received " + packet.getData() + " into the server");
                address = packet.getAddress();
                checkReceivedUDPMessage(new String(packet.getData(), 0, packet.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnUDPPacket(String message) throws IOException {
        packetPort = packet.getPort();

        buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, address, packetPort);

        multicastSocket.send(packet);
    }

    public void checkReceivedUDPMessage(String message) throws IOException {

        if (message.contains("hello")) {
            //parse the received Packet information from the client
            returnUDPPacket("Server says hi");
            System.out.println("Received a packet from a client with correct word, sending back to: " + address);
        }else if(message.contains("end")){
            System.out.println("Client wants to end communication, sending end");
            returnUDPPacket("end");
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        }else{
            System.out.println("Correct message not sent to this server, sending end");
            returnUDPPacket("end");
        }
    }

    public static void main(String[] args) throws Exception {
         one2one_server server = new one2one_server();
         server.run();
        }
    }