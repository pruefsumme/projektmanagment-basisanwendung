package gui.kunde;

import business.kunde.Kunde;
import business.kunde.KundeModel;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Klasse, welche die Maske mit den Kundendaten implementiert.
 */
public class KundeView {
    
    // das Control zum View
    private KundeControl kundeControl;
    // das Model zum View
    private KundeModel kundeModel;	
    // das Haupt-Fenster
    private Stage primaryStage;	
    // Oberflaechen-Elemente
    private GridPane gridPane;
    private Label lblUeberschrift;
    private Label lblVorname;
    private Label lblNachname;
    private Label lblTelefon;
    private Label lblEmail;
    // NEW: Label und ComboBox für Hausnummer
    private Label lblHausnummer;
    private ComboBox<Integer> cbHausnummer;
    private TextField tfVorname;
    private TextField tfNachname;
    private TextField tfTelefon;
    private TextField tfEmail;
    private Button btnSpeichern;
    private MenuBar mnbrMenueLeiste;
    private Menu mnDatei;
    private Menu mnSonderwuensche;
    private MenuItem mnItmBeenden;
    private MenuItem mnItmGrundriss;
    
    /**
     * erzeugt ein View-Objekt zum Grundfenster mit den Kundendaten
     * @param kundeControl KundeControl zum View
     * @param primaryStage Stage fuer dieses Fenster
     * @param kundeModel KundeModel zum View
     */
    public KundeView(KundeControl kundeControl, Stage primaryStage, 
        KundeModel kundeModel){
        this.kundeControl = kundeControl;
        this.primaryStage = primaryStage;
        this.kundeModel = kundeModel;
        this.erzeugeOberflaeche();
    }
    
    /*
     * erzeugt die Oberflaeche
     */
    private void erzeugeOberflaeche(){
        Scene scene = new Scene(this.initGridPane(), 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Verwaltung der Sonderwuensche zu den Haeusern");
        primaryStage.show();
    }
    
    /*
     * initialisiert ein GridPane
     * @return GridPane mit den Oberflaechen-Elementen
     */
    private GridPane initGridPane(){
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(10));
        
        // Ueberschrift
        lblUeberschrift = new Label(kundeModel.getUeberschrift());
        lblUeberschrift.setFont(new Font("Arial", 20));
        gridPane.add(lblUeberschrift, 0, 0, 2, 1);
        
        // Eingabefelder
        lblVorname = new Label("Vorname:");
        gridPane.add(lblVorname, 0, 1);
        tfVorname = new TextField();
        gridPane.add(tfVorname, 1, 1);
        
        lblNachname = new Label("Nachname:");
        gridPane.add(lblNachname, 0, 2);
        tfNachname = new TextField();
        gridPane.add(tfNachname, 1, 2);
        
        lblTelefon = new Label("Telefon:");
        gridPane.add(lblTelefon, 0, 3);
        tfTelefon = new TextField();
        gridPane.add(tfTelefon, 1, 3);
        
        lblEmail = new Label("E-Mail:");
        gridPane.add(lblEmail, 0, 4);
        tfEmail = new TextField();
        gridPane.add(tfEmail, 1, 4);
        
        // NEW: Hausnummer-Auswahl
        lblHausnummer = new Label("Hausnummer:");
        gridPane.add(lblHausnummer, 0, 5);
        cbHausnummer = new ComboBox<>();
        cbHausnummer.setItems(kundeModel.getPlannummern());
        cbHausnummer.setValue(1); // Standardwert
        gridPane.add(cbHausnummer, 1, 5);
        
        // Button
        btnSpeichern = new Button("Speichern");
        btnSpeichern.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                speichereKunden();
            }
        });
        gridPane.add(btnSpeichern, 1, 6);
        
        // Menue
        this.initMenue();
        gridPane.add(mnbrMenueLeiste, 0, 7, 2, 1);
        
        return gridPane;
    }
    
    /*
     * initialisiert das Menue
     */
    private void initMenue(){
        mnbrMenueLeiste = new MenuBar();
        
        mnDatei = new Menu("Datei");
        mnItmBeenden = new MenuItem("Beenden");
        mnItmBeenden.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                System.exit(0);
            }
        });
        mnDatei.getItems().add(mnItmBeenden);
        
        mnSonderwuensche = new Menu("Sonderwuensche");
        mnItmGrundriss = new MenuItem("Grundriss");
        mnItmGrundriss.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                kundeControl.oeffneGrundrissControl();
            }
        });
        mnSonderwuensche.getItems().add(mnItmGrundriss);
        
        mnbrMenueLeiste.getMenus().addAll(mnDatei, mnSonderwuensche);
    }
    
    /*
     * speichert die Kundendaten
     */
    private void speichereKunden(){
        // NEW: Hausnummer aus ComboBox holen
        Integer hausnr = cbHausnummer.getValue();
        
        Kunde kunde = new Kunde(
            0, // ID wird von DB vergeben
            hausnr, // NEW: Hausnummer
            tfVorname.getText(),
            tfNachname.getText(),
            tfTelefon.getText(),
            tfEmail.getText()
        );
        kundeControl.speichereKunden(kunde);
        zeigeInformationsfenster("Information", "Kunde wurde gespeichert!");
    }
    
    /**
     * zeigt ein Informationsfenster an
     * @param titel Titel des Fensters
     * @param inhalt Inhalt des Fensters
     */
    public void zeigeInformationsfenster(String titel, String inhalt){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(inhalt);
        alert.showAndWait();
    }
    
    /**
     * zeigt eine Fehlermeldung an
     * @param titel Titel des Fensters
     * @param inhalt Inhalt des Fensters
     */
    public void zeigeFehlermeldung(String titel, String inhalt){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(inhalt);
        alert.showAndWait();
    }
}