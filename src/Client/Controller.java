package Client;

import Comms.KeyDistributor;
import dominio.Security.Encryption;
import dominio.Security.KeyMethods;
import dominio.Ticket.Ticket;
import dominio.Ticket.UTicket;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Silver-VS
 */

public class Controller {
    public static void main(String[] args) {
        //to run this we need 4 args:
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        do {
            try {
                System.out.print("Ingrese su ID: ");
                String userID = reader.readLine();
                System.out.print("Ingrese el ID del servicio: ");
                String serviceID = reader.readLine();
                System.out.print("Ingrese el tiempo de vida solicitado: ");
                String requestedLifetime = reader.readLine();

                System.out.print("\nSolicitud al AS");
                //  We send the public key
                flag = new KeyDistributor().sendPublicKey("26.78.224.3", 5500);

                //  We send the request ticket to the AS and receive the response from the AS
                UTicket responseFromAS = RequestAccess.startAuth(userID, serviceID, requestedLifetime);
                //  We decrypt the tickets with our private key.
                responseFromAS.decryptTicket(KeyMethods.importPrivate(), "responseToClient");
                responseFromAS.decryptTicket(KeyMethods.importPrivate(), "TGT");
                //  We show in console the tickets unencrypted, but we should only be able to read the response to
                //  the client, but not TGT, as this is still encrypted with the Public key of the TGS.
                System.out.println("\nTickets decrypted: ");
                responseFromAS.printTicket(responseFromAS, "responseToClient");
                System.out.println("Termina respuesta del AS\n");


                //We send the response from the AS to the TGS, including our authenticator and our ID.
                System.out.print("\nSolicitud al TGS\n");
                UTicket responseFromTGS = RequestAccess.followTGS(responseFromAS, serviceID, requestedLifetime,
                        userID, Timestamp.from(Instant.now()).toString());
                //  We decrypt the tickets using the key sent by the AS for communication with the TGS.
                Ticket responseAS = responseFromAS.searchTicket("responseToClient");
                SecretKey sessionKeyClientTGS = new Encryption().convertString2Key(responseAS.getKey());
                responseFromTGS.decryptTicket(sessionKeyClientTGS, "responseToClient");
                responseFromTGS.decryptTicket(sessionKeyClientTGS, "serviceTicket");
                //  We show in console the tickets unencrypted, but we should only be able to read the response to
                //  the client, but not serviceTicket, as this is still encrypted with the Public key of the Server.
                responseFromTGS.printTicket(responseFromTGS);
                System.out.println("Termina respuesta del TGS\n");

                System.out.println("\nSolicitud al servidor");
                Ticket responseTGS = responseFromTGS.searchTicket("responseToClient");
                SecretKey sessionKeyClientServer = new Encryption().convertString2Key(responseTGS.getKey());

                UTicket responseFromServer = RequestAccess.askForService(responseFromTGS, userID,
                        Timestamp.from(Instant.now()).toString(), sessionKeyClientServer);
                //  We decrypt the ticket using the key sent by the TGS for communication with the Server.

                responseFromServer.decryptTicket(sessionKeyClientServer, "auth");
                responseFromServer.printTicket(responseFromServer, "auth");
                System.out.println("Termina solicitud del servidor.");

            } catch (Exception e) {
                System.out.print("\n Ingrese datos correctos por favor.");
            }
        } while (flag);

    }
}
