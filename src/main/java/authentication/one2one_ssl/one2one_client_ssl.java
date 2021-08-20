package authentication.one2one_ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class one2one_client_ssl {
    //sslsocket gives ssl for sending data.
    protected SSLSocket clientSocket;
    private final DatagramSocket datagramSocket;
    private final InetAddress group;
    private byte[] buffer;

    private PrintWriter out;
    private BufferedReader in;
    public int serversDiscovered = 0;
    public int tcpPort;

    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

    public one2one_client_ssl() throws Exception {
        this.clientSocket = (SSLSocket) sslsocketfactory.createSocket();
        this.datagramSocket = new DatagramSocket();
        this.group = InetAddress.getByName("230.0.0.0");
    }

    public one2one_client_ssl(String ip, int port) throws Exception {
        this.clientSocket = (SSLSocket) sslsocketfactory.createSocket(ip, port);
        this.datagramSocket = new DatagramSocket();
        this.group = InetAddress.getByName("230.0.0.0");
    }

    public void startConnection(Socket client) throws IOException {
        //Start TCP Connection
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        //Send TCP Message
        out.println(msg);
        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void discoverAvailableServers(String msg) throws IOException {
        copyMessageOnBuffer(msg);
        multicastSendPacket();

        receivePackets();
    }

    private void copyMessageOnBuffer(String msg) {
        buffer = msg.getBytes();
    }

    private void multicastSendPacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 5555);
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
        datagramSocket.close();
        System.out.println("Closing port");
    }
}
