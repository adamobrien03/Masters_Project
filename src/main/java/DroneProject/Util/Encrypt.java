package DroneProject.Util;

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

public class Encrypt {

    public static String aes_encrypt(String message, byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        //Encrypt message to Base64
        byte[] srcBytes = message.getBytes(StandardCharsets.UTF_8);

        //Able to specify the type of cipher to utilise from a enum library.
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        //Get the hash value.
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        //encrypt the value with the hash value using the password/shared key
        sha.update(password);
        //digest() creates the hash value key.
        byte[] key = sha.digest();
        key = Arrays.copyOf(key, 16);

        //Create a secret key
        SecretKeySpec spec = new SecretKeySpec(key, "AES");

        //Initializing for encrypt operation
        GCMParameterSpec GCMParams = new GCMParameterSpec(128, new byte[cipher.getBlockSize()]);
        cipher.init(cipher.ENCRYPT_MODE, spec, GCMParams);
        //Finishes the encryption method, all together.
        byte[] cipherText = cipher.doFinal(srcBytes);
        byte[] encryptedBytes = new byte[12 + cipherText.length];
        return encrypt_text_base64(encryptedBytes);
    }

    public static String encrypt_text(String message){
        //Base64 encryption
        byte[] encryptedText = message.getBytes();
        return Base64.getEncoder().encodeToString(encryptedText);
    }

    public static String encrypt_text_base64(byte[] message){
        //Base64 encryption
        return Base64.getEncoder().encodeToString(message);
    }

    //Encrypting HMAC and Base64 encryption; putting them together in one string.
    public static String encrypt_greeting_hmac_base64(String message, String password) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        //calculate HMAC to be added
        String hmac = HashSetup.calculateHMAC(message.getBytes(), password.getBytes());

        //add to String
        String full_message = hmac + message;

        return encrypt_text(full_message);
    }

    //Encrypting AES and adding HMAC for validation
    public String encrypt_greeting_aes(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        //calculate HMAC to be added
        String hmac = HashSetup.calculateHMAC(message.getBytes(), "password".getBytes());

        //add to String
        String full_message = hmac + message;

        //encrypt entire String
        byte[] encrypted_message = aes_encrypt(message, "password".getBytes()).getBytes();

        return encrypted_message.toString();
    }
}
