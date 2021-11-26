package dominio.Security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Silver-VS
 */

public class KeyMethods {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        saveKeys();
        System.out.println("Se generaron de manera correcta las llaves");
    }

    public static void saveKeys() throws NoSuchAlgorithmException, IOException {
        KeyPair keys = generatePairKeys();
        saveKeys(keys);
    }

    public static void saveKeys(KeyPair keys) throws IOException {
        saveKeys(keys.getPublic(), "publicKey.key");
        saveKeys(keys.getPrivate(), "privateKey.key");
    }

    public static KeyPair generatePairKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance("RSA");
        return generatorRSA.generateKeyPair();
    }

    public static void saveKeys(Key key, String fileName) throws IOException {

        String directoryPath = "C:\\Escuela\\Quinto\\Kerberos\\src\\dominio\\Keys";
        String path = directoryPath + "\\" + fileName;

        byte[] keyBytes = key.getEncoded();
        FileOutputStream stream = new FileOutputStream(path);
        stream.write(keyBytes);
        stream.close();
    }

    public static KeyPair importKeysFromLocal() throws Exception {
        return importKeysFromLocal("publicKey.key", "privateKey.key");
    }

    public static KeyPair importKeysFromLocal(String publicKeyFileName, String privateKeyFileName)
            throws Exception {
        return new KeyPair(importPublic(publicKeyFileName), importPrivate(privateKeyFileName));
    }

    public static PublicKey importPublic() throws Exception {
        return importPublic("publicKey.key");
    }

    public static PublicKey importPublic(String publicKeyFileName)
            throws Exception {

        byte[] publicBytes = retrieveKey(publicKeyFileName);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpecPublic = new X509EncodedKeySpec(publicBytes);
        return keyFactory.generatePublic(keySpecPublic);
    }

    public static PrivateKey importPrivate() throws Exception {
        return importPrivate("privateKey.key");
    }

    public static PrivateKey importPrivate(String privateKeyFileName)
            throws Exception {

        byte[] privateBytes = retrieveKey(privateKeyFileName);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpecPrivate = new X509EncodedKeySpec(privateBytes);

        return keyFactory.generatePrivate(keySpecPrivate);
    }

    public static String correctPath4Keys(String fileName) {
        return "C:\\Escuela\\Quinto\\Kerberos\\src\\dominio\\Keys\\" + fileName;
    }

    public static byte[] retrieveKey(String fileName) throws Exception {
        FileInputStream privateIn = new FileInputStream(correctPath4Keys(fileName));
        byte[] bytes = new byte[privateIn.available()];
        privateIn.read(bytes);
        privateIn.close();
        return bytes;
    }

}