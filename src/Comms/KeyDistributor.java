package Comms;

import dominio.Security.KeyMethods;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Objects;

public class KeyDistributor {

    public boolean sendPublicKey(String receiverHost, int connectionPort) {
        try {
            Socket socket = Messenger.initSocket(receiverHost, connectionPort);

            PublicKey publicKey = KeyMethods.importPublic();

            //  Now we need to send the object through the connection.
            Messenger.initSender(socket).writeObject(publicKey);

            //  We show in the console what are we trying to send.
            System.out.print("\nLlave enviada");

            //  So now we think it has been sent, but we need to be sure of it.
            //  We are going to be receiving information from the socket to confirm
            //  the reception of the object.
            InputStream inputStream = socket.getInputStream();
            //  The server will be returning a boolean, which is already serialized, so we can make
            //  use of the already existing methods for sending and receiving booleans.
            ObjectInputStream objectReceiver = new ObjectInputStream(inputStream);
            //  At this point, we are reading the information sent as a response for our request.
            boolean aBoolean = objectReceiver.readBoolean();
            if (aBoolean) System.out.println("Llave recibida por AS correctamente");
            else System.out.println("Ha ocurrido un error al enviar la llave.");

            //  Now that we have a response we can close the communication channel.
            socket.close();

            return aBoolean;
        } catch (Exception e) {
            return false;
        }
    }

    public static PublicKey importRemotePublicKey(Socket socket) {
        try {
            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream, so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // read the list of messages from the socket
            PublicKey publicKey = (PublicKey) objectInputStream.readObject();
            // We send confirmation of receipt.
            new Messenger().returnResponse(socket, true);
            return publicKey;
        } catch (Exception e) {
            // We send confirmation of error.
            new Messenger().returnResponse(socket, false);
            return null;
        }
    }

    public static boolean importAndSavePublicKeyFromServer(Socket socket) {
        try {
            KeyMethods.saveKeys(Objects.requireNonNull(importRemotePublicKey(socket)),
                    socket.getInetAddress().toString() + ".key");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
