package gui.fenster;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

/**
 * Klasse, welche das Fenster mit den Sonderwuenschen zu 
 * den Fenstern und Aussentueren bereitstellt.
 */
public class FensterView extends BasisView{
 
 	// das Control-Objekt des Fenster-Fensters
	private FensterControl fensterControl;
   
    //---Anfang Attribute der grafischen Oberflaeche---
    private Label lblSchiebetuerenEG = new Label("Schiebetueren im EG zur Terrasse");
    private TextField txtPreisSchiebetuerenEG = new TextField();
    private Label lblSchiebetuerenEGEuro = new Label("Euro");
    private CheckBox chckBxSchiebetuerenEG = new CheckBox();

    private Label lblSchiebetuerenDG = new Label("Schiebetueren im DG zur Dachterrasse");
    private TextField txtPreisSchiebetuerenDG = new TextField();
    private Label lblSchiebetuerenDGEuro = new Label("Euro");
    private CheckBox chckBxSchiebetuerenDG = new CheckBox();

    private Label lblEinbruchschutz = new Label("Erhoehter Einbruchschutz an der Haustuer");
    private TextField txtPreisEinbruchschutz = new TextField();
    private Label lblEinbruchschutzEuro = new Label("Euro");
    private CheckBox chckBxEinbruchschutz = new CheckBox();

    private Label lblVorlRolladenEG = new Label("Vorbereitung el. Antriebe Rolladen EG");
    private TextField txtPreisVorlRolladenEG = new TextField();
    private Label lblVorlRolladenEGEuro = new Label("Euro");
    private CheckBox chckBxVorlRolladenEG = new CheckBox();

    private Label lblVorlRolladenOG = new Label("Vorbereitung el. Antriebe Rolladen OG");
    private TextField txtPreisVorlRolladenOG = new TextField();
    private Label lblVorlRolladenOGEuro = new Label("Euro");
    private CheckBox chckBxVorlRolladenOG = new CheckBox();

    private Label lblVorlRolladenDG = new Label("Vorbereitung el. Antriebe Rolladen DG");
    private TextField txtPreisVorlRolladenDG = new TextField();
    private Label lblVorlRolladenDGEuro = new Label("Euro");
    private CheckBox chckBxVorlRolladenDG = new CheckBox();

    private Label lblElRolladenEG = new Label("Elektrische Rolladen EG");
    private TextField txtPreisElRolladenEG = new TextField();
    private Label lblElRolladenEGEuro = new Label("Euro");
    private CheckBox chckBxElRolladenEG = new CheckBox();

    private Label lblElRolladenOG = new Label("Elektrische Rolladen OG");
    private TextField txtPreisElRolladenOG = new TextField();
    private Label lblElRolladenOGEuro = new Label("Euro");
    private CheckBox chckBxElRolladenOG = new CheckBox();

    private Label lblElRolladenDG = new Label("Elektrische Rolladen DG");
    private TextField txtPreisElRolladenDG = new TextField();
    private Label lblElRolladenDGEuro = new Label("Euro");
    private CheckBox chckBxElRolladenDG = new CheckBox();

    private Button btnExport = new Button("Export CSV");

    //-------Ende Attribute der grafischen Oberflaeche-------
  
    /**
     * erzeugt ein FensterView-Objekt
     */
    public FensterView (FensterControl fensterControl, Stage fensterStage){
    	super(fensterStage);
        this.fensterControl = fensterControl;
        fensterStage.setTitle("Sonderwuensche zu Fenster und Aussentueren");
                
	    this.initKomponenten();
    }
    
    private void exportiereCsv() {
        this.fensterControl.exportiereCsv();
    }
  
    /* initialisiert die Steuerelemente auf der Maske */
    protected void initKomponenten(){
    	super.initKomponenten(); 
       	super.getLblSonderwunsch().setText("Fenster und Aussentueren");
       	
        // Row 1
       	super.getGridPaneSonderwunsch().add(lblSchiebetuerenEG, 0, 1);
    	super.getGridPaneSonderwunsch().add(txtPreisSchiebetuerenEG, 1, 1);
    	txtPreisSchiebetuerenEG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblSchiebetuerenEGEuro, 2, 1);
    	super.getGridPaneSonderwunsch().add(chckBxSchiebetuerenEG, 3, 1);

