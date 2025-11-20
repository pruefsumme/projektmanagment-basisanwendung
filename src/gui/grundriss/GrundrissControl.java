// filepath: d:\Users\GZ\Desktop\Projektmanagement\projektmanagment-basisanwendung\src\gui\grundriss\GrundrissControl.java
package gui.grundriss;

import business.grundriss.Sonderwunsch;
import javafx.stage.Stage;
import java.util.List;

/**
 * Control-Klasse für die Grundriss-Varianten
 */
public class GrundrissControl {
    
    private GrundrissModel grundrissModel;
    private GrundrissView grundrissView;
    
    /**
     * Konstruktor für GrundrissControl ohne Parameter
     */
    public GrundrissControl() {
        this.grundrissModel = new GrundrissModel();
    }
    
    // NEW: setzt die aktuelle Hausnummer für Speicherung/Laden
    /**
     * setzt die aktuelle Hausnummer
     * @param hausnummer Hausnummer des Hauses
     */
    public void setAktuelleHausnummer(int hausnummer) {
        this.grundrissModel.setAktuelleHausnummer(hausnummer);
    }
    
    // NEW: liest Grundriss-Sonderwuensche (Weiterleitung an Model)
    /**
     * liest die Grundriss-Sonderwuensche aus der Datenbank
     */
    public void leseGrundrissSonderwuensche() {
        this.grundrissModel.leseGrundrissSonderwuensche();
    }
    
    /**
     * Öffnet die Grundriss-View
     */
    public void oeffneGrundrissView() {
        // NEW: Daten laden bevor UI aufgebaut wird
        this.grundrissModel.leseGrundrissSonderwuensche();
        Stage grundrissStage = new Stage();
        this.grundrissView = new GrundrissView(this, grundrissStage);
        this.grundrissView.oeffneGrundrissView();
    }
    
    /**
     * gibt die Liste der Sonderwuensche zurueck
     * @return Liste der Sonderwuensche
     */
    public List<Sonderwunsch> getSonderwuensche() {
        return this.grundrissModel.getSonderwuensche();
    }
    
    /**
     * prueft ob ein Sonderwunsch ausgewaehlt ist
     * @param sonderwunschId ID des Sonderwunsches
     * @return true wenn ausgewaehlt, false sonst
     */
    public boolean istSonderwunschAusgewaehlt(int sonderwunschId) {
        return this.grundrissModel.istSonderwunschAusgewaehlt(sonderwunschId);
    }
    
    /**
     * prueft die Konstellation der ausgewaehlten Sonderwuensche
     * @param ausgewaehlteSw Array der ausgewaehlten Sonderwunsch-IDs
     * @return true wenn Konstellation gueltig, false sonst
     */
    public boolean pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw) {
        return this.grundrissModel.pruefeKonstellationSonderwuensche(ausgewaehlteSw);
    }
    
    /**
     * berechnet den Gesamtpreis der ausgewaehlten Sonderwuensche
     * @param ausgewaehlteSw Array der ausgewaehlten Sonderwunsch-IDs
     * @return Gesamtpreis
     */
    public double berechnePreisSonderwuensche(int[] ausgewaehlteSw) {
        return this.grundrissModel.berechnePreisSonderwuensche(ausgewaehlteSw);
    }
    
    /**
     * speichert die ausgewaehlten Sonderwuensche in der Datenbank
     * @param ausgewaehlteSw Array der ausgewaehlten Sonderwunsch-IDs
     */
    public void speichereSonderwuensche(int[] ausgewaehlteSw) {
        this.grundrissModel.speichereSonderwuensche(ausgewaehlteSw);
    }
}