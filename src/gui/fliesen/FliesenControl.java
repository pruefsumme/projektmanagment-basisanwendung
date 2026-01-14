package gui.fliesen;

import business.kunde.KundeModel;
import business.kunde.Kunde;
import data.DbConnector;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;

public class FliesenControl {
    private FliesenView view;
    private KundeModel kundeModel;
    
    public FliesenControl(KundeModel kundeModel) {
        this.kundeModel = kundeModel;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.view = new FliesenView(this, stage);
    }
    
    public void oeffneView() {
        if (!kundeModel.hatGrundrissSonderwuensche()) {
             zeigeMeldung("Hinweis", "Grundriss-Varianten fehlen. Die Sonderwünsche zu Grundriss-Varianten müssen zuerst ausgewählt/gespeichert werden.", AlertType.WARNING);
             return;
        }

        view.oeffneFliesenView();
    }
    
    public void speichereSonderwuensche(int[] selection) {
        try {
            kundeModel.speichereFliesenSonderwuensche(selection);
             zeigeMeldung("Erfolg", "Sonderwünsche wurden gespeichert.", AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            zeigeMeldung("Fehler", "Fehler beim Speichern: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public int[] lesePreise() {
        return kundeModel.getFliesenPreise();
    }

    public int[] leseSelection() {
        return kundeModel.getFliesenSelection();
    }
    
    public boolean pruefeKonstellationSonderwuensche(int[] selection) {
        // 7.1 (No tiles Kitchen EG) vs 7.3 (Large tiles Kitchen EG)
        // 0 vs 2
        if (selection[0] == 1 && selection[2] == 1) {
            zeigeMeldung("Fehler", "Sie können nicht 'Keine Fliesen im Küchenbereich' und 'Großformatige Fliesen im Küchenbereich' gleichzeitig wählen.", AlertType.ERROR);
            return false;
        }
        
        // 7.2 (No tiles Bath OG) vs 7.4 (Large tiles Bath OG)
        // 1 vs 3
        if (selection[1] == 1 && selection[3] == 1) {
             zeigeMeldung("Fehler", "Sie können nicht 'Keine Fliesen im Bad des OG' und 'Großformatige Fliesen im Bad des OG' gleichzeitig wählen.", AlertType.ERROR);
             return false;
        }
        
        // 7.6 (Large tiles Bath DG) requires 7.5 (Tiles Bath DG)
        // 4 (Tiles DG), 5 (Large Tiles DG)
        if (selection[5] == 1 && selection[4] == 0) {
             zeigeMeldung("Fehler", "Um 'Großformatige Fliesen im Bad des DG' zu wählen, müssen Sie erst 'Fliesen im Bad des DG' auswählen.", AlertType.ERROR);
             return false;
        }
        
        return true;
    }
    
    public void exportiereCsv() {
        Kunde kunde = kundeModel.getKunde();
        if (kunde == null) {
            zeigeMeldung("Kein Kunde", "Es wurde kein Kunde ausgewaehlt.", AlertType.ERROR);
            return;
        }
        
        // F72: Kundennummer_NachnameDesKunden_Fliesen.csv
        String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Fliesen.csv";
        
        try (Connection con = DbConnector.getConnection()) {
            // Category 70 = Fliesen
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 70")) {
                 
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 
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
                     zeigeMeldung("Info", "Der Kunde hat keine Sonderwünsche in dieser Kategorie gespeichert.", AlertType.INFORMATION);
                 }
            }
        } catch (SQLException | IOException e) {
             zeigeMeldung("Fehler", "Export fehlgeschlagen: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void zeigeMeldung(String titel, String nachricht, AlertType typ) {
        Alert alert = new Alert(typ);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(nachricht);
        alert.show(); // Modified to show() instead of showAndWait() to match other controllers if needed, but showAndWait is safer for modal flows. Standard seems to be show() in KundeControl but showAndWait for results.
    }
}
