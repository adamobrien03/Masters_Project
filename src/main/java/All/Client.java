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

public class Client extends Thread {
    private static Socket clientSocket;
    private static DatagramChannel udpclient;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String received_port = "";
    //private static InetSocketAddress serverAddress = new InetSocketAddress("localhost", 5555);

    public static DatagramChannel startUDPClient(String hostname, int ipAddress) throws IOException {
        //InetSocketAddress address = new InetSocketAddress(hostname, ipAddress);
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
        startUDPClient(System.getProperty("hostname"), parseInt(System.getProperty("portNumber")));
        InetSocketAddress serverAddress = new InetSocketAddress(System.getProperty("hostname"), parseInt(System.getProperty("portNumber")));

        while (true) {
                System.out.println("Sending UDP Message");
                sendUDPMessage(udpclient, System.getProperty("secretPassword"), System.getProperty("secretPassword"), serverAddress);

                System.out.println("Received UDP Message");
                received_port = receiveUDPMessage(udpclient, System.getProperty("secretPassword"));

            if (!received_port.equals("No message received")) {

                //Start up the TCP Client and communicate.
                if (clientSocket == null) {
                    System.out.println("Starting TCP Client");

                    //Grab the ipAddress and port.
                    String hostAddress = remoteAdd.toString().substring(1, remoteAdd.toString().indexOf(":"));
                    startTCPConnection(hostAddress, parseInt(received_port));

                    //Send TCP Message
                    System.out.println("Sending TCP Message");
                    sendTCPMessage(System.getProperty("firstMessage"), System.getProperty("secretPassword"), out);

                    if (receiveTCPMessage(System.getProperty("secondMessage"), System.getProperty("secretPassword"), in)) {
                        //If they say start, start the heartbeat message
                        System.out.println("Sending Heartbeat Message");
                        sendTCPHeartbeatMessage(System.getProperty("requiredHeartbeat"), System.getProperty("secretPassword"), out);

                        whileHeartbeat(System.getProperty("heartbeatMessage"), System.getProperty("requiredHeartbeat"), System.getProperty("secretPassword"), out, in);
                    } else {
                        System.out.println("Message did not match");
                    }
                }

                if (checkUDPClientStatus(udpclient)) {
                    sendUDPMessage(udpclient, "Connection Complete, don't need UDP", System.getProperty("secretPassword"), serverAddress);
                    //Close UDP Client
                    System.out.println("Stopping UDP Client");
                    UDP.stopUDPClient(udpclient);
                }
            }
        }
    }
}
