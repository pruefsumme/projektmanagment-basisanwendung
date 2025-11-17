package gui.kunde;

import business.kunde.*;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Klasse, welche das Grundfenster mit den Kundendaten bereitstellt.
 */
public class KundeView{
 
	// das Control-Objekt des Grundfensters mit den Kundendaten
	private KundeControl kundeControl;
	// das Model-Objekt des Grundfensters mit den Kundendaten
	private KundeModel kundeModel;

    //---Anfang Attribute der grafischen Oberflaeche---
	private BorderPane borderPane 		= new BorderPane();
	private GridPane gridPane 			= new GridPane();
	private Label lblKunde    	      	= new Label("Kunde");
    private Label lblNummerHaus     	= new Label("Plannummer des Hauses");
    private ComboBox<Integer> 
        cmbBxNummerHaus                 = new ComboBox<Integer>();
    private Label lblVorname         	= new Label("Vorname");
    private TextField txtVorname     	= new TextField();   
    private Button btnAnlegen	 	  	= new Button("Anlegen");
    private Button btnAendern 	      	= new Button("�ndern");
    private Button btnLoeschen 	 		= new Button("L�schen");
    private MenuBar mnBar 			  	= new MenuBar();
    private Menu mnSonderwuensche    	= new Menu("Sonderw�nsche");
    private MenuItem mnItmGrundriss  	= new MenuItem("Grundrissvarianten");
    
    private Label lblNachname = new Label("Nachname");
    private TextField txtNachname = new TextField();

    private Label lblEmail = new Label("E-Mail");
    private TextField txtEmail = new TextField();

    private Label lblHausnummer = new Label("Hausnummer");
    private TextField txtHausnummer = new TextField();
   
    
    //-------Ende Attribute der grafischen Oberflaeche-------
  
    /**
     * erzeugt ein KundeView-Objekt und initialisiert die Steuerelemente der Maske
     * @param kundeControl KundeControl, enthaelt das zugehoerige Control
     * @param primaryStage Stage, enthaelt das Stage-Objekt fuer diese View
     * @param kundeModel KundeModel, enthaelt das zugehoerige Model
    */
    public KundeView (KundeControl kundeControl, Stage primaryStage, 
    	KundeModel kundeModel){
        this.kundeControl = kundeControl;
        this.kundeModel = kundeModel;
        
        primaryStage.setTitle(this.kundeModel.getUeberschrift());	
	    Scene scene = new Scene(borderPane, 550, 400);
	    primaryStage.setScene(scene);
        primaryStage.show();

	    this.initKomponenten();
	    this.initListener();
    }

 
    /* initialisiert die Steuerelemente auf der Maske */
    private void initKomponenten(){
    	borderPane.setCenter(gridPane);
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(25, 25, 25, 25));
       	
	    gridPane.add(lblKunde, 0, 1);
       	lblKunde.setMinSize(150, 40);
	    lblKunde.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    gridPane.add(lblNummerHaus, 0, 2);
	    gridPane.add(cmbBxNummerHaus, 1, 2);
	    cmbBxNummerHaus.setMinSize(150,  25);
	    cmbBxNummerHaus.setItems(this.kundeModel.getPlannummern());
	    gridPane.add(lblVorname, 0, 3);
	    gridPane.add(txtVorname, 1, 3);
	    
	    gridPane.add(lblNachname, 0, 4);
	    gridPane.add(txtNachname, 1, 4);

	    gridPane.add(lblEmail, 0, 5);
	    gridPane.add(txtEmail, 1, 5);

