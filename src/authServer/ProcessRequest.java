package authServer;

import Comms.Messenger;
import dominio.Security.Encryption;
import dominio.Security.KeyMethods;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.PublicKey;
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

            userResponse.generateResponse4User( // Name of ticket: responseToClient
                    "TGS - Victor", // ID TGS
                    timestamp.toString(), // TS 2
                    ticket.getLifetime(), // Tiempo de vida 2
                    encryption.convertKey2String(sessionKey)); // K c-tgs

            userResponse.generateTicket(
                    "TGT", // Ticket TGS
                    ticket.getFirstId(), // ID c
                    "TGS - Victor", // ID tgs
                    timestamp.toString(), // TS 2
                    socket.getInetAddress().toString(), //AD c
                    ticket.getLifetime(), // Tiempo de vida 2
                    encryption.convertKey2String(sessionKey)); // K c-tgs

            String publicTGSName = "26.144.135.252" + ".key";
            String publicClientName = "26.78.223.219" + ".key";
            PublicKey publicTGS = KeyMethods.importPublic(publicTGSName);
            PublicKey publicClient = KeyMethods.importPublic(publicClientName);

            userResponse.encryptTicket(publicTGS, "TGT");

            userResponse.encryptTicket(publicClient, "TGT");
            userResponse.encryptTicket(publicClient, "TGS - Victor");

            return new Messenger().ticketResponse(socket, userResponse);
        } catch (Exception e) {
            return false;
        }
    }

}
