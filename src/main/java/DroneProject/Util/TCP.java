package DroneProject.Util;

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

    public static void sendTCPMessage(String msg, String password, PrintWriter writer) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        String message = Encrypt.encrypt_greeting_hmac_base64(msg, password);
        writer.println(message);
    }

    public static boolean receiveTCPMessage(String required_message, String password, BufferedReader reader) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        return required_message.contains(Decrypt.decrypt_greeting_hmac_base64(reader.readLine(), password));
    }

    public static void sendTCPHeartbeatMessage(String heartbeat_message, String password, PrintWriter writer) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(heartbeat_message, password);
        writer.println(outgoing_greeting);

        heartbeat++;
    }

    public static boolean checkTCPClientStatus(Socket clientSocket) throws IOException {
        return clientSocket.isConnected();
    }

    public void stopTCPClient(BufferedReader reader, PrintWriter writer, Socket client) throws IOException {
        reader.close();
        writer.close();
        client.close();
    }

    public static void whileHeartbeat(String required, String heartbeatMessage, String password, PrintWriter writer, BufferedReader reader) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException, InterruptedException {
        while(receiveTCPMessage(required, password, reader)){
            Thread.sleep(1000);
            sendTCPHeartbeatMessage(heartbeatMessage, password, writer);
            System.out.println("Heartbeat: " + heartbeat);
        }
    }
}
