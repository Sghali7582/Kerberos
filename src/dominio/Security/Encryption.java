package dominio.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author Silver-VS
 */

public class Encryption {

    public SecretKey keyGenerator() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("DES").generateKey();
    }

    public String convertKey2String(SecretKey secretKey) {
        byte[] keyEncoded = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyEncoded);
    }

    public SecretKey convertString2Key(String keyInString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyInString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }

    public String encrypt(Cipher encryptCipher, String toEncrypt) throws Exception {
        byte[] bytesToEncrypt = toEncrypt.getBytes(StandardCharsets.UTF_8);
        byte[] bytesEncrypted = encryptCipher.doFinal(bytesToEncrypt);
        bytesEncrypted = Base64.getEncoder().encode(bytesEncrypted);
        return new String(bytesEncrypted);
    }

    public String decrypt(Cipher decryptCypher, String toDecrypt) throws Exception {
        byte[] bytesToDecrypt = Base64.getDecoder().decode(toDecrypt.getBytes());
        byte[] bytesDecrypted = decryptCypher.doFinal(bytesToDecrypt);
        return new String(bytesDecrypted);
    }

    public String encryptSymmetric(SecretKey secretKey, String toEncrypt) throws Exception {
        Cipher encryptCypher = Cipher.getInstance("DES");
        encryptCypher.init(Cipher.ENCRYPT_MODE, secretKey);
        return encrypt(encryptCypher, toEncrypt);
    }

    public String decryptSymmetric(SecretKey secretKey, String toDecrypt) throws Exception {
        Cipher decryptCypher = Cipher.getInstance("DES");
        decryptCypher.init(Cipher.DECRYPT_MODE, secretKey);
        return decrypt(decryptCypher, toDecrypt);
    }

    public String publicEncrypt(PublicKey publicKey, String toEncrypt) throws Exception {
        Cipher encryptCypher = Cipher.getInstance("RSA");
        encryptCypher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encrypt(encryptCypher, toEncrypt);
    }

    public String privateDecrypt(PrivateKey privateKey, String toDecrypt) throws Exception {
        Cipher decryptCypher = Cipher.getInstance("RSA");
        decryptCypher.init(Cipher.DECRYPT_MODE, privateKey);
        return decrypt(decryptCypher, toDecrypt);
    }
}