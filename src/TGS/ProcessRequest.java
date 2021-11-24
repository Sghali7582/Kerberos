package TGS;

import Comms.Messenger;
import dominio.Security.Encryption;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;

public class ProcessRequest {

    public boolean processUserRequest(Socket socket) {
        try {
            UTicket userRequest = new Messenger().acceptTicket(socket);
            Ticket tgt = userRequest.searchTicket("TGT");
            Ticket userService = userRequest.searchTicket("request4TGS");
            Ticket userAuth = userRequest.searchTicket("auth");

            if (tgt.getFirstId().equals(userAuth.getFirstId())) {

                Encryption encryption = new Encryption();

                SecretKey sessionKey = encryption.keyGenerator();
                Timestamp timestamp = Timestamp.from(Instant.now());

                UTicket userResponse = new UTicket();
                userResponse.generateResponse4User(
                        userService.getFirstId(),
                        timestamp.toString(),
                        userService.getLifetime(),
                        encryption.convertKey2String(sessionKey)
                );

                userResponse.generateTicket(
                        "serviceTicket",
                        tgt.getFirstId(),
                        userService.getFirstId(),
                        timestamp.toString(),
                        tgt.getAddressIP(),
                        userService.getLifetime(),
                        encryption.convertKey2String(sessionKey)
                );

                return new Messenger().ticketResponse(socket, userResponse);
            } else {
                boolean flag;
                do flag = new Messenger().negateResponse(socket); while (!flag);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
