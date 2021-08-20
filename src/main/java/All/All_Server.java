package All;

import All.Util.Decrypt;
import All.Util.Encrypt;
import All.Util.UDPBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import static java.lang.Integer.parseInt;

public class All_Server {
    static byte[] buf;
    private static ServerSocket tcpServer;
    private static Socket tcpClient;
    private static DatagramChannel udpServer;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String secretPassword = "NUIG Masters Project";
    private static String private_tcp_port = "6666";
    private static int private_port = parseInt(private_tcp_port);
    private static InetSocketAddress address = new InetSocketAddress("localhost", 5555);
    private static SocketAddress remoteAdd;
    private static int heartbeat = 0;


    public static void  startUDPServer() throws IOException {
        udpServer = UDPBuilder.bindChannel(address);
        System.out.println("Server started at #" + address);
    }

    public static void startTCPServer(int port) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Set up TCP Server
        tcpServer = new ServerSocket(port);
        //Accept incoming TCP Client Socket
        tcpClient = tcpServer.accept();

        out = new PrintWriter(tcpClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(tcpClient.getInputStream()));
    }

    public static boolean checkUDPServerStatus(DatagramChannel server) throws IOException {
        return server.isOpen();
    }

    public static boolean checkTCPServerStatus(ServerSocket server) throws IOException {
        return server.isClosed();
    }

    public static void sendUDPMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String encrypted_message = Encrypt.encrypt_greeting_hmac_base64(msg);
        ByteBuffer buffer = ByteBuffer.wrap(encrypted_message.getBytes());
        client.send(buffer, serverAddress);
    }

    public static String receiveUDPMessage(DatagramChannel server) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        remoteAdd = server.receive(buffer);
        String message = extractUDPMessage(buffer);
        System.out.println("The encrypted message from the client: " + message);

        //Decrypt the UDP Message
        String decrypted_message = Decrypt.decrypt_greeting_hmac_base64(message);
        System.out.println("Server at #" + remoteAdd + "  sent: " + decrypted_message);
        return decrypted_message;
    }

    private static String extractUDPMessage(ByteBuffer buffer) {
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return new String(bytes);
    }

    private static void sendTCPMessage(String outgoing) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
            String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(outgoing);
            out.println(outgoing_greeting);
    }

    private static void sendTCPHeartbeatMessage(String heartbeat_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        //if the heartbeat message matches, keep communicating
        String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(heartbeat_message);
        out.println(outgoing_greeting);

        heartbeat++;
        System.out.println("Server Heartbeat: " + heartbeat);
    }

    public static boolean receiveTCPMessage(String required_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Decrypt incoming to verify incoming communication communication
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(in.readLine()));
    }

    public static boolean receiveTCPHeartbeatMessage(String required_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Decrypt starting heartbeat message from client.
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(in.readLine()));
    }

    public void stopTCPServer() throws IOException {
        in.close();
        out.close();
        tcpClient.close();
        tcpServer.close();
    }
    public static void stopUDPServer() throws IOException {
        udpServer.close();
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
        startUDPServer();

        while (true) {
            //Any messages that come through, we print and verify.
            String udpMessage_fromClient = receiveUDPMessage(udpServer);

            if(udpMessage_fromClient.equals(secretPassword)){
                //Send back the correct port for the client to start the TCP Server.
                System.out.println("Completed security check - sending back port " + private_tcp_port);
                sendUDPMessage(udpServer, private_tcp_port, remoteAdd);

                //Start up the TCP server and connect to the client.
                if(tcpServer==null){
                    System.out.println("Starting TCP Server and sending message");
                    startTCPServer(private_port);

                    Thread.sleep(3000);
                    if(receiveTCPMessage("Hello")){
                        //If they say Hello, we say Start
                        sendTCPMessage("Start");

                        Thread.sleep(3000);
                        System.out.println("After sleep, before While check");

                        while(receiveTCPHeartbeatMessage("Client_Heartbeat")){
                            Thread.sleep(3000);
                            sendTCPHeartbeatMessage("Server_Heartbeat");
                        }
                    }
                }
            }else{
                System.out.println("Wrong message received: " + udpMessage_fromClient + " from " + remoteAdd);
            }
        }
    }
}
