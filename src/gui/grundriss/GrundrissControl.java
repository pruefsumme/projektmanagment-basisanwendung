package gui.grundriss;

import business.kunde.Kunde;
import business.kunde.KundeModel;
import data.DbConnector;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Klasse, welche das Fenster mit den Sonderwuenschen zu den Grundriss-Varianten
 * kontrolliert.
 */
public final class GrundrissControl {
	
	// das View-Objekt des Grundriss-Fensters
	private GrundrissView grundrissView;
	private KundeModel kundeModel;
    
    // Default-Preise und Beschreibungen
    private int[] preise = new int[]{300, 300, 0, 890, 990, 8990};
    private final String[] beschreibungen = new String[]{
        "Wand zur Abtrennung der Kueche von dem Essb.",
        "Tuer in der Wand zw. Kueche und Essbereich",
        "Grosses Zimmer im OG statt zwei kleinen",
        "Abgetrennter Treppenraum im DG",
        "Vorrichtung eines Bades im DG",
        "Ausfuehrung eines Bades im DG"
    };

	/**
	 * erzeugt ein ControlObjekt inklusive View-Objekt und Model-Objekt zum 
	 * Fenster fuer die Sonderwuensche zum Grundriss.
	 * @param kundeModel, Model-Objekt mit den Kundendaten
	 */
	public GrundrissControl(KundeModel kundeModel){  
		this.kundeModel = kundeModel;
	   	Stage stageGrundriss = new Stage();
    	stageGrundriss.initModality(Modality.APPLICATION_MODAL);
    	this.grundrissView = new GrundrissView(this, stageGrundriss);
        this.leseGrundrissSonderwuensche();
	}
	    
	/**
	 * macht das GrundrissView-Objekt sichtbar.
	 */
	public void oeffneGrundrissView(){ 
		this.grundrissView.oeffneGrundrissView();
	}

    private int[] sonderwunschIds = new int[6]; // Speichert die DB-IDs der 6 Sonderwuensche

	public void leseGrundrissSonderwuensche(){
        // Versuch Preise aus DB zu lesen
        // Wir suchen Sonderwuensche der Kategorie 'Grundriss-Varianten' (ID 20 laut Dump, aber wir suchen besser per Name)
        try (Connection con = DbConnector.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT s.idSonderwunsch, s.Beschreibung, s.Preis " + 
                 "FROM Sonderwunsch s " +
                 "JOIN Sonderwunschkategorie k ON s.Sonderwunschkategorie_idSonderwunschkategorie = k.idSonderwunschkategorie " + 
                 "WHERE k.Kategorie = 'Grundriss-Varianten'")) {
             
             while(rs.next()){
                 String beschreibung = rs.getString("Beschreibung");
                 int preis = rs.getInt("Preis");
                 int id = rs.getInt("idSonderwunsch");

                 // Zuordnung zu internen Arrays via Beschreibung
                 for(int i=0; i<beschreibungen.length; i++) {
                     if(beschreibungen[i].equals(beschreibung)) {
                         preise[i] = preis;
                         sonderwunschIds[i] = id;
                     }
                 }
             }
        } catch (SQLException e) {
            System.err.println("Datenbankzugriff Fehler (Preise): " + e.getMessage());
        }
        this.grundrissView.setPreise(preise);
        
