package udp;

//import com.many2many.many2many_client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class original_server_withTCPandUDP extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
//    private MultiServer multiServer;
//    protected MulticastSocket multicastSocket = null;
    private DatagramChannel datagramChannel;


    protected byte[] buf = new byte[256];
    protected InetAddress group = null;

    private DatagramPacket packet;
    private InetAddress address = null;
    private int packetPort;

    private PrintWriter out;
    private BufferedReader in;

    private final int udpPortNumber = 5555;
    final int tcpPortNumber = 5556;

    int portUsed;

    public original_server_withTCPandUDP() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", udpPortNumber);
        datagramChannel = DatagramChannel.open().bind(address);
//        multicastSocket.setReuseAddress(true);
//        group = InetAddress.getByName("230.0.0.0");
//        datagramChannel.joinGroup(group);
    }

//    public Server(int port, boolean reUseAddress) throws IOException {
//        multicastSocket = new MulticastSocket(port);
//        multicastSocket.setReuseAddress(reUseAddress);
//        group = InetAddress.getByName("230.0.0.0");
//        multicastSocket.joinGroup(group);
//    }

    public original_server_withTCPandUDP(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        portUsed = port;
    }

//    public Server(int udpPort, int tcpPort) throws IOException {
//        multicastSocket = new MulticastSocket(udpPort);
//        multicastSocket.setReuseAddress(true);
//        group = InetAddress.getByName("230.0.0.0");
//        multicastSocket.joinGroup(group);
//
//        serverSocket = new ServerSocket(tcpPort);
//    }

    public void startTCPServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Started TCP Server");
    }

    //TCP Methods
    public void stopTCPServer() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
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

    //UDP Methods
    public void run() {
        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                SocketAddress clientAddress = datagramChannel.receive(buffer);
                String message = extractMessage(buffer);

//                packet = new DatagramPacket(buf, buf.length);
//                datagramChannel.receive(packet);

                System.out.println("Client at #" + clientAddress + "  sent: " + message);

                //checkUDPReceivedMessage(new String(packet.getData(), 0, packet.getLength()));
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

        //multicastSocket.send(packet);
    }

    public void checkUDPReceivedMessage(String message) throws IOException {

        if (message.contains("hello")) {
            //parse the received Packet information from the client

            System.out.println("Server port number is: " + portUsed);

            //String tcpPort = String.valueOf(tcpPortNumber);
            sendUDPPacket(String.valueOf(portUsed));
            System.out.println("Received a packet from a client with correct word, sending back: " + tcpPortNumber);

        } else if (message.contains("end")) {
            System.out.println("Client wants to end communication, sending end");
            sendUDPPacket("end");
            //multicastSocket.leaveGroup(group);
            //multicastSocket.close();
        } else {
            System.out.println("Correct message not sent to this server, sending end");
            sendUDPPacket("end");
        }
    }

    public static void main(String[] args) throws IOException {
        //Start TCP Server independently.
        int tcpPort = 5556;

        original_server_withTCPandUDP server = new original_server_withTCPandUDP(tcpPort);
        while (true) {
//            new many2many_client(server.clientSocket, "127.0.0.1", tcpPort).start();
        }

//         Server server = new Server();
//         server.serverSocket = new ServerSocket(server.tcpPortNumber);
//         while (true) {
//                new MultiClient(server.clientSocket, "127.0.0.1", server.tcpPortNumber).start();
//            }
//        }

//        Server server1 = new Server(5556);
        //server1.start();

//        Server server2 = new Server(6667);
//        server2.serverSocket = new ServerSocket(6667);
//
        //Server server3 = new Server(7778);
//        server3.serverSocket = new ServerSocket(7778);
//
//        Server server4 = new Server(8889);
//        server4.serverSocket = new ServerSocket(8889);

        //System.out.println("What ports have been gathered? " + Arrays.toString(portsLaunched));

        //while (true) {
//            new MultiClient(server1.clientSocket, "127.0.0.1", 5556).start();
//            new MultiClient(server2.clientSocket, "127.0.0.1", 6667).start();
            //new MultiClient(server3.clientSocket, "127.0.0.1", 7778).start();
//            new MultiClient(server4.clientSocket, "127.0.0.1", 8889).start();
        //}
    }
}