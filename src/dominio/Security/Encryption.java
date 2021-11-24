package dominio.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Silver-VS
 */

public class Encryption {

    public SecretKey keyGenerator() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("DES").generateKey();
    }

    public String convertKey2String(SecretKey secretKey){
        byte[] keyEncoded = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyEncoded);
    }

    public SecretKey convertString2Key(String keyInString){
        byte[] decodedKey = Base64.getDecoder().decode(keyInString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }

    public String encrypt(SecretKey secretKey, String toEncrypt) throws Exception {
        Cipher encrypt = Cipher.getInstance("DES");
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bytesToEncrypt = toEncrypt.getBytes(StandardCharsets.UTF_8);
        byte[] bytesEncrypted = encrypt.doFinal(bytesToEncrypt);
        bytesEncrypted = Base64.getEncoder().encode(bytesEncrypted);
        return new String(bytesEncrypted);
    }

    public String decrypt(SecretKey secretKey, String toDecrypt) throws Exception {
        Cipher decrypt = Cipher.getInstance("DES");
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytesToDecrypt = Base64.getDecoder().decode(toDecrypt.getBytes());
        byte[] bytesDecrypted = decrypt.doFinal(bytesToDecrypt);
        return new String(bytesDecrypted);
    }
}