        // Row 2
        super.getGridPaneSonderwunsch().add(lblSchiebetuerenDG, 0, 2);
    	super.getGridPaneSonderwunsch().add(txtPreisSchiebetuerenDG, 1, 2);
    	txtPreisSchiebetuerenDG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblSchiebetuerenDGEuro, 2, 2);
    	super.getGridPaneSonderwunsch().add(chckBxSchiebetuerenDG, 3, 2);

        // Row 3
        super.getGridPaneSonderwunsch().add(lblEinbruchschutz, 0, 3);
    	super.getGridPaneSonderwunsch().add(txtPreisEinbruchschutz, 1, 3);
    	txtPreisEinbruchschutz.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblEinbruchschutzEuro, 2, 3);
    	super.getGridPaneSonderwunsch().add(chckBxEinbruchschutz, 3, 3);

        // Row 4
        super.getGridPaneSonderwunsch().add(lblVorlRolladenEG, 0, 4);
    	super.getGridPaneSonderwunsch().add(txtPreisVorlRolladenEG, 1, 4);
    	txtPreisVorlRolladenEG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblVorlRolladenEGEuro, 2, 4);
    	super.getGridPaneSonderwunsch().add(chckBxVorlRolladenEG, 3, 4);

        // Row 5
        super.getGridPaneSonderwunsch().add(lblVorlRolladenOG, 0, 5);
    	super.getGridPaneSonderwunsch().add(txtPreisVorlRolladenOG, 1, 5);
    	txtPreisVorlRolladenOG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblVorlRolladenOGEuro, 2, 5);
    	super.getGridPaneSonderwunsch().add(chckBxVorlRolladenOG, 3, 5);

        // Row 6
        super.getGridPaneSonderwunsch().add(lblVorlRolladenDG, 0, 6);
    	super.getGridPaneSonderwunsch().add(txtPreisVorlRolladenDG, 1, 6);
    	txtPreisVorlRolladenDG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblVorlRolladenDGEuro, 2, 6);
    	super.getGridPaneSonderwunsch().add(chckBxVorlRolladenDG, 3, 6);

        // Row 7
        super.getGridPaneSonderwunsch().add(lblElRolladenEG, 0, 7);
    	super.getGridPaneSonderwunsch().add(txtPreisElRolladenEG, 1, 7);
    	txtPreisElRolladenEG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblElRolladenEGEuro, 2, 7);
    	super.getGridPaneSonderwunsch().add(chckBxElRolladenEG, 3, 7);

        // Row 8
        super.getGridPaneSonderwunsch().add(lblElRolladenOG, 0, 8);
    	super.getGridPaneSonderwunsch().add(txtPreisElRolladenOG, 1, 8);
    	txtPreisElRolladenOG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblElRolladenOGEuro, 2, 8);
    	super.getGridPaneSonderwunsch().add(chckBxElRolladenOG, 3, 8);

        // Row 9
        super.getGridPaneSonderwunsch().add(lblElRolladenDG, 0, 9);
    	super.getGridPaneSonderwunsch().add(txtPreisElRolladenDG, 1, 9);
    	txtPreisElRolladenDG.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblElRolladenDGEuro, 2, 9);
    	super.getGridPaneSonderwunsch().add(chckBxElRolladenDG, 3, 9);

        // Export Button
        super.getGridPaneButtons().add(btnExport, 3, 0);
        btnExport.setMinSize(150, 25);
        btnExport.setOnAction(aEvent -> {
            exportiereCsv();
        });
    }  
    
    /**
	 * macht das View-Objekt sichtbar.
	 */
	public void oeffneFensterView(){ 
		super.oeffneBasisView();
	}
    
    /* zeigt eine Fehlermeldung an */
    private void zeigeFehlermeldung(String ueberschrift, String meldung){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehlermeldung");
        alert.setHeaderText(ueberschrift);
        alert.setContentText(meldung);
        alert.show();
    }

 	/* berechnet den Preis der ausgesuchten Sonderwuensche und zeigt diesen an */
  	protected void berechneUndZeigePreisSonderwuensche(){
		int[] ausgewaehlteSw = getAusgewaehlteSonderwuensche();
  		if (this.fensterControl.pruefeKonstellationSonderwuensche(ausgewaehlteSw)){
			int gesamtPreis = 0;
            if (chckBxSchiebetuerenEG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisSchiebetuerenEG.getText());
            if (chckBxSchiebetuerenDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisSchiebetuerenDG.getText());
            if (chckBxEinbruchschutz.isSelected()) gesamtPreis += Integer.parseInt(txtPreisEinbruchschutz.getText());
            if (chckBxVorlRolladenEG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorlRolladenEG.getText());
            if (chckBxVorlRolladenOG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorlRolladenOG.getText());
            if (chckBxVorlRolladenDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorlRolladenDG.getText());
            if (chckBxElRolladenEG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisElRolladenEG.getText());
            if (chckBxElRolladenOG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisElRolladenOG.getText());
            if (chckBxElRolladenDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisElRolladenDG.getText());

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Preisberechnung");
			alert.setHeaderText("Gesamtpreis der Sonderwuensche");
			alert.setContentText("Der Gesamtpreis betraegt: " + gesamtPreis + " Euro");
			alert.showAndWait();
		} else {
             zeigeFehlermeldung("Ungültige Konstellation", "Die Kombination der gewählten Sonderwünsche ist nicht zulässig.\nBitte überprüfen Sie Ihre Auswahl bezüglich der Rolläden.");
        }
  	}
  	
   	/* speichert die ausgesuchten Sonderwuensche in der Datenbank ab */
  	protected void speichereSonderwuensche(){
		int[] ausgewaehlteSw = getAusgewaehlteSonderwuensche();
 		if (this.fensterControl.pruefeKonstellationSonderwuensche(ausgewaehlteSw)){
			this.fensterControl.speichereSonderwuensche(ausgewaehlteSw);
		} else {
             zeigeFehlermeldung("Ungültige Konstellation", "Die Kombination der gewählten Sonderwünsche ist nicht zulässig.\nBitte überprüfen Sie Ihre Auswahl bezüglich der Rolläden.");
        }
  	}

	private int[] getAusgewaehlteSonderwuensche() {
		int[] ausgewaehlteSw = new int[9];
		if (chckBxSchiebetuerenEG.isSelected()) ausgewaehlteSw[0] = 1;
		if (chckBxSchiebetuerenDG.isSelected()) ausgewaehlteSw[1] = 1;
		if (chckBxEinbruchschutz.isSelected()) ausgewaehlteSw[2] = 1;

        if (chckBxVorlRolladenEG.isSelected()) ausgewaehlteSw[3] = 1;
        if (chckBxVorlRolladenOG.isSelected()) ausgewaehlteSw[4] = 1;
        if (chckBxVorlRolladenDG.isSelected()) ausgewaehlteSw[5] = 1;

        if (chckBxElRolladenEG.isSelected()) ausgewaehlteSw[6] = 1;
        if (chckBxElRolladenOG.isSelected()) ausgewaehlteSw[7] = 1;
        if (chckBxElRolladenDG.isSelected()) ausgewaehlteSw[8] = 1;
		return ausgewaehlteSw;
	}

	public void setPreise(int[] preise) {
        txtPreisSchiebetuerenEG.setText(String.valueOf(preise[0]));
        txtPreisSchiebetuerenDG.setText(String.valueOf(preise[1]));
        txtPreisEinbruchschutz.setText(String.valueOf(preise[2]));
        txtPreisVorlRolladenEG.setText(String.valueOf(preise[3]));
        txtPreisVorlRolladenOG.setText(String.valueOf(preise[4]));
        txtPreisVorlRolladenDG.setText(String.valueOf(preise[5]));
        txtPreisElRolladenEG.setText(String.valueOf(preise[6]));
        txtPreisElRolladenOG.setText(String.valueOf(preise[7]));
        txtPreisElRolladenDG.setText(String.valueOf(preise[8]));
	}

	public void setAusgewaehlteSonderwuensche(int[] ausgewaehlteSw) {
		chckBxSchiebetuerenEG.setSelected(ausgewaehlteSw[0] == 1);
        chckBxSchiebetuerenDG.setSelected(ausgewaehlteSw[1] == 1);
        chckBxEinbruchschutz.setSelected(ausgewaehlteSw[2] == 1);
        chckBxVorlRolladenEG.setSelected(ausgewaehlteSw[3] == 1);
        chckBxVorlRolladenOG.setSelected(ausgewaehlteSw[4] == 1);
        chckBxVorlRolladenDG.setSelected(ausgewaehlteSw[5] == 1);
        chckBxElRolladenEG.setSelected(ausgewaehlteSw[6] == 1);
        chckBxElRolladenOG.setSelected(ausgewaehlteSw[7] == 1);
        chckBxElRolladenDG.setSelected(ausgewaehlteSw[8] == 1);
	}
}
