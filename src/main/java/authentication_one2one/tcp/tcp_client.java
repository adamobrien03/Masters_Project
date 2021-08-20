package authentication_one2one.tcp;

import authentication_one2one.Decrypt;
import authentication_one2one.Encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class tcp_client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String ip;
    private final int port;

    //Show encryption over TCP
    public tcp_client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startConnection() throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String msg) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        String message = Encrypt.encrypt_greeting_hmac_base64(msg);

        System.out.println("Adam Client encrypted msg = " + msg);
        System.out.println("Adam Client raw msg = " + message);

        out.println(message);
    }

    public String receiveMessage() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        String incoming_message = in.readLine();
        return Decrypt.decrypt_greeting_hmac_base64(incoming_message);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
