package authServer;

import Comms.KeyDistributor;
import Comms.Messenger;

import java.net.ServerSocket;

public class Controller {

    /**
     * @author Silver-VS
     */

    public static void main(String[] args) {
        Messenger messenger = new Messenger();

        ServerSocket serverSocket = messenger.initServerSocket(5500);
        System.out.println("AS iniciado y esperando peticiones...");

        do {
            if (KeyDistributor.importAndSavePublicKeyFromServer(messenger.acceptRequest(serverSocket))) {
                System.out.println("Clave del cliente recuperada exitosamente");
            } else System.out.println("Recibo de llave fallido");

            if (new ProcessRequest().processUserRequest(messenger.acceptRequest(serverSocket))) {
                System.out.println("Respuesta enviada del AS al cliente.");
            } else {
                System.out.println("Ha ocurrido un error en la respuesta.");
            }
        } while (!serverSocket.isClosed());
    }

}
