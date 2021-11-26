package TGS;

import Comms.Messenger;

import java.net.ServerSocket;

/**
 * @author Silver-VS
 */

public class Controller {
    public static void main(String[] args) {
        Messenger messenger = new Messenger();

        ServerSocket serverSocket = messenger.initServerSocket(5501);
        System.out.println("TGS iniciado y esperando peticiones...");

        do {
            if (new TGS.ProcessRequest().processUserRequest(messenger.acceptRequest(serverSocket))) {
                System.out.println("Respuesta enviada del TGS al cliente.");
            } else {
                System.out.println("Ha ocurrido un error en la respuesta.");
            }
        } while (!serverSocket.isClosed());
    }
}
