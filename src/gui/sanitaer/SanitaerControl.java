package gui.sanitaer;

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

public class SanitaerControl {

    private SanitaerView view;
    private KundeModel kundeModel;

    public SanitaerControl(KundeModel kundeModel) {
        this.kundeModel = kundeModel;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.view = new SanitaerView(this, stage);
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

        int[] prices = kundeModel.getSanitaerPreise();
        view.setPreise(prices);

        int[] selection = kundeModel.getSanitaerSelection();
        view.setSelection(selection);

        view.oeffneView();
    }
    
    public void speichereSonderwuensche(int[] selection) {
        try {
            kundeModel.speichereSanitaerSonderwuensche(selection);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setContentText("Fehler beim Speichern: " + e.getMessage());
            alert.show();
        }
    }
    
    public boolean pruefeKonstellationSonderwuensche(int[] selection) {
        // F61: Pruefung der Sonderwuensche
        return true;
    }
    
    public void exportiereCsv() {
        Kunde kunde = kundeModel.getKunde();
        if (kunde == null) {
            zeigeMeldung("Kein Kunde", "Es wurde kein Kunde ausgewaehlt.", AlertType.ERROR);
            return;
        }

        try (Connection con = DbConnector.getConnection()) {
            // Category 60 = Sanitaer
            try (PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ? AND s.Sonderwunschkategorie_idSonderwunschkategorie = 60")) {
                 
                 ps.setInt(1, kunde.getHausnummer());
                 ResultSet rs = ps.executeQuery();
                 
                 String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + "_Sanitaer.csv";
                 
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
