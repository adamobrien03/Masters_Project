package All.Util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

public class HashSetup {
    public static String calculateHMAC(byte[] data, byte[] key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return toHexString(mac.doFinal(data));
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for(int i = 0; i < bytes.length; ++i) {
            byte b = bytes[i];
            formatter.format("%02x", b);
        }

        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
