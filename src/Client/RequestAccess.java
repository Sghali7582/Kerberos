package Client;

import Comms.Messenger;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;

/**
 * @author Silver-VS
 */

public class RequestAccess {

    public static UTicket startAuth(String userID, String serviceID, String requestedLifetime) {
        UTicket serviceRequest = new UTicket();
        serviceRequest.generateRequest(userID, serviceID, requestedLifetime);
        return new Messenger().sendTicket("26.78.224.3", 5500, serviceRequest);
    }

    public static UTicket followTGS(UTicket ticketFromAS, String serviceID, String requestedLifetime,
                                    String userID, String timeStamp) {
        UTicket followUpTicketTGS = new UTicket();
        followUpTicketTGS.addTicket(ticketFromAS.searchTicket("TGT"));
        followUpTicketTGS.request4TGS(serviceID, requestedLifetime);
        followUpTicketTGS.addAuthenticator(userID, "26.78.223.219", timeStamp);
        return new Messenger().sendTicket("26.144.135.252", 5501, followUpTicketTGS);
    }

    public static UTicket askForService(UTicket ticketFromTGS, String userID, String timeStamp, SecretKey key) {
        UTicket askForService = new UTicket();
        askForService.addTicket(ticketFromTGS.searchTicket("serviceTicket"));
        askForService.addAuthenticator(userID, "26.78.223.219", timeStamp);
        askForService.encryptTicket(key, "auth");
        return new Messenger().sendTicket("26.66.159.197", 5502, askForService);

    }
}
