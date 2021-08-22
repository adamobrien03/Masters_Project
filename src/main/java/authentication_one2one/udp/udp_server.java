//package authentication_one2one.udp;
//
//import authentication_one2one.Decrypt;
//import authentication_one2one.UDPBuilder;
//import udp.many2many.DatagramChannelBuilder;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.*;
//import java.nio.ByteBuffer;
//import java.nio.channels.DatagramChannel;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SignatureException;
//
//public class udp_server {
//    static byte[] buf;
//
//    public static DatagramChannel startServer(int port) throws IOException {
//        InetSocketAddress address = new InetSocketAddress("localhost", port);
//        DatagramChannel server = UDPBuilder.bindChannel(address);
//
//        System.out.println("Server started at #" + address);
//
//        return server;
//    }
//
//    public static String receiveMessage(DatagramChannel server) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        SocketAddress remoteAdd = server.receive(buffer);
//        String message = extractMessage(buffer);
//
//        String decrypted_message = Decrypt.decrypt_greeting_hmac_base64(message);
//
//        System.out.println("Client at #" + remoteAdd + "  sent: " + decrypted_message);
//
//        return decrypted_message;
//    }
//
//    private static String extractMessage(ByteBuffer buffer) {
//        buffer.flip();
//
//        byte[] bytes = new byte[buffer.remaining()];
//        buffer.get(bytes);
//
//        String msg = new String(bytes);
//
//        return msg;
//    }
//
//    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
//        DatagramChannel server = startServer(5555);
//
//        while (true) {
//            buf = receiveMessage(server).getBytes();
//            System.out.write(buf);
//        }
//    }
//}