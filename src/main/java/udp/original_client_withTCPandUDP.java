package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.regex.*;

public class original_client_withTCPandUDP extends Thread {
    protected Socket clientSocket;
    private DatagramSocket datagramSocket;
    private InetAddress group;
    private byte[] buffer;

    private PrintWriter out;
    private BufferedReader in;
    public final String ip = "127.0.0.1";
    public int serversDiscovered = 0;
    public int tcpPort;

    //TCP Methods
    public original_client_withTCPandUDP() throws Exception {
        this.clientSocket = new Socket();

        this.datagramSocket = new DatagramSocket();
        this.group = InetAddress.getByName("230.0.0.0");
    }

    public void startConnection(int port) throws IOException {
        //Start TCP Connection by connecting to specific server
        System.out.println("Connecting to server on port: " + port);
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        //Send TCP Message through Client Socket to server.
        out.println(msg);
        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    //UDP Methods
    public void discoverAvailableServers(String msg, int port) throws IOException {
        copyMessageOnBuffer(msg);
        multicastSendPacket(port);

        receivePackets();
    }

    private void copyMessageOnBuffer(String msg) {
        buffer = msg.getBytes();
    }

    private void multicastSendPacket(int port) throws IOException {
        //Send out UDP message on group and port
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
        datagramSocket.send(packet);
        System.out.println("Sending out UDP packet: " + new String(packet.getData()));
    }

    private void receivePackets() throws IOException {
            receivePacket();
            serversDiscovered++;
            System.out.println("How many servers have we found: " + serversDiscovered);
    }

    private void receivePacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);
        System.out.println("Receiving in UDP packet: " + new String(packet.getData()));

        checkUDPReceivedMessage(new String(packet.getData()));
    }

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
        //UDP message has to be in a specific message structure.
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        client.send(buffer, serverAddress);
    }

    public void checkUDPReceivedMessage(String message) throws IOException {

//        String regexPattern = "(\\d{4})";
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(message);

        //boolean checkforPortNumber = MatchResult(regexPattern, message);
        message = message.replaceAll("(\\D)", ""); // Replace all non-digits
        System.out.println("Using ReplaceAll to find digits: " + message);

        if (message.equals("5556")) {
            //parse the received Packet information
            //String possiblePort = message.substring(0, 4);
            tcpPort = Integer.parseInt(message);
            System.out.println("This is the port number transferred: " + message);
        }else if(message.contains("end")){
            stopUDPBroadcast();
        }else{
            System.out.println("No port number found!");
        }
    }

    public void stopUDPBroadcast(){
        datagramSocket.close();
        System.out.println("Closing port");
    }
}
