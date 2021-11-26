package authServer;

import Comms.KeyDistributor;
import Comms.Messenger;

import java.io.IOException;
import java.net.ServerSocket;

public class KeysDist {
    public static void main(String[] args) throws IOException {
        Messenger messenger = new Messenger();
        ServerSocket serverSocket = messenger.initServerSocket(1250);
        System.out.println("AS en espera de clave publica de TGS...");
        if (KeyDistributor.importAndSavePublicKeyFromServer(messenger.acceptRequest(serverSocket))) {
            System.out.println("Clave del TGS recuperada exitosamente");
        } else System.out.println("Recibo de llave fallido");
        serverSocket.close();
    }
}
