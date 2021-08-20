package All.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class TCP {
    public static int heartbeat = 0;

    public static void sendTCPMessage(String msg, PrintWriter writer) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        //Client initiates the communication with server
        String message = Encrypt.encrypt_greeting_hmac_base64(msg);
        writer.println(message);
    }
    public static boolean receiveTCPMessage(String required_message, BufferedReader reader) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(reader.readLine()));
    }

    public static void sendTCPHeartbeatMessage(String heartbeat_message, PrintWriter writer) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(heartbeat_message);
        writer.println(outgoing_greeting);

        heartbeat++;
    }

    public static boolean receiveTCPHeartbeatMessage(String required_message, BufferedReader reader) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //Decrypt starting heartbeat message from client.
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(reader.readLine()));
    }

    public static boolean checkTCPClientStatus(Socket clientSocket) throws IOException {
        return clientSocket.isConnected();
    }

    public void stopTCPClient(BufferedReader reader, PrintWriter writer, Socket client) throws IOException {
        reader.close();
        writer.close();
        client.close();
    }
}
