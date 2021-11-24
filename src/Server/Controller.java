package Server;

import Comms.Messenger;

import java.net.ServerSocket;

/**
 * @author Silver-VS
 */

public class Controller {
    public static void main(String[] args) {
        Messenger messenger = new Messenger();
        ServerSocket serverSocket = messenger.initServerSocket(5502);
        System.out.println("Servidor iniciado y esperando peticiones...");

        do {
            new ProcessRequest().processUserRequest(messenger.acceptRequest(serverSocket));
            System.out.println("Respuesta enviada al cliente.");
        } while (!serverSocket.isClosed());
    }
}
