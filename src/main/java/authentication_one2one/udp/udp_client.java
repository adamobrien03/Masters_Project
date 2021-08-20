package authentication_one2one.udp;

import authentication_one2one.Encrypt;
import authentication_one2one.UDPBuilder;
import udp.many2many.DatagramChannelBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class udp_client extends Thread {

    public static DatagramChannel startClient() throws IOException {
        DatagramChannel client = UDPBuilder.bindChannel(null);
        client.configureBlocking(false);
        return client;
    }

    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String encrypted_message = Encrypt.encrypt_greeting_hmac_base64(msg);

        ByteBuffer buffer = ByteBuffer.wrap(encrypted_message.getBytes());
        client.send(buffer, serverAddress);
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        DatagramChannel client = startClient();
        String msg = "Hello, this is a Baeldung's DatagramChannel based UDP client!";
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7001);

        sendMessage(client, msg, serverAddress);
    }
}
