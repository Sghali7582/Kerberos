package dominio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>This class should be used to create, encrypt and decrypt tickets.
 * <br>One <i>UTicket</i> can contain several tickets.</p>
 *
 * @author SilverVS
 * @version 1.1
 */

//TODO: add methods and things for the IP direction of the user

public class UTicket {
    private ArrayList<Ticket> UTicket;

    public void addTicket(String ID, String secondID, java.security.Timestamp timestamp){
     Ticket ticket = new Ticket(ID, secondID, timestamp);
     UTicket.add(ticket);
    }

    public void addTicket(String key, String id, Timestamp lifetime,
                          java.security.Timestamp timeOfExpedition) {
        Ticket ticket = new Ticket(key, id, lifetime, timeOfExpedition);
        UTicket.add(ticket);
    }

    /**
     * @param secondId Ideally, the only ticket that should not have a secondId <br>
     *                should be the one that the user sends for the first time to the AS.
     */
    public void addTicket(String key, String id, String secondId, Timestamp lifetime,
                          java.security.Timestamp timeOfExpedition) {
        Ticket ticket = new Ticket(key, id, secondId, lifetime, timeOfExpedition);
        UTicket.add(ticket);
    }

    public Ticket searchTicket(String idTicket) {
        Ticket ticket = null;
        for (Ticket i : UTicket) {
            if (Objects.equals(i.getID(), idTicket)) {
                ticket = i;
                break;
            }
        }
        return ticket;
    }

    public boolean encryptTicket(String idTicket, String key) {

        Ticket ticket = searchTicket(idTicket);
        if (ticket == null) {
            return false;
        }
        Encryption encryption = new Encryption();
        ticket.setKey(encryption.encrypt(key, ticket.getKey()));
        ticket.setID(encryption.encrypt(key, ticket.getID()));
        if (ticket.getSecondID() != null) {
            ticket.setSecondID(encryption.encrypt(key, ticket.getSecondID()));
        }
        ticket.setLifetime(encryption.encrypt(key, ticket.getLifetime()));
        ticket.setTS(encryption.encrypt(key, ticket.getTS()));

        return true;
    }

    public boolean decryptTicket(String idTicket, String key) {
        Ticket ticket = searchTicket(idTicket);
        if (ticket == null) {
            return false;
        }
        Encryption encryption = new Encryption();
        ticket.setKey(encryption.decrypt(key, ticket.getKey()));
        ticket.setID(encryption.decrypt(key, ticket.getID()));
        if (ticket.getSecondID() != null) {
            ticket.setSecondID(encryption.decrypt(key, ticket.getSecondID()));
        }
        ticket.setLifetime(encryption.decrypt(key, ticket.getLifetime()));
        ticket.setTS(encryption.decrypt(key, ticket.getTS()));

        return true;
    }
}
