package gui.parkett;

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

public class ParkettControl {
    private ParkettView view;
    private KundeModel kundeModel;
    
    public ParkettControl(KundeModel kundeModel) {
        this.kundeModel = kundeModel;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.view = new ParkettView(this, stage);
    }
    
    public void oeffneView() {
        if (!kundeModel.hatFliesenSonderwuensche() || !kundeModel.hatHeizungSonderwuensche()) {
             zeigeMeldung("Hinweis", "Die Sonderwünsche zu Fliesen und zu Heizungen müssen bereits vorhanden sein.", AlertType.WARNING);
             return;
        }

        view.oeffneParkettView();
    }
    
    public void speichereSonderwuensche(int[] selection) {
        try {
            kundeModel.speichereParkettSonderwuensche(selection);
             zeigeMeldung("Erfolg", "Sonderwünsche wurden gespeichert.", AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            zeigeMeldung("Fehler", "Fehler beim Speichern: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public int[] lesePreise() {
        return kundeModel.getParkettPreise();
    }

    public int[] leseSelection() {
        return kundeModel.getParkettSelection();
    }
    
    public boolean pruefeKonstellationSonderwuensche(int[] selection) {
        // IDs mapping:
        // 0: 8.1 Landhaus Ess EG
        // 1: 8.2 Landhaus Küche EG
        // 2: 8.3 Stäbchen Ess EG
        // 3: 8.4 Stäbchen Küche EG
        // 4: 8.5 Landhaus OG
        // 5: 8.6 Stäbchen OG
        // 6: 8.7 Landhaus DG
        // 7: 8.8 Landhaus DG ohne Bad
        // 8: 8.9 Stäbchen DG
        // 9: 8.10 Stäbchen DG ohne Bad
        
        // Validation F81
        
        // Essbereich EG: 8.1 vs 8.3
        if (selection[0] == 1 && selection[2] == 1) {
            zeigeMeldung("Fehler", "Essbereich EG: Bitte wählen Sie entweder Landhausdielen oder Stäbchenparkett, nicht beides.", AlertType.ERROR);
            return false;
        }
        
        // Kuechenbereich EG: 8.2 vs 8.4
        if (selection[1] == 1 && selection[3] == 1) {
            zeigeMeldung("Fehler", "Küchenbereich EG: Bitte wählen Sie entweder Landhausdielen oder Stäbchenparkett, nicht beides.", AlertType.ERROR);
            return false;
        }

        // OG: 8.5 vs 8.6
        if (selection[4] == 1 && selection[5] == 1) {
            zeigeMeldung("Fehler", "OG: Bitte wählen Sie entweder Landhausdielen oder Stäbchenparkett, nicht beides.", AlertType.ERROR);
            return false;
        }

        // DG: All mutually exclusive
        int dgSelectedCount = 0;
        if (selection[6] == 1) dgSelectedCount++;
        if (selection[7] == 1) dgSelectedCount++;
        if (selection[8] == 1) dgSelectedCount++;
        if (selection[9] == 1) dgSelectedCount++;
        
        if (dgSelectedCount > 1) {
             zeigeMeldung("Fehler", "DG: Bitte wählen Sie nur eine der Parkett-Varianten für das Dachgeschoss.", AlertType.ERROR);
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
        
        // F82: Kundennummer_NachnameDesKunden_Parkett.csv
        String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Parkett.csv";
        
        try (Connection con = DbConnector.getConnection()) {
            // Category 80 = Parkett
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 80")) {
                 
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
        alert.show(); 
    }
}
