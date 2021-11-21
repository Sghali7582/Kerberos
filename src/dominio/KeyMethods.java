package dominio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyMethods {

//    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
//        saveKeys();
//    }

    public static void generateSecretKey(){

    }

    public static void savePairKeys() throws NoSuchAlgorithmException, IOException {
        KeyPair keys = generatePairKeys();
        savePairKeys(keys);
    }

    public static void savePairKeys(KeyPair keys) throws IOException {
        savePairKeys(keys.getPublic(), "publicKey.key");
        savePairKeys(keys.getPrivate(), "privateKey.key");
    }

    public static KeyPair generatePairKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance("RSA");
        return generatorRSA.generateKeyPair();
    }

    // TODO: Correct path to save keys
    public static void savePairKeys(Key key, String fileName) throws IOException {
        byte[] keyBytes = key.getEncoded();
        FileOutputStream stream = new FileOutputStream(fileName);
        stream.write(keyBytes);
        stream.close();
    }

    public static KeyPair importKeysFromServer() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return importKeysFromServer("publicKey.key", "privateKey.key");
    }

    public static KeyPair importKeysFromServer(String publicKeyFileName, String privateKeyFileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//        TODO: Change URL form string to retrieve key
        return new KeyPair(importPublic(publicKeyFileName), importPrivate(privateKeyFileName));
    }

    public static PublicKey importPublic(String publicKeyFileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream publicIn = new FileInputStream(publicKeyFileName);
        byte[] publicBytes = new byte[publicIn.available()];
        publicIn.read(publicBytes);
        publicIn.close();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpecPublic = new X509EncodedKeySpec(publicBytes);
        return keyFactory.generatePublic(keySpecPublic);
    }

    public static PrivateKey importPrivate(String privateKeyFileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream privateIn = new FileInputStream(privateKeyFileName);

        byte[] privateBytes = new byte[privateIn.available()];

        privateIn.read(privateBytes);
        privateIn.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpecPrivate = new X509EncodedKeySpec(privateBytes);

        return keyFactory.generatePrivate(keySpecPrivate);
    }
}