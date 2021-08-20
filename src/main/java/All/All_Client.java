package All;

import All.Util.Decrypt;
import All.Util.Encrypt;
import All.Util.UDPBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static java.lang.Integer.parseInt;

public class All_Client extends Thread {
    private static Socket clientSocket;
    private static DatagramChannel udpclient;
    private static PrintWriter out;
    private static BufferedReader in;
    private final String ip;
    private final int port;
    private static String received_port = "";
    private static InetSocketAddress serverAddress = new InetSocketAddress("localhost", 5555);
    private static int heartbeat = 0;

    public All_Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static DatagramChannel startUDPClient() throws IOException {
        udpclient = UDPBuilder.bindChannel(null);
        udpclient.configureBlocking(false);
        return udpclient;
    }

    public static boolean checkUDPClientStatus(DatagramChannel client) throws IOException {
        return client.isOpen();
    }

    public static boolean checkTCPClientStatus(Socket clientSocket) throws IOException {
        return clientSocket.isConnected();
    }

    public static void startTCPConnection(String new_ip, int new_port) throws IOException {
        clientSocket = new Socket(new_ip, new_port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void sendTCPMessage(String msg) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        //Client initiates the communication with server
        String message = Encrypt.encrypt_greeting_hmac_base64(msg);
        out.println(message);
    }

    private static void sendTCPHeartbeatMessage(String heartbeat_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(heartbeat_message);
        out.println(outgoing_greeting);

        heartbeat++;
        System.out.println("Client Heartbeat: " + heartbeat);
    }

    public static boolean receiveTCPHeartbeatMessage(String required_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Decrypt starting heartbeat message from client.
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(in.readLine()));
    }

    public static void sendUDPMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String encrypted_message = Encrypt.encrypt_greeting_hmac_base64(msg);

        ByteBuffer buffer = ByteBuffer.wrap(encrypted_message.getBytes());
        client.send(buffer, serverAddress);
    }

    public static String receiveUDPMessage(DatagramChannel client) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = client.receive(buffer);
        String message = extractUDPMessage(buffer);
        System.out.println("Client at #" + remoteAdd + "  sent unencypted message of: " + message);

        String decrypted_message = Decrypt.decrypt_greeting_hmac_base64(message);

        System.out.println("Client at #" + remoteAdd + "  sent: " + decrypted_message);

        return decrypted_message;
    }

    private static String extractUDPMessage(ByteBuffer buffer) {
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String msg = new String(bytes);

        return msg;
    }

    public static boolean receiveTCPMessage(String required_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(in.readLine()));
    }

    public void stopTCPClient() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void stopUDPClient() throws IOException {
        udpclient.close();
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
        startUDPClient();

        while (true){
                if(checkUDPClientStatus(udpclient)) {

                    System.out.println("Sending UDP Message");
                    sendUDPMessage(udpclient, "NUIG Masters Project", serverAddress);

                    System.out.println("Received UDP Message");
                    received_port = receiveUDPMessage(udpclient);

                    System.out.println("Port Number: " + received_port);
                }

            if(!received_port.equals("No message received")) {
                //Start up the TCP server and communicate.
                if (clientSocket==null) {
                    System.out.println("Starting TCP Client");
                    Thread.sleep(1000);
                    startTCPConnection("127.0.0.1", parseInt(received_port));

                    //Send TCP Message
                    System.out.println("Sending TCP Message");
                    Thread.sleep(1000);
                    sendTCPMessage("Hello");

                    Thread.sleep(2000);
                    if(receiveTCPMessage("Start")){
                        //If they say start, start the heartbeat message
                        System.out.println("Sending Heartbeat Message");
                        Thread.sleep(1000);
                        sendTCPHeartbeatMessage("Client_Heartbeat");

                        Thread.sleep(3000);
                        while(receiveTCPHeartbeatMessage("Server_Heartbeat")){
                            Thread.sleep(3000);
                            sendTCPHeartbeatMessage("Client_Heartbeat");
                        }

                    }else if(receiveTCPMessage("Hello")){
                        System.out.println("Sending TCP Message");
                        Thread.sleep(1000);
                        sendTCPMessage("Hello");
                    }
                    else{
                        System.out.println("Message did not match");
                    }
                }

                if(checkUDPClientStatus(udpclient)){
                    sendUDPMessage(udpclient, "Connection Complete, don't need UDP", serverAddress);
                    //Close UDP Client
                    System.out.println("Stopping UDP Client");
                    stopUDPClient();
                }
            }
            Thread.sleep(5000);
        }
    }
}
