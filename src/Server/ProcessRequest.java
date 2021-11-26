package Server;

import Comms.Messenger;
import dominio.Security.Encryption;
import dominio.Security.KeyMethods;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Silver-VS
 */

public class ProcessRequest {
    public void processUserRequest(Socket socket) {
        try {
            UTicket userRequest = new Messenger().acceptTicket(socket);

            userRequest.decryptTicket(KeyMethods.importPrivate(), "serviceTicket");

            Ticket serviceTicket = userRequest.searchTicket("serviceTicket");

            SecretKey sessionKeyClientServer = new Encryption().convertString2Key(serviceTicket.getKey());

            userRequest.decryptTicket(sessionKeyClientServer, "auth");

            Ticket userAuth = userRequest.searchTicket("auth");


            if (serviceTicket.getFirstId().equals(userAuth.getFirstId())) {
                if (serviceTicket.getSecondId().equals(userAuth.getSecondId()) &&
                        userAuth.getSecondId().equals(socket.getInetAddress().toString()))
                    approveSession(socket, serviceTicket);
            }
            boolean flag;
            do flag = new Messenger().returnResponse(socket, false); while (!flag);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void approveSession(Socket socket, Ticket serviceTicket) {
        UTicket approved = new UTicket();
        approved.addAuthenticator("ServiceAuth", "26.66.159.197",
                Timestamp.from(Instant.now()).toString());
        approved.encryptTicket(new Encryption().convertString2Key(serviceTicket.getKey()), "V - Diego");
        boolean flag;
        do flag = new Messenger().ticketResponse(socket, approved); while (!flag);
    }
}