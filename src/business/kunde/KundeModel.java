package business.kunde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import data.DbConnector;

import javafx.collections.*;

/**
 * Klasse, welche das Model des Grundfensters mit den Kundendaten enthaelt.
 */
public final class KundeModel {

    // enthaelt den aktuellen Kunden
    private Kunde kunde;

    /* enthaelt die Plannummern der Haeuser, diese muessen vielleicht noch
       in eine andere Klasse verschoben werden */
    ObservableList<Integer> plannummern =
            FXCollections.observableArrayList(
                    0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);


    // enthaelt das einzige KundeModel-Objekt
    private static KundeModel kundeModel;

    /* privater Konstruktor gemaess des Singleton-Pattern */
    private KundeModel(){
        super();
    }

    /**
     *  Methode zum Erhalt des einzigen KundeModel-Objekts.
     *  Das Singleton-Pattern wird realisiert.
     *  @return KundeModel, welches das einzige Objekt dieses
     *          Typs ist.
     */
    public static KundeModel getInstance(){
        if(kundeModel == null){
            kundeModel = new KundeModel();
        }
        return kundeModel;
    }

    /**
     * gibt die Ueberschrift zum Grundfenster mit den Kundendaten heraus
     * @return String, Ueberschrift zum Grundfenster mit den Kundendaten
     */
    public String getUeberschrift(){
        return "Verwaltung der Sonderwunschlisten";
    }

    /**
     * gibt saemtliche Plannummern der Haeuser des Baugebiets heraus.
     * @return ObservableList<Integer> , enthaelt saemtliche Plannummern der Haeuser
     */
    public ObservableList<Integer> getPlannummern(){
        return this.plannummern;
    }

    // ---- Datenbankzugriffe -------------------

    /**
     * speichert ein Kunde-Objekt in die Datenbank
     * @param kunde, Kunde-Objekt, welches zu speichern ist
     * @throws SQLException, Fehler beim Speichern in die Datenbank
     * @throws Exception, unbekannter Fehler
     */
    public void speichereKunden(Kunde kunde) throws SQLException,
            Exception {
        this.kunde = kunde;

        final String sql =
                "INSERT INTO Kunde (Haus_Hausnr, Vorname, Nachname, Telefon, email) " +
                        "VALUES (?,?,?,?,?)";

        try (Connection con = DbConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {


            if (kunde.getHausnummer() > 0) {
                ps.setInt(1, kunde.getHausnummer());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, kunde.getVorname() == null ? "" : kunde.getVorname());

            String nn = (kunde.getNachname() == null
                    || kunde.getNachname().isBlank())
                    ? "" : kunde.getNachname();
            ps.setString(3, nn);

            if (kunde.getTelefonnummer() == null || kunde.getTelefonnummer().isBlank())
                ps.setNull(4, Types.VARCHAR);
            else
                ps.setString(4, kunde.getTelefonnummer());

            if (kunde.getEmail() == null || kunde.getEmail().isBlank())
                ps.setNull(5, Types.VARCHAR);
            else
                ps.setString(5, kunde.getEmail());

            ps.executeUpdate();
        }
    }

    /**
     * aktualisiert die Kundendaten in der Datenbank. Falls zu der Hausnummer
     * noch kein Kunde existiert, wird ein neuer Kunde angelegt.
     * Änderungen werden damit sofort in der Datenbank persistiert.
     *
     * @param kunde Kunde-Objekt mit den neuen Daten
     */
    public void aktualisiereKunden(Kunde kunde) throws SQLException, Exception {
        if (kunde == null) {
            throw new IllegalArgumentException("kunde darf nicht null sein");
        }

        final String sql =
                "UPDATE Kunde SET Vorname = ?, Nachname = ?, Telefon = ?, email = ? " +
                        "WHERE Haus_Hausnr = ?";

        try (Connection con = DbConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Vorname (nie null in der Oberfläche)
            ps.setString(1, kunde.getVorname() == null ? "" : kunde.getVorname());

            // Nachname
            String nn = (kunde.getNachname() == null || kunde.getNachname().isBlank())
                    ? "" : kunde.getNachname();
            ps.setString(2, nn);

            // Telefon (optional)
            if (kunde.getTelefonnummer() == null || kunde.getTelefonnummer().isBlank()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, kunde.getTelefonnummer());
            }

            // E-Mail (optional, aber formell geprüft in der View)
            if (kunde.getEmail() == null || kunde.getEmail().isBlank()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, kunde.getEmail());
            }

            // Hausnummer (WHERE-Bedingung)
            if (kunde.getHausnummer() > 0) {
                ps.setInt(5, kunde.getHausnummer());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            int rows = ps.executeUpdate();

            // Wenn kein Datensatz aktualisiert wurde, wird ein neuer angelegt
            if (rows == 0) {
                speichereKunden(kunde);
            }
        }
    }

    /**
     * löscht alle Kunden zu einer Hausnummer aus der Datenbank.
     *
     * @param hausnr Hausnummer des Hauses, zu dem alle Kunden gelöscht werden sollen
     */
    public void loescheKundenZuHaus(int hausnr) throws SQLException {
        final String sql = "DELETE FROM Kunde WHERE Haus_Hausnr = ?";

        try (Connection con = DbConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, hausnr);
            ps.executeUpdate();
        }
    }


    public Kunde ladeLetztenKundenZuHaus(int hausnr) throws SQLException {
        final String sql =
                "SELECT Haus_Hausnr, Vorname, Nachname, Telefon, email " +
                        "FROM Kunde WHERE Haus_Hausnr = ? " +
                        "ORDER BY idKunde DESC LIMIT 1";

        try (Connection con = DbConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, hausnr);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Kunde k = new Kunde();
                k.setHausnummer(rs.getInt("Haus_Hausnr"));
                k.setVorname(rs.getString("Vorname"));
                k.setNachname(rs.getString("Nachname"));
                k.setTelefonnummer(rs.getString("Telefon"));
                k.setEmail(rs.getString("email"));
                return k;
            }
        }
    }
}
