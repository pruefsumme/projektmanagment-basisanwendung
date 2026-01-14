package gui.aussenanlagen;

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

public class AussenanlagenControl {
    private AussenanlagenView view;
    private KundeModel kundeModel;
    
    public AussenanlagenControl(KundeModel kundeModel) {
        this.kundeModel = kundeModel;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.view = new AussenanlagenView(this, stage);
    }
    
    public void oeffneView() {
        if (!kundeModel.hatGrundrissSonderwuensche()) {
             zeigeMeldung("Hinweis", "Grundriss-Varianten fehlen. Die Sonderwünsche zu Grundriss-Varianten müssen zuerst ausgewählt/gespeichert werden.", AlertType.WARNING);
             return;
        }

        view.oeffneAussenanlagenView();
    }
    
    public void speichereSonderwuensche(int[] selection) {
        if (!pruefeKonstellationSonderwuensche(selection)) {
            return;
        }

        try {
            kundeModel.speichereAussenanlagenSonderwuensche(selection);
             zeigeMeldung("Erfolg", "Sonderwünsche wurden gespeichert.", AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            zeigeMeldung("Fehler", "Fehler beim Speichern: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public int[] lesePreise() {
        return kundeModel.getAussenanlagenPreise();
    }

    public int[] leseSelection() {
        return kundeModel.getAussenanlagenSelection();
    }
    
    public boolean pruefeKonstellationSonderwuensche(int[] selection) {
        // Indices:
        // 0: Abstellraum
        // 1: Vorb. Markise EG
        // 2: Vorb. Markise DG
        // 3: Markise EG
        // 4: Markise DG
        // 5: Antrieb Garage
        // 6: Sektionaltor

        // Rule 1: Cannot select Prep Markise EG (1) AND Markise EG (3)
        if (selection[1] == 1 && selection[3] == 1) {
            zeigeMeldung("Fehler", "Sie können nicht 'Vorbereitung Markise EG' und 'Elektrische Markise EG' gleichzeitig wählen.", AlertType.ERROR);
            return false;
        }
        
        // Rule 2: Cannot select Prep Markise DG (2) AND Markise DG (4)
        if (selection[2] == 1 && selection[4] == 1) {
             zeigeMeldung("Fehler", "Sie können nicht 'Vorbereitung Markise DG' und 'Elektrische Markise DG' gleichzeitig wählen.", AlertType.ERROR);
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
        
        // F92: Kundennummer_NachnameDesKunden_Aussenanlagen.csv
        String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Aussenanlagen.csv";
        
        try (Connection con = DbConnector.getConnection()) {
            // Category 90 = Aussenanlagen
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 90")) {
                 
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 
                 boolean found = false;
                 try (FileWriter writer = new FileWriter(filename)) {
                     while (rs.next()) {
                         writer.write(rs.getString("Beschreibung") + ";" + rs.getInt("Preis") + "\n");
                         found = true;
                     }
                 }
                 
                 if (found) {
                     zeigeMeldung("Erfolg", "CSV Export erfolgreich nach " + filename, AlertType.INFORMATION);
                 } else {
                     zeigeMeldung("Info", "Keine Sonderwünsche für Aussenanlagen vorhanden zum Exportieren.", AlertType.INFORMATION);
                 }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            zeigeMeldung("Fehler", "Fehler beim Export: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void zeigeMeldung(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
