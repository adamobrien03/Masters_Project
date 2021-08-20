package test;

import All.All_Client;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static All.All_Client.*;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

public class All {
    @Test
    public void client_test() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Send out UDP message with the right password
        DatagramChannel udpClient = startUDPClient();

        //For UDP Messages, the port and socketAddress need to be known first.
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 5555);
        System.out.println("Sending UDP Message");
        sendUDPMessage(udpClient, "NUIG Masters Project", serverAddress);

        //Get the port number from the server
        System.out.println("Received UDP Message");
        String port = receiveUDPMessage(udpClient);

        //Close UDP Client
        System.out.println("Stopping UDP Client");
        stopUDPClient();

        //Start up the TCP server and communicate.
        System.out.println("Starting TCP Client");
        startTCPConnection("127.0.0.1", parseInt(port));

        //Send TCP Message
        System.out.println("Sending TCP Message");
        sendTCPMessage("Hello");

        //assertEquals("hello client", cs1.receiveMessage());
    }
}
