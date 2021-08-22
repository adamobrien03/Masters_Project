//package authentication_one2one.tcp;
//
//import authentication_one2one.Decrypt;
//import authentication_one2one.Encrypt;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SignatureException;
//
//public class tcp_server {
//    private ServerSocket serverSocket;
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void start(int port, String required_message, String response_message) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
//        //Set up TCP Server
//        serverSocket = new ServerSocket(port);
//        //Accept incoming TCP Client Socket
//        clientSocket = serverSocket.accept();
//
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//        String incoming_message = in.readLine();
//        System.out.println("Adam: " + incoming_message);
//
//        String decrypted_message = Decrypt.decrypt_greeting_hmac_base64(incoming_message);
//        System.out.println("Adam: " + decrypted_message);
//
//        if (required_message.equals(decrypted_message)) {
//            String outgoing_greeting = Encrypt.encrypt_greeting_hmac_base64(response_message);
//            out.println(outgoing_greeting);
//        }
//        else {
//            out.println("unrecognised greeting");
//        }
//    }
//
//    public void stop() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//        serverSocket.close();
//    }
//    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
//        tcp_server server =new tcp_server();
//        server.start(6666, "hello server", "hello client");
//    }
//
//}
