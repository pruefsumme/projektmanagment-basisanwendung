package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnector {

    // stumpf aus deiner Angabe
    private static final String HOST = "sr-labor.ddns.net";
    private static final String PORT = "3306";
    private static final String DB   = "PM_Gruppe_A";
    private static final String USER = "PM_Gruppe_A";
    private static final String PWD  = "123456789";

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB
        + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new ExceptionInInitializerError(e); }
    }

    private DbConnector() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }
}
