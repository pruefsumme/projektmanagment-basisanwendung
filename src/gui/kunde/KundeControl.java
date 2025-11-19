package gui.kunde;

import java.sql.SQLException;

import business.kunde.Kunde;
import business.kunde.KundeModel;
import gui.grundriss.GrundrissControl;
import javafx.stage.Stage;

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
}
