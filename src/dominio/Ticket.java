package dominio;

import java.sql.Timestamp;

/**
 * This class is merely the base for one ticket.
 * <h3>In order to create a ticket please make use of the methods in the class UTicket.</h3>
 *
 * @author SilverVS
 * @version 1.1
 */

public class Ticket {
    /**
     * The security Timestamp <i>"TS"</i> will include the time the ticket was emited,<br> and by whom.
     */
    private String key;
    private String ID;
    private String secondID;
    private Timestamp lifetime;
    private java.security.Timestamp TS;

//    TODO add methods and things for the IP direction of the user

    public Ticket(String ID, String secondID, java.security.Timestamp timestamp){
        this.ID = ID;
        this.secondID = secondID;
        this.TS = timestamp;
    }

    public Ticket(String key, String ID, Timestamp lifetime, java.security.Timestamp TS) {
        this.key = key;
        this.ID = ID;
        this.lifetime = lifetime;
        this.TS = TS;
    }

    public Ticket(String key, String ID, String secondID, Timestamp lifetime, java.security.Timestamp TS) {
        this.key = key;
        this.ID = ID;
        this.secondID = secondID;
        this.lifetime = lifetime;
        this.TS = TS;
    }

    public String getSecondID() {
        return secondID;
    }

    public void setSecondID(String secondID) {
        this.secondID = secondID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Timestamp getLifetime() {
        return lifetime;
    }

    public void setLifetime(Timestamp lifetime) {
        this.lifetime = lifetime;
    }

    public java.security.Timestamp getTS() {
        return TS;
    }

    public void setTS(java.security.Timestamp TS) {
        this.TS = TS;
    }
}
