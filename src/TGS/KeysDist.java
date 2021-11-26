package TGS;

import Comms.KeyDistributor;
import Comms.Messenger;

import java.io.IOException;
import java.net.ServerSocket;

public class KeysDist {
    public static void main(String[] args) throws IOException {
        new KeyDistributor().sendPublicKey("26.78.224.3", 1250);
        System.out.println("Llave enviada a AS");
        Messenger messenger = new Messenger();
        ServerSocket serverSocket = messenger.initServerSocket(5501);
        System.out.println("TGS en espera de clave publica del Servidor...");
        if (KeyDistributor.importAndSavePublicKeyFromServer(messenger.acceptRequest(serverSocket))) {
            System.out.println("Clave del TGS recuperada exitosamente");
        } else System.out.println("Recibo de llave fallido");
        serverSocket.close();
    }
}