package All;

import All.Util.UDP;
import All.Util.UDPBuilder;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static All.Util.TCP.*;
import static All.Util.UDP.*;
import static java.lang.Integer.parseInt;

public class All_Client extends Thread {
    private static Socket clientSocket;
    private static DatagramChannel udpclient;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String received_port = "";
    private static String secretPassword = "NUIG Masters Project";
    private static InetSocketAddress serverAddress = new InetSocketAddress("localhost", 5555);

    public static DatagramChannel startUDPClient() throws IOException {
        udpclient = UDPBuilder.bindChannel(null);
        udpclient.configureBlocking(false);
        return udpclient;
    }

    public static void startTCPConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
        startUDPClient();

        while (true){
                if(checkUDPClientStatus(udpclient)) {

                    System.out.println("Sending UDP Message");
                    sendUDPMessage(udpclient, secretPassword, serverAddress);

                    System.out.println("Received UDP Message");
                    received_port = receiveUDPMessage(udpclient);

                    System.out.println("Port Number: " + received_port);
                }

            if(!received_port.equals("No message received")) {
                //Start up the TCP server and communicate.
                if (clientSocket==null) {
                    System.out.println("Starting TCP Client");
                    Thread.sleep(1000);
                    //Define the port too.
                    startTCPConnection("127.0.0.1", parseInt(received_port));

                    //Send TCP Message
                    System.out.println("Sending TCP Message");
                    Thread.sleep(1000);
                    sendTCPMessage("Hello", out);

                    Thread.sleep(2000);
                    if(receiveTCPMessage("Start", in)){
                        //If they say start, start the heartbeat message
                        System.out.println("Sending Heartbeat Message");
                        Thread.sleep(1000);
                        sendTCPHeartbeatMessage("Client_Heartbeat", out);

                        Thread.sleep(3000);
                        while(receiveTCPHeartbeatMessage("Server_Heartbeat", in)){
                            Thread.sleep(3000);
                            sendTCPHeartbeatMessage("Client_Heartbeat", out);
                            System.out.println("Client Heartbeat: " + heartbeat);
                        }

                    }else if(receiveTCPMessage("Hello", in)){
                        System.out.println("Sending TCP Message");
                        Thread.sleep(1000);
                        sendTCPMessage("Hello", out);
                    }
                    else{
                        System.out.println("Message did not match");
                    }
                }

                if(checkUDPClientStatus(udpclient)){
                    sendUDPMessage(udpclient, "Connection Complete, don't need UDP", serverAddress);
                    //Close UDP Client
                    System.out.println("Stopping UDP Client");
                    UDP.stopUDPClient(udpclient);
                }
            }
            Thread.sleep(5000);
        }
    }
}
