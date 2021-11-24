package Client;

import Comms.Messenger;
import dominio.Security.Encryption;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;

public class RequestAccess {

    public static UTicket startAuth(String userID, String serviceID, String userIP, String requestedLifetime){
        UTicket serviceRequest = new UTicket();
        serviceRequest.generateRequest(userID, serviceID, userIP, requestedLifetime);
        return new Messenger().sendTicket("localhost",5500, serviceRequest);
    }

    public static UTicket followTGS(UTicket ticketFromAS, String serviceID, String requestedLifetime,
                             String userID, String timeStamp){
        UTicket followUpTicketTGS = new UTicket();
        followUpTicketTGS.addTicket(ticketFromAS.searchTicket("TGT"));
        followUpTicketTGS.request4TGS(serviceID, requestedLifetime);
        followUpTicketTGS.addAuthenticator(userID, timeStamp);
        return new Messenger().sendTicket("localhost", 5501, followUpTicketTGS);
    }

    public static UTicket askForService(UTicket ticketFromTGS, String userID, String timeStamp){
        UTicket askForService = new UTicket();
        askForService.addTicket(ticketFromTGS.searchTicket("serviceTicket"));
        askForService.addAuthenticator(userID, timeStamp);
        return new Messenger().sendTicket("localhost", 5502, askForService);

    }

    public static SecretKey getKeyFromTicket(Ticket ticket){
        return new Encryption().convertString2Key(ticket.getKey());
    }

}