	    gridPane.add(lblHausnummer, 0, 6);
	    gridPane.add(txtHausnummer, 1, 6);
        
	    
	    // Buttons
	    gridPane.add(btnAnlegen, 0, 7);
	    btnAnlegen.setMinSize(150,  25);
	    gridPane.add(btnAendern, 1, 7);
	    btnAendern.setMinSize(150,  25);
	    gridPane.add(btnLoeschen, 2, 7);
	    btnLoeschen.setMinSize(150,  25);
	    // MenuBar und Menu
	    borderPane.setTop(mnBar);
	    mnBar.getMenus().add(mnSonderwuensche);
	    mnSonderwuensche.getItems().add(mnItmGrundriss);
    }

    /* initialisiert die Listener zu den Steuerelementen auf de Maske */
    private void initListener(){
    	cmbBxNummerHaus.setOnAction(aEvent-> {
    		 holeInfoDachgeschoss();  
    		 leseKunden();
     	});
       	btnAnlegen.setOnAction(aEvent-> {
 	        legeKundenAn();
	    });
    	btnAendern.setOnAction(aEvent-> {
           	aendereKunden();
	    });
       	btnLoeschen.setOnAction(aEvent-> { 
           	loescheKunden();
	    });
      	mnItmGrundriss.setOnAction(aEvent-> {
 	        kundeControl.oeffneGrundrissControl(); 
	    });
    }
    
    private void holeInfoDachgeschoss(){ 
    }
    
    private void leseKunden(){
    }
    
    private void legeKundenAn(){
         Kunde kunde = null;
         // Objekt kunde fuellen
         
         
         // ---- Plannummer prüfen ----
         Integer planNummer = cmbBxNummerHaus.getValue();

         if (planNummer == null) {
             zeigeFehlermeldung("Fehlende Plannummer", "Bitte wählen Sie eine Plannummer aus.");
             return;
         }
         if (planNummer < 1 || planNummer > 24) {
             zeigeFehlermeldung("Ungültige Plannummer", "Die Plannummer muss zwischen 1 und 24 liegen.");
             return;
         }
         
         // ---- Pflichtfeld: Vorname ----
         String vorname = txtVorname.getText().trim();
         if (vorname.isEmpty()) {
             zeigeFehlermeldung("Fehlender Vorname", "Der Vorname darf nicht leer sein.");
             return;
         }
         // Prüfen, ob nur Buchstaben erlaubt sind
         if (!vorname.matches("[a-zA-ZäöüÄÖÜß\\- ]+")) {
             zeigeFehlermeldung("Ungültiger Vorname", "Der Vorname darf nur Buchstaben enthalten.");
             return;
         }

         // ---- Pflichtfeld: Nachname ----
         String nachname = txtNachname.getText().trim();
         if (nachname.isEmpty()) {
             zeigeFehlermeldung("Fehlender Nachname", "Der Nachname darf nicht leer sein.");
             return;
         }
         // Prüfen, ob nur Buchstaben erlaubt sind
         if (!nachname.matches("[a-zA-ZäöüÄÖÜß\\- ]+")) {
             zeigeFehlermeldung("Ungültiger Nachname", "Der Nachname darf nur Buchstaben enthalten.");
             return;
         }


         // ---- Pflichtfeld + Formatprüfung: E-Mail ----
         String email = txtEmail.getText().trim();
         if (email.isEmpty()) {
             zeigeFehlermeldung("Fehlende E-Mail", "Die E-Mail-Adresse darf nicht leer sein.");
             return;
         }
         if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
             zeigeFehlermeldung("Ungültige E-Mail", "Bitte geben Sie eine gültige E-Mail-Adresse ein.");
             return;
         }

         // ---- Hausnummer prüfen (muss positive Zahl sein) ----
         int hausnummer;
         try {
             hausnummer = Integer.parseInt(txtHausnummer.getText().trim());
             if (hausnummer <= 0) {
                 zeigeFehlermeldung("Ungültige Hausnummer", "Die Hausnummer muss größer als 0 sein.");
                 return;
             }
         } catch (NumberFormatException e) {
             zeigeFehlermeldung("Ungültige Hausnummer", "Bitte geben Sie eine gültige Zahl ein.");
             return;
         }
         
         kunde.setVorname(txtVorname.getText());
         kunde.setNachname(txtNachname.getText());
         kunde.setEmail(email);
         kunde.setHausnummer(hausnummer);
        
         kundeControl.speichereKunden(kunde);
         
         //Felder leeren
         txtVorname.clear();
         txtNachname.clear();
         txtEmail.clear();
         txtHausnummer.clear();
         
         
         
   	}
    
  	private void aendereKunden(){
   	}
  	
   	private void loescheKunden(){
   	}
   	
   /** zeigt ein Fehlermeldungsfenster an
    * @param ueberschrift, Ueberschrift fuer das Fehlermeldungsfenster
    * @param meldung, String, welcher die Fehlermeldung enthaelt
    */
    public void zeigeFehlermeldung(String ueberschrift, String meldung){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehlermeldung");
        alert.setHeaderText(ueberschrift);
        alert.setContentText(meldung);
        alert.show();
    }

}


