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
        this.initSonderwuensche();
    }

    // Descriptions and Prices for Fenster (Category 30)
    private static final String[] FENSTER_DESCRIPTIONS = {
        "Schiebetueren im EG zur Terrasse",
        "Schiebetueren im DG zur Dachterrasse",
        "Erhoehter Einbruchschutz an der Haustuer",
        "Vorbereitung el. Antriebe Rolladen EG",
        "Vorbereitung el. Antriebe Rolladen OG",
        "Vorbereitung el. Antriebe Rolladen DG",
        "Elektrische Rolladen EG",
        "Elektrische Rolladen OG",
        "Elektrische Rolladen DG"
    };

    private static final int[] FENSTER_PRICES = {
        590, 590, 690, 190, 190, 190, 990, 990, 990
    };

    // Descriptions and Prices for Innentueren (Category 40)
    private static final String[] INNENTUEREN_DESCRIPTIONS = {
        "Glasausschnitt (Klarglas) in einer Innentuer",
        "Glasausschnitt (Milchglas) in einer Innentuer",
        "Innentuer zur Garage als Holztuer"
    };
    private static final int[] INNENTUEREN_PRICES = {
        460, 560, 660
    };

    // Descriptions and Prices for Heizungen (Category 50)
    private static final String[] HEIZUNG_DESCRIPTIONS = {
        "Zusaetzlicher Standard-Heizkoerper",
        "Heizkoerper mit glatter Oberflaeche",
        "Handtuchheizkoerper",
        "Fussbodenheizung ohne DG",
        "Fussbodenheizung mit DG"
    };
    private static final int[] HEIZUNG_PRICES = {
        660, 160, 660, 8990, 9990
    };

    // Descriptions and Prices for Sanitaer (Category 60)
    private static final String[] SANITAER_DESCRIPTIONS = {
        "Groesseres Waschbecken im OG",
        "Groesseres Waschbecken im DG",
        "Bodentiefe Dusche im OG",
        "Bodentiefe Dusche im DG"
    };
    private static final int[] SANITAER_PRICES = {
        160, 160, 560, 560
    };


    private void initSonderwuensche() {
        try (Connection con = DbConnector.getConnection()) {
            // Check/Init Category 30 (Fenster)
            for (int i = 0; i < FENSTER_DESCRIPTIONS.length; i++) {
                String desc = FENSTER_DESCRIPTIONS[i];
                int price = FENSTER_PRICES[i];
                initSingleSonderwunsch(con, desc, price, 30);
            }
            // Check/Init Category 40 (Innentueren)
            for (int i=0; i < INNENTUEREN_DESCRIPTIONS.length; i++) {
                String desc = INNENTUEREN_DESCRIPTIONS[i];
                int price = INNENTUEREN_PRICES[i];
                initSingleSonderwunsch(con, desc, price, 40);
            }
            // Check/Init Category 50 (Heizungen)
            for (int i=0; i < HEIZUNG_DESCRIPTIONS.length; i++) {
                String desc = HEIZUNG_DESCRIPTIONS[i];
                int price = HEIZUNG_PRICES[i];
                initSingleSonderwunsch(con, desc, price, 50);
            }
            // Check/Init Category 60 (Sanitaer)
            for (int i=0; i < SANITAER_DESCRIPTIONS.length; i++) {
                String desc = SANITAER_DESCRIPTIONS[i];
                int price = SANITAER_PRICES[i];
                initSingleSonderwunsch(con, desc, price, 60);
            }
         } catch (Exception e) { e.printStackTrace(); }
    }

    private void initSingleSonderwunsch(Connection con, String desc, int price, int categoryId) throws SQLException {
        try (PreparedStatement check = con.prepareStatement("SELECT * FROM Sonderwunsch WHERE Beschreibung = ?")) {
            check.setString(1, desc);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                try (PreparedStatement insert = con.prepareStatement("INSERT INTO Sonderwunsch (Beschreibung, Preis, Sonderwunschkategorie_idSonderwunschkategorie) VALUES (?, ?, ?)")) {
                     insert.setString(1, desc);
                     insert.setInt(2, price);
                     insert.setInt(3, categoryId);
                     insert.executeUpdate();
                }
            }
        }
    }

    public int[] getFensterPreise() {
         int[] prices = new int[FENSTER_DESCRIPTIONS.length];
         try (Connection con = DbConnector.getConnection()) {
            for (int i = 0; i < FENSTER_DESCRIPTIONS.length; i++) {
                 String desc = FENSTER_DESCRIPTIONS[i];
                 PreparedStatement ps = con.prepareStatement("SELECT Preis FROM Sonderwunsch WHERE Beschreibung = ?");
                 ps.setString(1, desc);
                 ResultSet rs = ps.executeQuery();
                 if (rs.next()) {
                     prices[i] = rs.getInt("Preis");
                 }
            }
         } catch (Exception e) { e.printStackTrace(); }
         return prices;
    }
    
    public void speichereFensterSonderwuensche(int[] selection) throws SQLException {
         if (this.kunde == null) return;
         int hausNr = this.kunde.getHausnummer();
         
         try (Connection con = DbConnector.getConnection()) {
            String deleteSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch IN (SELECT idSonderwunsch FROM Sonderwunsch WHERE Sonderwunschkategorie_idSonderwunschkategorie = ?)";
            try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                del.setInt(1, hausNr);
                del.setInt(2, 30); 
                del.executeUpdate();
            }
            
            String insertSql = "INSERT INTO Sonderwunsch_has_Haus (Sonderwunsch_idSonderwunsch, Haus_Hausnr) VALUES (?, ?)";
            try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                for (int i=0; i<selection.length; i++) {
                    if (selection[i] == 1) {
                         String desc = FENSTER_DESCRIPTIONS[i];
                         try (PreparedStatement find = con.prepareStatement("SELECT idSonderwunsch FROM Sonderwunsch WHERE Beschreibung = ?")) {
                              find.setString(1, desc);
                              ResultSet rs = find.executeQuery();
                              if (rs.next()) {
                                  int swId = rs.getInt(1);
                                  ins.setInt(1, swId);
                                  ins.setInt(2, hausNr);
                                  ins.executeUpdate();
                              }
                         }
                    }
                }
            }
         } catch (Exception e) { throw new SQLException(e); }
    }
    
    public int[] getFensterSelection() {
         int[] selection = new int[FENSTER_DESCRIPTIONS.length];
         if (this.kunde == null) return selection;
         int hausNr = this.kunde.getHausnummer();
         
         try (Connection con = DbConnector.getConnection()) {
             for (int i=0; i<FENSTER_DESCRIPTIONS.length; i++) {
                 String desc = FENSTER_DESCRIPTIONS[i];
                 String sql = "SELECT * FROM Sonderwunsch_has_Haus shh " +
                              "JOIN Sonderwunsch s ON shh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                              "WHERE shh.Haus_Hausnr = ? AND s.Beschreibung = ?";
                 try (PreparedStatement ps = con.prepareStatement(sql)) {
                     ps.setInt(1, hausNr);
                     ps.setString(2, desc);
                     ResultSet rs = ps.executeQuery();
                     if (rs.next()) selection[i] = 1; else selection[i] = 0;
                 }
             }
         } catch (Exception e) { e.printStackTrace(); }
         return selection;
    }

    // --- Methoden fuer Innentueren (analog zu Fenster) ---
    
    public int[] getInnentuerenPreise() {
         int[] prices = new int[INNENTUEREN_DESCRIPTIONS.length];
         try (Connection con = DbConnector.getConnection()) {
            for (int i = 0; i < INNENTUEREN_DESCRIPTIONS.length; i++) {
                 String desc = INNENTUEREN_DESCRIPTIONS[i];
                 PreparedStatement ps = con.prepareStatement("SELECT Preis FROM Sonderwunsch WHERE Beschreibung = ?");
                 ps.setString(1, desc);
                 ResultSet rs = ps.executeQuery();
                 if (rs.next()) prices[i] = rs.getInt("Preis");
            }
         } catch (Exception e) { e.printStackTrace(); }
         return prices;
    }
    
    public int[] getInnentuerenSelection() {
         int[] selection = new int[INNENTUEREN_DESCRIPTIONS.length];
         if (this.kunde == null) return selection;
         int hausNr = this.kunde.getHausnummer();
         try (Connection con = DbConnector.getConnection()) {
             for (int i=0; i<INNENTUEREN_DESCRIPTIONS.length; i++) {
                 String desc = INNENTUEREN_DESCRIPTIONS[i];
                 String sql = "SELECT * FROM Sonderwunsch_has_Haus shh " +
                              "JOIN Sonderwunsch s ON shh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                              "WHERE shh.Haus_Hausnr = ? AND s.Beschreibung = ?";
                 try (PreparedStatement ps = con.prepareStatement(sql)) {
                     ps.setInt(1, hausNr);
                     ps.setString(2, desc);
                     ResultSet rs = ps.executeQuery();
                     selection[i] = rs.next() ? 1 : 0;
                 }
             }
         } catch (Exception e) { e.printStackTrace(); }
         return selection;
    }
    
    public void speichereInnentuerenSonderwuensche(int[] selection) throws SQLException {
        if (this.kunde == null) return;
        int hausNr = this.kunde.getHausnummer();
        try (Connection con = DbConnector.getConnection()) {
           // Delete existing Category 40 entries
           String deleteSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch IN (SELECT idSonderwunsch FROM Sonderwunsch WHERE Sonderwunschkategorie_idSonderwunschkategorie = ?)";
           try (PreparedStatement del = con.prepareStatement(deleteSql)) {
               del.setInt(1, hausNr);
               del.setInt(2, 40); 
               del.executeUpdate();
           }
           
           String insertSql = "INSERT INTO Sonderwunsch_has_Haus (Sonderwunsch_idSonderwunsch, Haus_Hausnr) VALUES (?, ?)";
           try (PreparedStatement ins = con.prepareStatement(insertSql)) {
               for (int i=0; i<selection.length; i++) {
                   if (selection[i] == 1) {
                        String desc = INNENTUEREN_DESCRIPTIONS[i];
                        try (PreparedStatement find = con.prepareStatement("SELECT idSonderwunsch FROM Sonderwunsch WHERE Beschreibung = ?")) {
                             find.setString(1, desc);
                             ResultSet rs = find.executeQuery();
                             if (rs.next()) {
                                 int swId = rs.getInt(1);
                                 ins.setInt(1, swId);
                                 ins.setInt(2, hausNr);
                                 ins.executeUpdate();
                             }
                        }
                   }
               }
           }
        } catch (Exception e) { throw new SQLException(e); }
   }

    // --- Methoden fuer Heizungen (Category 50) ---

    public int[] getHeizungPreise() {
        int[] prices = new int[HEIZUNG_DESCRIPTIONS.length];
        try (Connection con = DbConnector.getConnection()) {
            for (int i = 0; i < HEIZUNG_DESCRIPTIONS.length; i++) {
                String desc = HEIZUNG_DESCRIPTIONS[i];
                PreparedStatement ps = con.prepareStatement("SELECT Preis FROM Sonderwunsch WHERE Beschreibung = ?");
                ps.setString(1, desc);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) prices[i] = rs.getInt("Preis");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return prices;
    }

    public int[] getHeizungSelection() {
        int[] selection = new int[HEIZUNG_DESCRIPTIONS.length];
        if (this.kunde == null) return selection;
        int hausNr = this.kunde.getHausnummer();
        try (Connection con = DbConnector.getConnection()) {
            for (int i=0; i<HEIZUNG_DESCRIPTIONS.length; i++) {
                String desc = HEIZUNG_DESCRIPTIONS[i];
                String sql = "SELECT * FROM Sonderwunsch_has_Haus shh " +
                             "JOIN Sonderwunsch s ON shh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                             "WHERE shh.Haus_Hausnr = ? AND s.Beschreibung = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, hausNr);
                    ps.setString(2, desc);
                    ResultSet rs = ps.executeQuery();
                    selection[i] = rs.next() ? 1 : 0;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return selection;
    }

    public void speichereHeizungSonderwuensche(int[] selection) throws SQLException {
        if (this.kunde == null) return;
        int hausNr = this.kunde.getHausnummer();
        try (Connection con = DbConnector.getConnection()) {
            // Delete existing Category 50 entries
            String deleteSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch IN (SELECT idSonderwunsch FROM Sonderwunsch WHERE Sonderwunschkategorie_idSonderwunschkategorie = ?)";
            try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                del.setInt(1, hausNr);
                del.setInt(2, 50);
                del.executeUpdate();
            }

            String insertSql = "INSERT INTO Sonderwunsch_has_Haus (Sonderwunsch_idSonderwunsch, Haus_Hausnr) VALUES (?, ?)";
            try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                for (int i=0; i<selection.length; i++) {
                    if (selection[i] == 1) {
                        String desc = HEIZUNG_DESCRIPTIONS[i];
                        try (PreparedStatement find = con.prepareStatement("SELECT idSonderwunsch FROM Sonderwunsch WHERE Beschreibung = ?")) {
                            find.setString(1, desc);
                            ResultSet rs = find.executeQuery();
                            if (rs.next()) {
                                int swId = rs.getInt(1);
                                ins.setInt(1, swId);
                                ins.setInt(2, hausNr);
                                ins.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { throw new SQLException(e); }
    }

    // --- Methoden fuer Sanitaer (Category 60) ---

    public int[] getSanitaerPreise() {
        int[] prices = new int[SANITAER_DESCRIPTIONS.length];
        try (Connection con = DbConnector.getConnection()) {
            for (int i = 0; i < SANITAER_DESCRIPTIONS.length; i++) {
                String desc = SANITAER_DESCRIPTIONS[i];
                PreparedStatement ps = con.prepareStatement("SELECT Preis FROM Sonderwunsch WHERE Beschreibung = ?");
                ps.setString(1, desc);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) prices[i] = rs.getInt("Preis");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return prices;
    }

    public int[] getSanitaerSelection() {
        int[] selection = new int[SANITAER_DESCRIPTIONS.length];
        if (this.kunde == null) return selection;
        int hausNr = this.kunde.getHausnummer();
        try (Connection con = DbConnector.getConnection()) {
            for (int i=0; i<SANITAER_DESCRIPTIONS.length; i++) {
                String desc = SANITAER_DESCRIPTIONS[i];
                String sql = "SELECT * FROM Sonderwunsch_has_Haus shh " +
                             "JOIN Sonderwunsch s ON shh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                             "WHERE shh.Haus_Hausnr = ? AND s.Beschreibung = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, hausNr);
                    ps.setString(2, desc);
                    ResultSet rs = ps.executeQuery();
                    selection[i] = rs.next() ? 1 : 0;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return selection;
    }

    public void speichereSanitaerSonderwuensche(int[] selection) throws SQLException {
        if (this.kunde == null) return;
        int hausNr = this.kunde.getHausnummer();
        try (Connection con = DbConnector.getConnection()) {
            // Delete existing Category 60 entries
            String deleteSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch IN (SELECT idSonderwunsch FROM Sonderwunsch WHERE Sonderwunschkategorie_idSonderwunschkategorie = ?)";
            try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                del.setInt(1, hausNr);
                del.setInt(2, 60);
                del.executeUpdate();
            }

            String insertSql = "INSERT INTO Sonderwunsch_has_Haus (Sonderwunsch_idSonderwunsch, Haus_Hausnr) VALUES (?, ?)";
            try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                for (int i=0; i<selection.length; i++) {
                    if (selection[i] == 1) {
                        String desc = SANITAER_DESCRIPTIONS[i];
                        try (PreparedStatement find = con.prepareStatement("SELECT idSonderwunsch FROM Sonderwunsch WHERE Beschreibung = ?")) {
                            find.setString(1, desc);
                            ResultSet rs = find.executeQuery();
                            if (rs.next()) {
                                int swId = rs.getInt(1);
                                ins.setInt(1, swId);
                                ins.setInt(2, hausNr);
                                ins.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { throw new SQLException(e); }
    }
   
   /**
    * Prueft, ob zu der aktuellen Hausnummer bereits Sonderwuensche der Kategorie "Grundriss-Varianten" (20) existieren.
    */
   public boolean hatGrundrissSonderwuensche() {
       if (this.kunde == null) return false;
       try (Connection con = DbConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM Sonderwunsch_has_Haus sh " + 
                "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 20")) {
            ps.setInt(1, this.kunde.getHausnummer());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
       } catch (Exception e) { e.printStackTrace(); }
       return false;
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

    /**
     * gibt den aktuellen Kunden zurueck.
     * @return Kunde, der aktuelle Kunde
     */
    public Kunde getKunde() {
        return this.kunde;
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
                if (!rs.next()) {
                    this.kunde = null;
                    return null;
                }
                Kunde k = new Kunde();
                k.setHausnummer(rs.getInt("Haus_Hausnr"));
                k.setVorname(rs.getString("Vorname"));
                k.setNachname(rs.getString("Nachname"));
                k.setTelefonnummer(rs.getString("Telefon"));
                k.setEmail(rs.getString("email"));
                
                this.kunde = k; // Update the model's current customer
                
                return k;
            }
        }
    }
}
