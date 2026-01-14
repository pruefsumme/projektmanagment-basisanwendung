package gui.kunde;

import business.kunde.*;

import java.util.Optional;

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

    // das zugehoerige Control-Objekt
    private KundeControl kundeControl;
    // das zugehoerige Model-Objekt
    private KundeModel kundeModel;

    //-------Anfang Attribute der grafischen Oberflaeche-------
    private BorderPane borderPane = new BorderPane();
    private GridPane gridPane = new GridPane();
    private Label lblKunde    	      	= new Label("Kunde");
    private Label lblNummerHaus     	= new Label("Plannummer des Hauses");
    private ComboBox<Integer>
            cmbBxNummerHaus                 = new ComboBox<Integer>();
    private Label lblVorname         	= new Label("Vorname");
    private TextField txtVorname     	= new TextField();
    private Button btnAnlegen	 	  	= new Button("Anlegen");
    private Button btnAendern 	      	= new Button("Ändern");
    private Button btnLoeschen 	 		= new Button("Löschen");
    private MenuBar mnBar 			  	= new MenuBar();
    private Menu mnSonderwuensche    	= new Menu("Sonderwünsche");
    private MenuItem mnItmGrundriss  	= new MenuItem("Grundrissvarianten");
    private MenuItem mnItmCsvExport  	= new MenuItem("Csv Export");

    private Label lblNachname = new Label("Nachname");
    private TextField txtNachname = new TextField();

    private Label lblEmail = new Label("E-Mail");
    private TextField txtEmail = new TextField();

    //!!! Hier: Label-Text geändert – Feld bleibt dasselbe, dient jetzt als Telefonnummer
    private Label lblHausnummer = new Label("Telefonnummer");
    private TextField txtTelefonnummer = new TextField();


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
        gridPane.add(txtTelefonnummer, 1, 6);

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
        mnSonderwuensche.getItems().add(mnItmCsvExport);
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
        mnItmCsvExport.setOnAction(aEvent-> {
            kundeControl.exportiereSonderwuenscheCsv();
        });
    }

    private void holeInfoDachgeschoss(){
    }

    private void leseKunden(){
        // Wird aufgerufen, wenn in der ComboBox eine Plannummer (Hausnummer) gewählt wird.
        Integer planNummer = cmbBxNummerHaus.getValue();

        if (planNummer == null) {
            // Keine Auswahl -> Felder leeren
            txtVorname.clear();
            txtNachname.clear();
            txtEmail.clear();
            txtTelefonnummer.clear(); // Telefonnummer-Feld leeren
            return;
        }

        Kunde kunde = kundeControl.ladeLetztenKundenZuHaus(planNummer);
        if (kunde == null) {
            // Kein Kunde zu dieser Hausnummer vorhanden -> Felder leeren
            txtVorname.clear();
            txtNachname.clear();
            txtEmail.clear();
            txtTelefonnummer.clear();
            return;
        }

        // Daten in der Maske anzeigen
        txtVorname.setText(kunde.getVorname());
        txtNachname.setText(kunde.getNachname());
        txtEmail.setText(kunde.getEmail() == null ? "" : kunde.getEmail());
        txtTelefonnummer.setText(kunde.getTelefonnummer() == null ? "" : kunde.getTelefonnummer());
    }

    private void legeKundenAn(){
        // Objekt kunde fuellen
        Kunde kunde = new Kunde();

        // ---- Plannummer prüfen (Hausnummer) ----
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

        // ---- Telefonnummer prüfen  ----
        String telefon = txtTelefonnummer.getText().trim();
        if (telefon.isEmpty()) {
            zeigeFehlermeldung("Fehlende Telefonnummer", "Bitte geben Sie eine Telefonnummer ein.");
            return;
        }

        // Kunde-Objekt befüllen
        kunde.setVorname(txtVorname.getText());
        kunde.setNachname(txtNachname.getText());
        kunde.setEmail(email);
        kunde.setTelefonnummer(telefon);
        // In der Datenbank ist Haus_Hausnr der Schlüssel -> wir verwenden die Plannummer
        kunde.setHausnummer(planNummer);

        // Neuer Kunde wird sofort in der DB gespeichert
        kundeControl.speichereKunden(kunde);

        // Felder leeren
        txtVorname.clear();
        txtNachname.clear();
        txtEmail.clear();
        txtTelefonnummer.clear();
    }

    private void aendereKunden(){
        // Objekt kunde fuellen
        Kunde kunde = new Kunde();

        // ---- Plannummer prüfen (Hausnummer) ----
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

        // ---- Pflichtfeld Telefonnummer prüfen ----
        String telefon = txtTelefonnummer.getText().trim();
        if (telefon.isEmpty()) {
            zeigeFehlermeldung("Fehlende Telefonnummer", "Bitte geben Sie eine Telefonnummer ein.");
            return;
        }

        // Kunde-Objekt befüllen
        kunde.setVorname(txtVorname.getText());
        kunde.setNachname(txtNachname.getText());
        kunde.setEmail(email);
        kunde.setTelefonnummer(telefon);
        kunde.setHausnummer(planNummer);

        // Änderungen sofort in der Datenbank persistieren
        kundeControl.aktualisiereKunden(kunde);
    }

    private void loescheKunden(){
        Integer planNummer = cmbBxNummerHaus.getValue();

        if (planNummer == null) {
            zeigeFehlermeldung("Fehlende Plannummer", "Bitte wählen Sie zunächst eine Plannummer aus.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Kunde löschen");
        alert.setHeaderText("Kunde löschen");
        alert.setContentText("Möchten Sie die Kundendaten für Haus " + planNummer + " wirklich löschen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Löschen in der Datenbank
            kundeControl.loescheKundenZuHaus(planNummer);

            // Felder leeren
            txtVorname.clear();
            txtNachname.clear();
            txtEmail.clear();
            txtTelefonnummer.clear();
        }
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
