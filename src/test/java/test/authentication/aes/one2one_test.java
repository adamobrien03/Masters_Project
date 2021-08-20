package test.authentication.aes;

import org.junit.Test;
import authentication_one2one.tcp.tcp_client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.junit.Assert.assertEquals;

public class one2one_test {
    @Test
    public void one2one_authentication_test() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        //port matches the port in one2one_server.
        tcp_client cs1 = new tcp_client("127.0.0.1", 6666);
        cs1.startConnection();
        cs1.sendMessage("hello server");
        assertEquals("hello client", cs1.receiveMessage());
    }
}
