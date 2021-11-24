package Server;

import Comms.Messenger;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;

public class ProcessRequest {
    public void processUserRequest(Socket socket){
        UTicket userRequest = new Messenger().acceptTicket(socket);
        Ticket serviceTicket = userRequest.searchTicket("serviceTicket");
        Ticket userAuth = userRequest.searchTicket("auth");

        if (serviceTicket.getFirstId().equals(userAuth.getFirstId()))
            approveSession(socket);
        else {
            boolean flag;
            do flag = new Messenger().negateResponse(socket); while (!flag);
        }
    }

    public void approveSession(Socket socket){
        UTicket approved = new UTicket();
        approved.addAuthenticator("ServiceAuth", Timestamp.from(Instant.now()).toString());
        boolean flag;
        do flag = new Messenger().ticketResponse(socket, approved); while (!flag);
    }
}