        // Versuch gespeicherte wünsche des Kunden zu lesen
        Kunde kunde = kundeModel.getKunde();
        if(kunde != null) {
            int[] selection = new int[6];
            try (Connection con = DbConnector.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT Sonderwunsch_idSonderwunsch FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ?")) {
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 while(rs.next()){
                     int dbId = rs.getInt("Sonderwunsch_idSonderwunsch");
                     // Finde den internen Index zur DB ID
                     for(int i=0; i<sonderwunschIds.length; i++) {
                         if (sonderwunschIds[i] == dbId && dbId != 0) {
                             selection[i] = 1;
                         }
                     }
                 }
            } catch (SQLException e) {
                 System.err.println("Datenbankzugriff Fehler (KundeSonderwunsch): " + e.getMessage());
            }
            this.grundrissView.setAusgewaehlteSonderwuensche(selection);
        }
    } 
	
	public boolean pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw){
        // /F21/ Pruefung
        // Regel: Tuer (Index 1) benoetigt Wand (Index 0)
        if (ausgewaehlteSw[1] == 1 && ausgewaehlteSw[0] == 0) {
        	zeigeFehlermeldung("Ungueltige Kombination", 
        			"Die Tuer in der Wand kann nur eingebaut werden, wenn die Wand auch existiert.");
            return false;
        }
		return true;
	}
	
	public void speichereSonderwuensche(int[] ausgewaehlteSw) {
		Kunde kunde = kundeModel.getKunde();
		if (kunde == null) {
			zeigeFehlermeldung("Fehler", "Kein Kunde vorhanden.");
			return;
		}

		// /F20/ Speichern in DB
		try (Connection con = DbConnector.getConnection()) {
            // Wir müssen sicherstellen, dass wir gültige IDs für die Sonderwünsche haben.
            // Falls sonderwunschIds noch 0 sind (weil Tabelle leer war), müssen wir sie evtl anlegen
            // oder - einfache Lösung - wir nehmen an sie existieren.
            // Fallback: Wenn IDs 0, können wir nicht speichern.
            
            // Prüfen ob IDs da sind, wenn nicht, können wir nicht in relationstabelle speichern
            boolean idsVorhanden = false;
            for(int id : sonderwunschIds) {
                if(id != 0) idsVorhanden = true;
            }

            if (!idsVorhanden) {
                 // DB Sonderwünsche fehlen -> Versuche sie anzulegen (Optional, aber sinnvoll damit es funzt)
                 // Hole ID der Kategorie
                 int katId = 0;
                 try(Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT idSonderwunschkategorie FROM Sonderwunschkategorie WHERE Kategorie = 'Grundriss-Varianten'")) {
                     if(rs.next()) katId = rs.getInt(1);
                 }
                 
                 if (katId != 0) {
                     String insertSw = "INSERT INTO Sonderwunsch (Beschreibung, Preis, Sonderwunschkategorie_idSonderwunschkategorie) VALUES (?, ?, ?)";
                     try(PreparedStatement psSw = con.prepareStatement(insertSw, Statement.RETURN_GENERATED_KEYS)) {
                         for(int i=0; i<beschreibungen.length; i++) {
                             if(sonderwunschIds[i] == 0) {
                                 psSw.setString(1, beschreibungen[i]);
                                 psSw.setInt(2, preise[i]);
                                 psSw.setInt(3, katId);
                                 psSw.executeUpdate();
                                 try(ResultSet gk = psSw.getGeneratedKeys()){
                                     if(gk.next()) sonderwunschIds[i] = gk.getInt(1);
                                 }
                             }
                         }
                     }
                 }
            }


			// Loeschen alter Eintraege fuer diesen Kunden und diese Sonderwuensche
            // Wir löschen nur Einträge, die zu 'Grundriss-Varianten' gehören.
            String delSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch IN (SELECT idSonderwunsch FROM Sonderwunsch WHERE Sonderwunschkategorie_idSonderwunschkategorie = (SELECT idSonderwunschkategorie FROM Sonderwunschkategorie WHERE Kategorie = 'Grundriss-Varianten'))";
             // Vereinfacht: Wir löschen Einträge die unseren bekannten IDs entsprechen
            if (sonderwunschIds[0] != 0) { // Zumindest IDs sollten jetzt da sein
                String delIdSql = "DELETE FROM Sonderwunsch_has_Haus WHERE Haus_Hausnr = ? AND Sonderwunsch_idSonderwunsch = ?";
                 try(PreparedStatement psDel = con.prepareStatement(delIdSql)){
                    for(int id : sonderwunschIds) {
                        if (id != 0) {
                            psDel.setInt(1, kunde.getHausnummer());
                            psDel.setInt(2, id);
                            psDel.executeUpdate();
                        }
                    }
                }
            }


            // Schreiben neuer Eintraege
            String insSql = "INSERT INTO Sonderwunsch_has_Haus (Haus_Hausnr, Sonderwunsch_idSonderwunsch) VALUES (?, ?)";
            try(PreparedStatement psIns = con.prepareStatement(insSql)){
                for(int i=0; i<ausgewaehlteSw.length; i++) {
                    if(ausgewaehlteSw[i] == 1 && sonderwunschIds[i] != 0) {
                        psIns.setInt(1, kunde.getHausnummer());
                        psIns.setInt(2, sonderwunschIds[i]); 
                        psIns.executeUpdate();
                    }
                }
            }
		} catch (SQLException e) {
			zeigeFehlermeldung("Datenbankfehler", "Fehler beim Speichern der Sonderwuensche: " + e.getMessage());
		}

		// /F22/ Export als CSV
		// Kundennummer_NachnameDesKunden_Grundriss.csv (Hier Hausnummer als ID)
		String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Grundriss.csv";
		try (FileWriter writer = new FileWriter(filename)) {
			for(int i=0; i<ausgewaehlteSw.length; i++) {
				if(ausgewaehlteSw[i] == 1) {
					writer.write(beschreibungen[i] + ";" + preise[i] + "\n");
				}
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Gespeichert");
			alert.setContentText("Sonderwuensche wurden gespeichert und exportiert nach:\n" + filename);
			alert.showAndWait();
		} catch (IOException e) {
			zeigeFehlermeldung("Exportfehler", "Fehler beim CSV Export: " + e.getMessage());
		}
	}
	
	private void zeigeFehlermeldung(String titel, String nachricht) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titel);
		alert.setContentText(nachricht);
		alert.showAndWait();
	}
}
