package gui.fenster;

import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import data.DbConnector;
import business.kunde.Kunde;
import business.kunde.KundeModel;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class FensterControl {
    
    private FensterView fensterView;
    private KundeModel kundeModel;

    public FensterControl(KundeModel kundeModel){  
        this.kundeModel = kundeModel;
        Stage stageFenster = new Stage();
        stageFenster.initModality(Modality.APPLICATION_MODAL);
        this.fensterView = new FensterView(this, stageFenster);
    }
        
    public void oeffneFensterView(){ 
        // Lese Daten aus Model
        int[] preise = kundeModel.getFensterPreise();
        this.fensterView.setPreise(preise);
        
        int[] selection = kundeModel.getFensterSelection();
        this.fensterView.setAusgewaehlteSonderwuensche(selection);
        
        this.fensterView.oeffneFensterView();
    }

    public void speichereSonderwuensche(int[] selection){
        try {
            this.kundeModel.speichereFensterSonderwuensche(selection);
        } catch (SQLException e) {
            e.printStackTrace();
            // In a real app, show error in View
        }
    } 
    
    public boolean pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw){
        // Prüfung der Konstellation der Sonderwünsche
        // Regel: Vorbereitung und elektrische Rolläden können nicht gleichzeitig gewählt werden
        // Index 3: Vorbereitung EG, Index 6: Elektrisch EG
        if (ausgewaehlteSw[3] == 1 && ausgewaehlteSw[6] == 1) return false;
        // Index 4: Vorbereitung OG, Index 7: Elektrisch OG
        if (ausgewaehlteSw[4] == 1 && ausgewaehlteSw[7] == 1) return false;
        // Index 5: Vorbereitung DG, Index 8: Elektrisch DG
        if (ausgewaehlteSw[5] == 1 && ausgewaehlteSw[8] == 1) return false;
        
        return true;
    }

    public void exportiereCsv() {
        Kunde kunde = kundeModel.getKunde();
        if (kunde == null) {
            zeigeMeldung("Kein Kunde", "Es wurde kein Kunde ausgewaehlt.", AlertType.ERROR);
            return;
        }

        try (Connection con = DbConnector.getConnection()) {
            // 2. Suche Sonderwünsche (Kat. 30 'Fenster und Türen')
            // Wir koennen auch die internen Arrays nutzen, aber DB ist sicherer für 'gespeicherte' Daten.
            // Requirement sagt "Die von einem Kunden ausgewählten Sonderwünsche ... exportieren". 
            // Also das was in DB steht.
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 30")) {
                 
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 
                 // Filename: Kundennummer_NachnameDesKunden_FensterUndAussentueren.csv
                 // Anmerkung: Kunde meint mit "Kundennummer" im Kontext der existierenden Dateien ("6_Jongel...") 
                 // offenbar die Hausnummer, nicht die Datenbank-ID (idKunde).
                 String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_FensterUndAussentueren.csv";
                 
                 boolean found = false;
                 try (FileWriter writer = new FileWriter(filename)) {
                     while(rs.next()){
                         String beschreibung = rs.getString("Beschreibung");
                         int preis = rs.getInt("Preis");
                         writer.write(beschreibung + ";" + preis + "\n");
                         found = true;
                     }
                 }
                 
                 if (found) {
                     zeigeMeldung("Export erfolgreich", "Datei wurde erstellt: " + filename, AlertType.INFORMATION);
                 } else {
                     zeigeMeldung("Keine Daten", "Der Kunde hat keine Sonderwünsche in dieser Kategorie gespeichert.", AlertType.INFORMATION);
                 }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            zeigeMeldung("Fehler", "Fehler beim Exportieren: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void zeigeMeldung(String titel, String text, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
