package Server;

import Comms.KeyDistributor;

public class KeysDist {
    public static void main(String[] args) {
        new KeyDistributor().sendPublicKey("localhost", 5501);
        System.out.println("Llave enviada a TGS");
    }
}
