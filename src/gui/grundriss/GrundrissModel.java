package gui.grundriss;

import business.grundriss.Sonderwunsch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model-Klasse für die Grundriss-Varianten
 */
public class GrundrissModel {
    
    private List<Sonderwunsch> sonderwuensche;
    private List<Integer> ausgewaehlteSonderwunschIds;
    // NEW: aktuelle Hausnummer für Speicherung/Laden
    private Integer aktuelleHausnummer;
    // NEW: Konstante für Kategorie Grundriss-Varianten
    private static final int KATEGORIE_GRUNDRISS = 20;
    // NEW: DB-Verbindungsdaten
    private static final String DB_URL = "jdbc:mysql://sr-labor.ddns.net:3306/PM_Gruppe_A";
    private static final String DB_USER = "PM_Gruppe_A";
    private static final String DB_PASS = "123456789";
    
    /**
     * Konstruktor für GrundrissModel
     */
    public GrundrissModel() {
        this.sonderwuensche = new ArrayList<>();
        this.ausgewaehlteSonderwunschIds = new ArrayList<>();
    }
    
    // NEW: setzt die aktuelle Hausnummer
    /**
     * setzt die aktuelle Hausnummer für Speicherung/Laden
     * @param hausnummer Hausnummer
     */
    public void setAktuelleHausnummer(int hausnummer) {
        this.aktuelleHausnummer = hausnummer;
    }
    
    /**
     * Liest die Grundriss-Sonderwünsche aus der Datenbank
     */
    public void leseGrundrissSonderwuensche() {
        this.sonderwuensche.clear();
        
        String sql = "SELECT idSonderwunsch, Beschreibung, Preis FROM Sonderwunsch " +
                    "WHERE Sonderwunschkategorie_idSonderwunschkategorie = " + KATEGORIE_GRUNDRISS +
                    " ORDER BY idSonderwunsch";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Sonderwunsch sw = new Sonderwunsch(
                    rs.getInt("idSonderwunsch"),
                    rs.getString("Beschreibung"),
                    rs.getInt("Preis")
                );
                this.sonderwuensche.add(sw);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Lesen der Grundriss-Sonderwünsche: " + e.getMessage());
        }
        
        // NEW: bereits gespeicherte Auswahl für aktuelles Haus laden
        this.ausgewaehlteSonderwunschIds.clear();
        if (this.aktuelleHausnummer != null) {
            String sqlAuswahl = "SELECT swh.Sonderwunsch_idSonderwunsch " +
                                "FROM Sonderwunsch_has_Haus swh " +
                                "JOIN Sonderwunsch s ON s.idSonderwunsch = swh.Sonderwunsch_idSonderwunsch " +
                                "WHERE swh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = conn.prepareStatement(sqlAuswahl)) {
                ps.setInt(1, this.aktuelleHausnummer);
                ps.setInt(2, KATEGORIE_GRUNDRISS);
                try (ResultSet rsSel = ps.executeQuery()) {
                    while (rsSel.next()) {
                        this.ausgewaehlteSonderwunschIds.add(rsSel.getInt(1));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Fehler beim Laden der Auswahl: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gibt die Liste der Sonderwünsche zurück
     * @return Liste der Sonderwünsche
     */
    public List<Sonderwunsch> getSonderwuensche() {
        return this.sonderwuensche;
    }
    
    /**
     * Prüft, ob ein Sonderwunsch bereits ausgewählt ist
     * @param sonderwunschId ID des Sonderwunsches
     * @return true wenn ausgewählt, false sonst
     */
    public boolean istSonderwunschAusgewaehlt(int sonderwunschId) {
        return this.ausgewaehlteSonderwunschIds.contains(sonderwunschId);
    }
    
    /**
     * Prüft die Konstellation der ausgewählten Sonderwünsche
     * @param ausgewaehlteSw Array der ausgewählten Sonderwunsch-IDs
     * @return true wenn Konstellation gültig, false sonst
     */
    public boolean pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw) {
        // Geschäftslogik für ungültige Kombinationen:
        // - Tür (ID 2) kann nur gewählt werden, wenn auch Wand (ID 1) gewählt ist
        // - Ausführung Bad DG (ID 6) kann nur gewählt werden, wenn Vorrichtung Bad DG (ID 5) gewählt ist
        
        boolean wandGewaehlt = false;
        boolean tuerGewaehlt = false;
        boolean vorrichtungBadGewaehlt = false;
        boolean ausfuehrungBadGewaehlt = false;
        
        for (int id : ausgewaehlteSw) {
            switch (id) {
                case 1: wandGewaehlt = true; break;
                case 2: tuerGewaehlt = true; break;
                case 5: vorrichtungBadGewaehlt = true; break;
                case 6: ausfuehrungBadGewaehlt = true; break;
            }
        }
        
        // Tür ohne Wand ist ungültig
        if (tuerGewaehlt && !wandGewaehlt) {
            return false;
        }
        
        // Ausführung Bad ohne Vorrichtung ist ungültig
        if (ausfuehrungBadGewaehlt && !vorrichtungBadGewaehlt) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Berechnet den Gesamtpreis der ausgewählten Sonderwünsche
     * @param ausgewaehlteSw Array der ausgewählten Sonderwunsch-IDs
     * @return Gesamtpreis
     */
    public double berechnePreisSonderwuensche(int[] ausgewaehlteSw) {
        double gesamtpreis = 0.0;
        
        for (int id : ausgewaehlteSw) {
            for (Sonderwunsch sw : sonderwuensche) {
                if (sw.getId() == id) {
                    gesamtpreis += sw.getPreis();
                    break;
                }
            }
        }
        
        return gesamtpreis;
    }
    
    /**
     * Speichert die ausgewählten Sonderwünsche in der Datenbank
     * @param ausgewaehlteSw Array der ausgewählten Sonderwunsch-IDs
     */
    public void speichereSonderwuensche(int[] ausgewaehlteSw) {
        // NEW: Prüfung ob Hausnummer gesetzt
        if (this.aktuelleHausnummer == null) {
            System.err.println("Keine Hausnummer gesetzt. Speicherung übersprungen.");
            return;
        }
        
        // NEW: Transaktion: Alte Einträge löschen, neue einfügen
        String deleteSQL = "DELETE swh FROM Sonderwunsch_has_Haus swh " +
                           "JOIN Sonderwunsch s ON swh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                           "WHERE swh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = ?";
        String insertSQL = "INSERT INTO Sonderwunsch_has_Haus (Sonderwunsch_idSonderwunsch, Haus_Hausnr) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement del = conn.prepareStatement(deleteSQL)) {
                del.setInt(1, this.aktuelleHausnummer);
                del.setInt(2, KATEGORIE_GRUNDRISS);
                del.executeUpdate();
            }
            
            try (PreparedStatement ins = conn.prepareStatement(insertSQL)) {
                for (int id : ausgewaehlteSw) {
                    ins.setInt(1, id);
                    ins.setInt(2, this.aktuelleHausnummer);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            
            conn.commit();
            // NEW: lokale Liste aktualisieren
            this.ausgewaehlteSonderwunschIds.clear();
            for (int id : ausgewaehlteSw) {
                this.ausgewaehlteSonderwunschIds.add(id);
            }
            System.out.println("Sonderwünsche erfolgreich gespeichert für Haus " + this.aktuelleHausnummer);
        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern der Sonderwünsche: " + e.getMessage());
            e.printStackTrace();
        }
    }
}