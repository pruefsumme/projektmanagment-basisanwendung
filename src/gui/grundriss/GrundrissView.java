package gui.grundriss;

import business.grundriss.Sonderwunsch;
import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, welche das Fenster mit den Sonderwuenschen zu 
 * den Grundrissvarianten bereitstellt.
 */
public class GrundrissView extends BasisView{
 
     // das Control-Objekt des Grundriss-Fensters
    private GrundrissControl grundrissControl;
   
    //---Anfang Attribute der grafischen Oberflaeche---
    // Dynamische Liste für Checkboxen statt einzelner Attribute, um alle DB-Einträge abzubilden
    private List<CheckBox> checkBoxes = new ArrayList<>();
    // Label für die Anzeige des Gesamtpreises
    private Label lblGesamtpreis = new Label("0 Euro");
    //-------Ende Attribute der grafischen Oberflaeche-------
  
    /**
     * erzeugt ein GrundrissView-Objekt, belegt das zugehoerige Control
     * mit dem vorgegebenen Objekt und initialisiert die Steuerelemente der Maske
     * @param grundrissControl GrundrissControl, enthaelt das zugehoerige Control
     * @param grundrissStage Stage, enthaelt das Stage-Objekt fuer diese View
     */
    public GrundrissView (GrundrissControl grundrissControl, Stage grundrissStage){
        super(grundrissStage);
        this.grundrissControl = grundrissControl;
        grundrissStage.setTitle("Sonderwuensche zu Grundriss-Varianten");
                
        this.initKomponenten();
        this.leseGrundrissSonderwuensche();
    }
  
    /* initialisiert die Steuerelemente auf der Maske */
    protected void initKomponenten(){
        super.initKomponenten(); 
        super.getLblSonderwunsch().setText("Grundriss-Varianten");
        // Komponenten werden dynamisch in leseGrundrissSonderwuensche hinzugefügt
        
        // Gesamtpreis-Label dem Grid hinzufügen
        Label lblGesamtpreisText = new Label("Gesamtpreis:");
        super.getGridPaneSonderwunsch().add(lblGesamtpreisText, 0, 0);
        super.getGridPaneSonderwunsch().add(lblGesamtpreis, 1, 0);
    }  
    
    /**
     * macht das GrundrissView-Objekt sichtbar.
     */
    public void oeffneGrundrissView(){ 
        super.oeffneBasisView();
    }
    
    private void leseGrundrissSonderwuensche(){
        this.grundrissControl.leseGrundrissSonderwuensche();
        
        // Liste der Sonderwünsche vom Control holen und dynamisch anzeigen
        List<Sonderwunsch> sonderwuensche = this.grundrissControl.getSonderwuensche();
        int row = 1;
        
        // Alte Checkboxen leeren falls Methode mehrfach aufgerufen wird
        checkBoxes.clear();
        // Grid bereinigen (optional, falls Refresh nötig wäre, hier vereinfacht nur Aufbau)
        
        for (Sonderwunsch sw : sonderwuensche) {
            Label lblBeschreibung = new Label(sw.getBeschreibung());
            TextField txtPreis = new TextField(String.valueOf(sw.getPreis()));
            txtPreis.setEditable(false);
            Label lblEuro = new Label("Euro");
            CheckBox chkBox = new CheckBox();
            
            // ID im UserData speichern, um später darauf zuzugreifen
            chkBox.setUserData(sw.getId());
            
            // Status setzen (falls schon ausgewählt)
            if (this.grundrissControl.istSonderwunschAusgewaehlt(sw.getId())) {
                chkBox.setSelected(true);
            }
            
            // Event-Handler für Preisberechnung bei Änderung der Auswahl
            chkBox.setOnAction(e -> berechneUndZeigePreisSonderwuensche());
            
            checkBoxes.add(chkBox);
            
            super.getGridPaneSonderwunsch().add(lblBeschreibung, 0, row);
            super.getGridPaneSonderwunsch().add(txtPreis, 1, row);
            super.getGridPaneSonderwunsch().add(lblEuro, 2, row);
            super.getGridPaneSonderwunsch().add(chkBox, 3, row);
            
            row++;
        }
        
        // Initialen Preis berechnen und anzeigen
        berechneUndZeigePreisSonderwuensche();
    }
    
    /* berechnet den Preis der ausgesuchten Sonderwuensche und zeigt diesen an */
    protected void berechneUndZeigePreisSonderwuensche(){
        // IDs der ausgewählten Checkboxen sammeln
        int[] selectedIds = checkBoxes.stream()
            .filter(CheckBox::isSelected)
            .mapToInt(cb -> (int) cb.getUserData())
            .toArray();
            
        // Konstellation prüfen und Preis berechnen
        boolean konstellationOk = this.grundrissControl.pruefeKonstellationSonderwuensche(selectedIds);
        
        if (konstellationOk) {
            double gesamtpreis = this.grundrissControl.berechnePreisSonderwuensche(selectedIds);
            lblGesamtpreis.setText(String.valueOf((int)gesamtpreis) + " Euro");
            lblGesamtpreis.setStyle("-fx-text-fill: black;");
        } else {
            lblGesamtpreis.setText("Ungültige Kombination!");
            lblGesamtpreis.setStyle("-fx-text-fill: red;");
        }
    }
      
    /* speichert die ausgesuchten Sonderwuensche in der Datenbank ab */
    protected void speichereSonderwuensche(){
        // Es wird erst die Methode pruefeKonstellationSonderwuensche(int[] ausgewaehlteSw)
        // aus dem Control aufgerufen, dann die Sonderwuensche gespeichert.
        
        // IDs der ausgewählten Checkboxen sammeln
        int[] selectedIds = checkBoxes.stream()
            .filter(CheckBox::isSelected)
            .mapToInt(cb -> (int) cb.getUserData())
            .toArray();
            
        this.grundrissControl.speichereSonderwuensche(selectedIds);
    }
}