package gui.grundriss;

import business.grundriss.GrundrissModel;
import business.grundriss.Sonderwunsch;
import business.kunde.KundeModel;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

/**
 * Klasse, welche das Fenster mit den Sonderwuenschen zu den Grundriss-Varianten
 * kontrolliert.
 */
public final class GrundrissControl {
    
    // das View-Objekt des Grundriss-Fensters
    private GrundrissView grundrissView;
    // Model für die Grundriss-Sonderwünsche (in-memory; später ersetzen durch DB)
    private GrundrissModel grundrissModel;
    // Referenz auf KundeModel (später für kundenspezifisches Laden/Speichern verwenden)
    private KundeModel kundeModel;

    /**
     * erzeugt ein ControlObjekt inklusive View-Objekt und Model-Objekt zum 
     * Fenster fuer die Sonderwuensche zum Grundriss.
     * @param kundeModel KundeModel, Referenz auf aktuelles Kundenmodell
     */
    public GrundrissControl(KundeModel kundeModel){  
        this.kundeModel = kundeModel;
        this.grundrissModel = new GrundrissModel(); // später durch Laden aus DB ersetzen
       	Stage stageGrundriss = new Stage();
        stageGrundriss.initModality(Modality.APPLICATION_MODAL);
        this.grundrissView = new GrundrissView(this, stageGrundriss);
    }
        
    /**
     * macht das GrundrissView-Objekt sichtbar.
     */
    public void oeffneGrundrissView(){ 
        this.grundrissView.oeffneGrundrissView();
    }

    public void leseGrundrissSonderwuensche(){
        // später: DB-Ladevorgang (SELECT auf Sonderwunsch + kundenspezifische Auswahl)
    } 
    
    public boolean pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw){
        // später: Validierungsregeln (Abhängigkeiten) einbauen
        return true;
    }

    /**
     * gibt alle Sonderwünsche für das View heraus
     * @return List<Sonderwunsch>, Liste der Sonderwünsche
     */
    public List<Sonderwunsch> getSonderwuensche() {
        return grundrissModel.getSonderwuensche();
    }

    /**
     * prüft, ob ein Sonderwunsch (ID) aktuell ausgewählt ist
     * @param id ID des Sonderwunsches
     * @return boolean, true wenn ausgewählt
     */
    public boolean istSonderwunschAusgewaehlt(int id) {
        return grundrissModel.istAusgewaehlt(id);
    }

    /**
     * speichert die Auswahl (aktuell nur in-memory; später Persistierung in DB)
     * @param ids int[], ausgewählte Sonderwunsch-IDs
     */
    public void speichereSonderwuensche(int[] ids) {
        grundrissModel.setAusgewaehlte(ids);
        // später: INSERT/DELETE in Zuordnungstabelle Sonderwunsch_has_Haus
    }

    /**
     * berechnet den Gesamtpreis der aktuell ausgewählten Sonderwünsche
     * @param ids int[], ausgewählte IDs
     * @return int, Gesamtpreis in Euro
     */
    public int berechneGesamtpreis(int[] ids) {
        int sum = 0;
        for (Sonderwunsch sw : grundrissModel.getSonderwuensche()) {
            for (int id : ids) {
                if (sw.getId() == id) {
                    sum += sw.getPreis();
                }
            }
        }
        return sum;
    }
}