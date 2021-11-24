package Client;

import dominio.Ticket.UTicket;

import java.io.BufferedReader;
import java.io.IOException;
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
        do{
            try{
                System.out.print("Ingrese su ID: ");
                String userID = reader.readLine();
                System.out.print("Ingrese el ID del servicio: ");
                String serviceID = reader.readLine();
                System.out.print("Ingrese el tiempo de vida solicitado: ");
                String requestedLifetime = reader.readLine();
                String userIP = "miIP";

                System.out.print("\nSolicitud al AS");
                UTicket responseFromAS = RequestAccess.startAuth(userID, serviceID, userIP, requestedLifetime);
                System.out.println("Termina respuesta del AS");

                System.out.print("\nSolicitud al TGS");
                UTicket responseFromTGS = RequestAccess.followTGS(responseFromAS, serviceID, requestedLifetime,
                        userID, Timestamp.from(Instant.now()).toString());
                System.out.println("Respuesta?");
                    responseFromTGS.printTicket(responseFromTGS);
                System.out.println("Termina respuesta del TGS");

                System.out.println("\nSolicitud al servidor");
                UTicket responseFromServer = RequestAccess.askForService(responseFromTGS, userID,
                        Timestamp.from(Instant.now()).toString());
                System.out.println("Termina solicitud del servidor.");


                //Una vez que se obtenga un ticket en respuesta, tenemos que reenviar el tiket nuestro con el del AS al
                //TGS, ya están los métodos, solo es encriptar el ticket y mandarlo.
            } catch (IOException e){
                System.out.print("\n Ingrese datos correctos por favor.");
            }
        } while (flag);

    }
}
