package All.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class Decrypt {
    //Decrypt AES Cipher message
    public static byte[] aes_decrypt(String encryptedText, byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = decrypt_text(encryptedText).getBytes(StandardCharsets.UTF_8);

        //Follow the same parameters back
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(password);
        key = Arrays.copyOf(key, 16);

        SecretKeySpec spec = new SecretKeySpec(key, "AES");
        //GCMParameterSpec GCMParams = new GCMParameterSpec(128, new byte[cipher.getBlockSize()]);
        GCMParameterSpec GCMParams = new GCMParameterSpec(128, encryptedBytes, 0, 12);

        //Initialising the decryption operation
        cipher.init(cipher.DECRYPT_MODE, spec, GCMParams);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes, 12, encryptedBytes.length - 12);

        return decryptedBytes;
    }

    //Decrypting Base64 encoded string
    public static String decrypt_text(String encryptedText) {
        //Base64 encryption
        return new String((Base64.getDecoder().decode(encryptedText)));
    }

    //Decrypting Base64 encoded string and HMAC; with HMAC validation
    public static String decrypt_greeting_hmac_base64(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        try {
            String decryptedMessage = decrypt_text(message);

            //byte[] hmac = Arrays.copyOfRange(decryptedMessage, 0, 40);
            String hmac = decryptedMessage.substring(0, 40);
            //byte[] msg = Arrays.copyOfRange(decryptedMessage, 40, message.length());
            byte[] msg = decryptedMessage.substring(40).getBytes();

            if (!HashSetup.calculateHMAC(msg, "password".getBytes()).equals(hmac)) {
                System.out.println("Message corruption detected!");
            }

            return decryptedMessage.substring(40);
        } catch (IndexOutOfBoundsException ae) {
            return ("No message received");
        } catch (NumberFormatException ae) {
            return ("No message received");
        }
    }



    //Decrypting AES; passing message and password key.
    public String decrypt_greeting_aes(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SignatureException {
        byte[] decryptedMessage = Decrypt.aes_decrypt(message, "password".getBytes());

        byte[] hmac = Arrays.copyOfRange(decryptedMessage, 0, 40);
        byte[] msg = Arrays.copyOfRange(decryptedMessage, 40, message.length());

        if (!HashSetup.calculateHMAC(msg, "password".getBytes()).equals(new String(hmac))) {
            System.out.println("Message corruption detected!");
        }

        return msg.toString();
    }
}
