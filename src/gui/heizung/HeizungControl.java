package gui.heizung;

import business.kunde.KundeModel;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import data.DbConnector;
import business.kunde.Kunde;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HeizungControl {
    
    private HeizungView view;
    private KundeModel kundeModel;

    public HeizungControl(KundeModel kundeModel) {
        this.kundeModel = kundeModel;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.view = new HeizungView(this, stage);
    }

    public void oeffneView() {
        if (!kundeModel.hatGrundrissSonderwuensche()) {
             Alert alert = new Alert(AlertType.WARNING);
             alert.setTitle("Hinweis");
             alert.setHeaderText("Grundriss-Varianten fehlen");
             alert.setContentText("Die Sonderwünsche zu Grundriss-Varianten müssen zuerst ausgewählt/gespeichert werden.");
             alert.showAndWait();
             return;
        }

        int[] prices = kundeModel.getHeizungPreise();
        view.setPreise(prices);

        int[] selection = kundeModel.getHeizungSelection();
        view.setSelection(selection);

        view.oeffneView();
    }

    public void speichereSonderwuensche(int[] selection) {
        try {
            kundeModel.speichereHeizungSonderwuensche(selection);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setContentText("Fehler beim Speichern: " + e.getMessage());
            alert.show();
        }
    }

    public boolean pruefeKonstellationSonderwuensche(int[] selection) {
        // Validation Logic for F51
        // 5.4 (Index 3) and 5.5 (Index 4) are mutually exclusive
        if (selection[3] == 1 && selection[4] == 1) {
            return false;
        }
        
        // Assumption: If Floor heating (3 or 4) is selected, radiators (0, 1) might be invalid except maybe towel radiator (2)
        // However, without explicit business rules from user about this specific exclusion, I will stick to the obvious one:
        // You can't have "FBH without DG" AND "FBH with DG" at the same time.
        
        return true;
    }

    public void exportiereCsv() {
        Kunde kunde = kundeModel.getKunde();
        if (kunde == null) {
            zeigeMeldung("Kein Kunde", "Es wurde kein Kunde ausgewaehlt.", AlertType.ERROR);
            return;
        }

        try (Connection con = DbConnector.getConnection()) {
            // Category 50 = Heizungen
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 50")) {
                 
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 
                 String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Heizungen.csv";
                 
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
