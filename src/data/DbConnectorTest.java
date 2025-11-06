package data;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectorTest {
    public static void main(String[] args) {
        System.out.println("Teste Datenbankverbindung ...");

        try (Connection conn = DbConnector.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Verbindung erfolgreich!");
            } else {
                System.out.println("Verbindung fehlgeschlagen.");
            }
        } catch (SQLException e) {
            System.err.println("SQL-Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
