package All.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class UDP {
    private static String received_port = "";
    private static String received_ip = "";

    public static void sendUDPMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        String encrypted_message = Encrypt.encrypt_greeting_hmac_base64(msg);

        ByteBuffer buffer = ByteBuffer.wrap(encrypted_message.getBytes());
        client.send(buffer, serverAddress);
    }

    public static String receiveUDPMessage(DatagramChannel client) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, SignatureException, InvalidKeyException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = client.receive(buffer);
        String message = extractUDPMessage(buffer);
        System.out.println("Client at #" + remoteAdd + "  sent unencypted message of: " + message);

        String decrypted_message = Decrypt.decrypt_greeting_hmac_base64(message);

        System.out.println("Client at #" + remoteAdd + "  sent: " + decrypted_message);

        return decrypted_message;
    }

    private static String extractUDPMessage(ByteBuffer buffer) {
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String msg = new String(bytes);

        return msg;
    }

    public static boolean checkUDPClientStatus(DatagramChannel client) throws IOException {
        return client.isOpen();
    }

    public static void stopUDPClient(DatagramChannel channel) throws IOException {
        channel.close();
    }
}
