package udp.many2many;

//import com.many2many.MultiClient;
//
//import com.many2many.many2many_client;
//import com.many2many.many2many_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    //private many2many_server multiServer;
    protected MulticastSocket multicastSocket = null;

    protected byte[] buf = new byte[256];
    protected InetAddress group = null;

    private DatagramPacket packet;
    private InetAddress address = null;
    private int packetPort;

    private PrintWriter out;
    private BufferedReader in;

    private final int udpPortNumber = 5555;
    final int tcpPortNumber = 5556;

    public Server() throws IOException {
        multicastSocket = new MulticastSocket(udpPortNumber);
        multicastSocket.setReuseAddress(true);
        group = InetAddress.getByName("230.0.0.0");
        multicastSocket.joinGroup(group);

//        multiServer = new MultiServer();
//        multiServer.start(portNumber);
    }

    public Server(int port) throws IOException {
//        multiServer = new many2many_server();
//        multiServer.start(tcpPortNumber);
    }

    public void startTCPServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Started TCP Server");

        //clientSocket = serverSocket.accept();
//        while (true) {
//            System.out.println("Accepting clients");
//            new MultiClient(clientSocket, "127.0.0.1", port).start();
//        }
    }

    //TCP Methods
    public void stopTCPServer() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    //UDP Methods
    public void run() {
        try {
            while (true) {
                packet = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(packet);

                System.out.println("Received " + packet.getData() + " into the server");

                checkUDPReceivedMessage(new String(packet.getData(), 0, packet.getLength()));
                //startTCPServer(portNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUDPPacket(String message) throws IOException {
        address = packet.getAddress();
        packetPort = packet.getPort();

        buf = message.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, packetPort);

        multicastSocket.send(packet);
    }

    public void checkUDPReceivedMessage(String message) throws IOException {

        if (message.contains("hello")) {
            //parse the received Packet information from the client
            String tcpPort = String.valueOf(tcpPortNumber);
            sendUDPPacket(tcpPort);
            System.out.println("Received a packet from a client with correct word, sending back: " + tcpPortNumber);
        }else if(message.contains("end")){
            System.out.println("Client wants to end communication, sending end");
            sendUDPPacket("end");
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        }else{
            System.out.println("Correct message not sent to this server, sending end");
            sendUDPPacket("end");
        }
    }

    public void runMultipleTCPServers() throws IOException {
        //write ports to an array/arrayList
        int[] portsLaunched = new int[4];
        int port = 5556;
        Server server = new Server(port);

        for(int i = 0; i < 4; i++) {
            portsLaunched[i] = port;
            server.serverSocket = new ServerSocket(port);
            port = port + 1111;
        }

        System.out.println("What ports have been gathered? " + Arrays.toString(portsLaunched));

        while(true){
//            new many2many_client(server.clientSocket, "127.0.0.1", portsLaunched[0]).start();
//            new many2many_client(server.clientSocket, "127.0.0.1", portsLaunched[1]).start();
//            new many2many_client(server.clientSocket, "127.0.0.1", portsLaunched[2]).start();
//            new many2many_client(server.clientSocket, "127.0.0.1", portsLaunched[3]).start();
        }
    }

    public static void main(String[] args) throws IOException {
        int[] portsLaunched = new int[4];
        int port = 5556;

        Server server1 = new Server(5556);
        server1.serverSocket = new ServerSocket(5556);

        Server server2 = new Server(6667);
        server2.serverSocket = new ServerSocket(6667);

        Server server3 = new Server(7778);
        server3.serverSocket = new ServerSocket(7778);

        Server server4 = new Server(8889);
        server4.serverSocket = new ServerSocket(8889);

        System.out.println("What ports have been gathered? " + Arrays.toString(portsLaunched));

        while(true){
//            new many2many_client(server1.clientSocket, "127.0.0.1", 5556).start();
//            new many2many_client(server2.clientSocket, "127.0.0.1", 6667).start();
//            new many2many_client(server3.clientSocket, "127.0.0.1", 7778).start();
//            new many2many_client(server4.clientSocket, "127.0.0.1", 8889).start();
        }


//            int port = 5556;
//            Server server = new Server(port);
//            server.serverSocket = new ServerSocket(port);
//            while (true) {
//                new MultiClient(server.clientSocket, "127.0.0.1", port).start();
//            }
        }
    }