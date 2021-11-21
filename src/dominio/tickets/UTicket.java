package dominio.tickets;

import dominio.Encryption;

import javax.crypto.SecretKey;
import java.util.ArrayList;

/**
 * This class should be used to create, encrypt and decrypt all the Tickets created
 * to transit in the network.
 *
 * @author Silver_VS
 */
public class UTicket {
    private ArrayList<Ticket> tickets;

    /**
     * Method to initialize the arraylist for a new UTicket.
     */
    public UTicket() {
        tickets = new ArrayList<>();
    }

    public Ticket searchTicket(String id) {
        for (Ticket i : tickets) {
            if (i.getIdTicket().equals(id)) {
                return i;
            }
        }
        return null;
    }

    /**
     * This ticket will be the one that the user sends to the AS at the time of asking for a service.
     * In other words, this should be the first ticket sent in the network.
     */
    public void generateRequest(String userID, String serviceID, String userIP, String requestedLifetime) {
        Ticket request = new Ticket();
        request.setIdTicket("request");
        request.setFirstId(userID);
        request.setSecondId(serviceID);
        request.setAddressIP(userIP);
        request.setLifetime(requestedLifetime);
        tickets.add(request);
    }

    public void generateResponse4User(String id, String timeStamp, String lifetime, String key) {
        Ticket response = new Ticket();
        response.setIdTicket("responseToClient");
        response.setFirstId(id);
        response.setTimeStamp(timeStamp);
        response.setLifetime(lifetime);
        response.setKey(key);
        tickets.add(response);
    }

    public void generateTicket(String nameOfTicket, String firstID, String secondID, String timeStamp, String addressIP,
                               String lifetime, String key) {
        tickets.add(
                new Ticket(nameOfTicket, firstID, secondID, timeStamp, addressIP, lifetime, key)
        );
    }

    public void request4TGS(String serviceID, String requestedLifetime) {
        Ticket request = new Ticket();
        request.setIdTicket("request4TGS");
        request.setFirstId(serviceID);
        request.setLifetime(requestedLifetime);
        tickets.add(request);
    }

    public void authenticator(String id, String timeStamp) {
        Ticket auth = new Ticket();
        auth.setIdTicket("auth");
        auth.setFirstId(id);
        auth.setTimeStamp(timeStamp);
        tickets.add(auth);
    }



    public boolean[] getFilled(Ticket ticket) {
        boolean[] existingFields = new boolean[6];
        existingFields[0] = ticket.isFilledFirstId();
        existingFields[1] = ticket.isFilledSecondId();
        existingFields[2] = ticket.isFilledAddressIP();
        existingFields[3] = ticket.isFilledLifetime();
        existingFields[4] = ticket.isFilledTimeStamp();
        existingFields[5] = ticket.isFilledKey();
        return existingFields;
    }

    public boolean encryptTicket(SecretKey key, String id) {
        try {
            Encryption encryption = new Encryption();
            Ticket toEncrypt = searchTicket(id);
            boolean[] existingFields = getFilled(toEncrypt);
            if (existingFields[0]) {
                toEncrypt.setFirstId(encryption.encrypt(key, toEncrypt.getFirstId()));
            }
            if (existingFields[1]) {
                toEncrypt.setSecondId(encryption.encrypt(key, toEncrypt.getSecondId()));
            }
            if (existingFields[2]) {
                toEncrypt.setAddressIP(encryption.encrypt(key, toEncrypt.getAddressIP()));
            }
            if (existingFields[3]) {
                toEncrypt.setLifetime(encryption.encrypt(key, toEncrypt.getLifetime()));
            }
            if (existingFields[4]) {
                toEncrypt.setTimeStamp(encryption.encrypt(key, toEncrypt.getTimeStamp()));
            }
            if (existingFields[5]) {
                toEncrypt.setKey(encryption.encrypt(key, toEncrypt.getKey()));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean decryptTicket(SecretKey key, String id){
        try {
            Encryption decryption = new Encryption();
            Ticket toDecrypt = searchTicket(id);
            boolean[] existingFields = getFilled(toDecrypt);
            if (existingFields[0]) {
                toDecrypt.setFirstId(decryption.decrypt(key, toDecrypt.getFirstId()));
            }
            if (existingFields[1]) {
                toDecrypt.setSecondId(decryption.decrypt(key, toDecrypt.getSecondId()));
            }
            if (existingFields[2]) {
                toDecrypt.setAddressIP(decryption.decrypt(key, toDecrypt.getAddressIP()));
            }
            if (existingFields[3]) {
                toDecrypt.setLifetime(decryption.decrypt(key, toDecrypt.getLifetime()));
            }
            if (existingFields[4]) {
                toDecrypt.setTimeStamp(decryption.decrypt(key, toDecrypt.getTimeStamp()));
            }
            if (existingFields[5]) {
                toDecrypt.setKey(decryption.decrypt(key, toDecrypt.getKey()));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
