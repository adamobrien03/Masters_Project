package All;

import All.Util.UDPBuilder;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static All.Util.TCP.*;
import static All.Util.UDP.receiveUDPMessage;
import static All.Util.UDP.sendUDPMessage;
import static java.lang.Integer.parseInt;

public class All_Server {
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
                    if(receiveTCPMessage("Hello", in)){
                        //If they say Hello, we say Start
                        sendTCPMessage("Start", out);

                        Thread.sleep(3000);
                        System.out.println("After sleep, before While check");

                        while(receiveTCPHeartbeatMessage("Client_Heartbeat", in)){
                            Thread.sleep(3000);
                            sendTCPHeartbeatMessage("Server_Heartbeat", out);
                            System.out.println("Server Heartbeat: " + heartbeat);
                        }
                    }
                }
            }else{
                System.out.println("Wrong message received: " + udpMessage_fromClient + " from " + remoteAdd);
            }
        }
    }
}
