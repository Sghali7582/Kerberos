package authServer;

import Comms.Messenger;
import dominio.Security.Encryption;
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

    public boolean processUserRequest(Socket socket) {
        try {
            UTicket userRequest = new Messenger().acceptTicket(socket);
            Ticket ticket = userRequest.searchTicket("request");
            UTicket userResponse = new UTicket();

            System.out.println("Ticket recibido");
            userResponse.printTicket(userRequest);
            System.out.println("Final de ticket recibido");

            Encryption encryption = new Encryption();

            SecretKey sessionKey = encryption.keyGenerator();
            Timestamp timestamp = Timestamp.from(Instant.now());

            userResponse.generateResponse4User(
                    "",
                    timestamp.toString(),
                    ticket.getLifetime(),
                    encryption.convertKey2String(sessionKey));

            userResponse.generateTicket(
                    "TGT",
                    ticket.getFirstId(),
                    "TGS - Pereda",
                    timestamp.toString(),
                    ticket.getAddressIP(),
                    ticket.getLifetime(),
                    encryption.convertKey2String(sessionKey));

            return new Messenger().ticketResponse(socket, userResponse);
        } catch (Exception e) {
            return false;
        }
    }

}
