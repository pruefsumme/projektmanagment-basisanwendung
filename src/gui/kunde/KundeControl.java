package gui.kunde;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileWriter;
import java.io.IOException;

import data.DbConnector;
import business.kunde.Kunde;
import business.kunde.KundeModel;
import gui.grundriss.GrundrissControl;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Klasse, welche das Grundfenster mit den Kundendaten kontrolliert.
 */
public class KundeControl {

    // das View-Objekt des Grundfensters mit den Kundendaten
    private KundeView kundeView;
    // das Model-Objekt des Grundfensters mit den Kundendaten
    private KundeModel kundeModel;
    /* das GrundrissControl-Objekt fuer die Sonderwuensche
       zum Grundriss zu dem Kunden */
    private GrundrissControl grundrissControl;
    /* das FensterControl-Objekt fuer die Sonderwuensche
       zu Fenster und Aussentueren */
    private gui.fenster.FensterControl fensterControl;

    /**
     * erzeugt ein ControlObjekt inklusive View-Objekt und Model-Objekt zum
     * Grundfenster mit den Kundendaten.
     * @param primaryStage, Stage fuer das View-Objekt zu dem Grundfenster mit den Kundendaten
     */
    public KundeControl(Stage primaryStage) {
        this.kundeModel = KundeModel.getInstance();
        this.kundeView = new KundeView(this, primaryStage, kundeModel);
    }

    /*
     * erstellt, falls nicht vorhanden, ein Grundriss-Control-Objekt.
     * Das GrundrissView wird sichtbar gemacht.
     */
    public void oeffneGrundrissControl(){
        if (this.grundrissControl == null){
            this.grundrissControl = new GrundrissControl(kundeModel);
        }
        this.grundrissControl.oeffneGrundrissView();
    }

    /*
     * erstellt, falls nicht vorhanden, ein Fenster-Control-Objekt.
     * Das FensterView wird sichtbar gemacht.
     */
    public void oeffneFensterControl(){
        if (this.fensterControl == null){
            this.fensterControl = new gui.fenster.FensterControl(kundeModel);
        }
        this.fensterControl.oeffneFensterView();
    }


    /**
     * speichert ein Kunde-Objekt in die Datenbank
     * @param kunde, Kunde-Objekt, welches zu speichern ist
     */
    public void speichereKunden(Kunde kunde){
        try{
            kundeModel.speichereKunden(kunde);
        }
        catch(SQLException exc){
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "SQLException",
                    "Fehler beim Speichern in die Datenbank");
        }
        catch(Exception exc){
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung("Exception",
                    "Unbekannter Fehler");
        }
    }

    /**
     * aktualisiert einen vorhandenen Kunden in der Datenbank.
     * Änderungen werden sofort persistiert.
     *
     * @param kunde Kunde-Objekt mit den geänderten Daten
     */
    public void aktualisiereKunden(Kunde kunde) {
        try {
            kundeModel.aktualisiereKunden(kunde);
        } catch (SQLException exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "SQLException",
                    "Fehler beim Aktualisieren in der Datenbank");
        } catch (Exception exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "Exception",
                    "Unbekannter Fehler beim Aktualisieren");
        }
    }

    /**
     * löscht alle Kunden zu einer Hausnummer aus der Datenbank.
     *
     * @param hausnr Hausnummer, zu der die Kunden gelöscht werden sollen
     */
    public void loescheKundenZuHaus(int hausnr) {
        try {
            kundeModel.loescheKundenZuHaus(hausnr);
        } catch (SQLException exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "SQLException",
                    "Fehler beim Löschen in der Datenbank");
        } catch (Exception exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "Exception",
                    "Unbekannter Fehler beim Löschen");
        }
    }

    /**
     * lädt den zuletzt gespeicherten Kunden zu einer Hausnummer aus der Datenbank.
     *
     * @param hausnr Hausnummer
     * @return Kunde oder null, falls zu der Hausnummer kein Kunde gefunden wurde
     */
    public Kunde ladeLetztenKundenZuHaus(int hausnr) {
        try {
            return kundeModel.ladeLetztenKundenZuHaus(hausnr);
        } catch (SQLException exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "SQLException",
                    "Fehler beim Lesen aus der Datenbank");
        } catch (Exception exc) {
            exc.printStackTrace();
            this.kundeView.zeigeFehlermeldung(
                    "Exception",
                    "Unbekannter Fehler beim Lesen");
        }
        return null;
    }

    public void exportiereSonderwuenscheCsv() {
        Kunde kunde = kundeModel.getKunde();
        if (kunde == null) {
            this.kundeView.zeigeFehlermeldung("Kein Kunde", "Es wurde kein Kunde ausgewaehlt.");
            return;
        }

        try (Connection con = DbConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT s.Beschreibung, s.Preis FROM Sonderwunsch_has_Haus sh " +
                 "JOIN Sonderwunsch s ON sh.Sonderwunsch_idSonderwunsch = s.idSonderwunsch " +
                 "WHERE sh.Haus_Hausnr = ?")) {
             ps.setInt(1, kunde.getHausnummer());
             ResultSet rs = ps.executeQuery();
             
             String filename = kunde.getHausnummer() + "_" + kunde.getNachname() + ".csv";
             try (FileWriter writer = new FileWriter(filename)) {
                 boolean found = false;
                 while(rs.next()){
                     String beschreibung = rs.getString("Beschreibung");
                     int preis = rs.getInt("Preis");
                     writer.write(beschreibung + ";" + preis + "\n");
                     found = true;
                 }
                 
                 if (found) {
                     Alert alert = new Alert(AlertType.INFORMATION);
                     alert.setTitle("Erfolg");
                     alert.setContentText("Sonderwuensche exportiert nach " + filename);
                     alert.showAndWait();
                 } else {
                     Alert alert = new Alert(AlertType.INFORMATION);
                     alert.setTitle("Info");
                     alert.setContentText("Kunde hat keine Sonderwuensche.");
                     alert.showAndWait();
                 }
             } catch (IOException e) {
                 this.kundeView.zeigeFehlermeldung("IO Fehler", "Fehler beim Schreiben der CSV: " + e.getMessage());
             }

        } catch (SQLException e) {
             this.kundeView.zeigeFehlermeldung("DB Fehler", "Fehler beim Lesen der Sonderwuensche: " + e.getMessage());
        }
    }
}
