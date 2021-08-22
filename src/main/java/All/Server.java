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
import static All.Util.UDP.*;
import static java.lang.Integer.parseInt;

public class Server {
    private static ServerSocket tcpServer;
    private static Socket tcpClient;
    private static DatagramChannel udpServer;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void  startUDPServer(String hostname, int ipAddress) throws IOException {
        InetSocketAddress address = new InetSocketAddress(hostname, ipAddress);
        udpServer = UDPBuilder.bindChannel(address);
        System.out.println("Server started at #" + address);
    }

    public static void startTCPServer(int port) throws IOException {
        //Set up TCP Server
        tcpServer = new ServerSocket(port);
        //Accept incoming TCP Client Socket
        tcpClient = tcpServer.accept();

        out = new PrintWriter(tcpClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(tcpClient.getInputStream()));
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
        startUDPServer(System.getProperty("hostname"), parseInt(System.getProperty("portNumber")));

        while (true) {
            //Any messages that come through, we print and verify.
            if(receiveUDPMessage(udpServer, System.getProperty("secretPassword")).equals(System.getProperty("secretPassword"))){
                //Send back the correct port for the client to start the TCP Server.
                System.out.println("Completed security check - sending back port " + System.getProperty("private_tcp_port"));
                sendUDPMessage(udpServer, System.getProperty("private_tcp_port"), System.getProperty("secretPassword"), remoteAdd);

                //Start up the TCP server and connect to the client.
                if(tcpServer==null){
                    System.out.println("Starting TCP Server and sending message with " + System.getProperty("private_tcp_port"));
                    int private_port = parseInt(System.getProperty("private_tcp_port"));
                    startTCPServer(private_port);

                    if(receiveTCPMessage(System.getProperty("firstMessage"), System.getProperty("secretPassword"), in)){
                        //If they say Hello, we say Start
                        sendTCPHeartbeatMessage(System.getProperty("secondMessage"), System.getProperty("secretPassword"), out);
                        whileHeartbeat(System.getProperty("requiredHeartbeat"), System.getProperty("heartbeatMessage"), System.getProperty("secretPassword"),out, in);
                    }
                }else{
                    System.out.println("TCP Server already started");
                }
            }else{
                System.out.println("Wrong message received: " + receiveUDPMessage(udpServer, System.getProperty("secretPassword")) + " from " + remoteAdd);
            }
        }
    }
}
