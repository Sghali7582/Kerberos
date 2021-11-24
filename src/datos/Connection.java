package datos;

import java.sql.DriverManager;

/**
 * @author Silver-VS
 */

public class Connection {

    private java.sql.Connection con;

    public java.sql.Connection getCon() {
        return con;
    }

    public void setCon(java.sql.Connection con) {
        this.con = con;
    }

    public void connect(String databaseName) throws Exception {
        //We look for the database driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        //We establish the credentials to access the database
        String databasePassword = "";
        String databaseUser = "root";

        //We use the database URL with its configurations.
        String databaseURL =
                "jdbc:mysql://" + //MySQL database.
                        "localhost:3306/" + //Location of database (host:port).
                        databaseName +  //Database name
                        "?useSSL=false" + //Secure Socket Layout, we need a certificate in order to use this.
                        "&useUnicode=true" + //Encoding for the database.
                        "&useJDBCCompliantTimezoneShift=true" + //Avoid problems with time zones.
                        "&useLegacyDatetimeCode=false" + //No legacy required.
                        "&serverTimezone=UTC" + //Self-explanatory.
                        "&allowPublicKeyRetrieval=true"; //Self-explanatory.

        //We create the connection with the URL and the credentials to access.
        con = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
    }

    public void disconnect() throws Exception {
        if (con != null) {
            if (!con.isClosed()) {
                con.isClosed();
            }
        }
    }
}