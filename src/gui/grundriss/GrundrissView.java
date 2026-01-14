package gui.grundriss;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

/**
 * Klasse, welche das Fenster mit den Sonderwuenschen zu 
 * den Grundrissvarianten bereitstellt.
 */
public class GrundrissView extends BasisView{
 
 	// das Control-Objekt des Grundriss-Fensters
	private GrundrissControl grundrissControl;
   
    //---Anfang Attribute der grafischen Oberflaeche---
    private Label lblWandKueche    	     
        = new Label("Wand zur Abtrennung der Kueche von dem Essbereich");
    private TextField txtPreisWandKueche 	= new TextField();
    private Label lblWandKuecheEuro 		= new Label("Euro");
    private CheckBox chckBxWandKueche 		= new CheckBox();

    private Label lblTuerKueche    	     
        = new Label("Tuer in der Wand zwischen Kueche und Essbereich");
    private TextField txtPreisTuerKueche 	= new TextField();
    private Label lblTuerKuecheEuro 		= new Label("Euro");
    private CheckBox chckBxTuerKueche 		= new CheckBox();

    private Label lblGrossesZimmer    	     
        = new Label("Grosses Zimmer im OG statt zwei kleinen Zimmern");
    private TextField txtPreisGrossesZimmer 	= new TextField();
    private Label lblGrossesZimmerEuro 		= new Label("Euro");
    private CheckBox chckBxGrossesZimmer 		= new CheckBox();

    private Label lblTreppenraum    	     
        = new Label("Abgetrennter Treppenraum im DG");
    private TextField txtPreisTreppenraum 	= new TextField();
    private Label lblTreppenraumEuro 		= new Label("Euro");
    private CheckBox chckBxTreppenraum 		= new CheckBox();

    private Label lblVorrichtungBad    	     
        = new Label("Vorrichtung eines Bades im DG");
    private TextField txtPreisVorrichtungBad 	= new TextField();
    private Label lblVorrichtungBadEuro 		= new Label("Euro");
    private CheckBox chckBxVorrichtungBad 		= new CheckBox();

    private Label lblAusfuehrungBad    	     
        = new Label("Ausfuehrung eines Bades im DG");
    private TextField txtPreisAusfuehrungBad 	= new TextField();
    private Label lblAusfuehrungBadEuro 		= new Label("Euro");
    private CheckBox chckBxAusfuehrungBad 		= new CheckBox();
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
        grundrissStage.setTitle("Sonderwunsche zu Grundriss-Varianten");
                
	    this.initKomponenten();
	    // this.leseGrundrissSonderwuensche(); // Initiiert durch Control
    }
  
    /* initialisiert die Steuerelemente auf der Maske */
    protected void initKomponenten(){
    	super.initKomponenten(); 
       	super.getLblSonderwunsch().setText("Grundriss-Varianten");
       	
       	super.getGridPaneSonderwunsch().add(lblWandKueche, 0, 1);
    	super.getGridPaneSonderwunsch().add(txtPreisWandKueche, 1, 1);
    	txtPreisWandKueche.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblWandKuecheEuro, 2, 1);
    	super.getGridPaneSonderwunsch().add(chckBxWandKueche, 3, 1);

        super.getGridPaneSonderwunsch().add(lblTuerKueche, 0, 2);
    	super.getGridPaneSonderwunsch().add(txtPreisTuerKueche, 1, 2);
    	txtPreisTuerKueche.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblTuerKuecheEuro, 2, 2);
    	super.getGridPaneSonderwunsch().add(chckBxTuerKueche, 3, 2);

        super.getGridPaneSonderwunsch().add(lblGrossesZimmer, 0, 3);
    	super.getGridPaneSonderwunsch().add(txtPreisGrossesZimmer, 1, 3);
    	txtPreisGrossesZimmer.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblGrossesZimmerEuro, 2, 3);
    	super.getGridPaneSonderwunsch().add(chckBxGrossesZimmer, 3, 3);

        super.getGridPaneSonderwunsch().add(lblTreppenraum, 0, 4);
    	super.getGridPaneSonderwunsch().add(txtPreisTreppenraum, 1, 4);
    	txtPreisTreppenraum.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblTreppenraumEuro, 2, 4);
    	super.getGridPaneSonderwunsch().add(chckBxTreppenraum, 3, 4);

        super.getGridPaneSonderwunsch().add(lblVorrichtungBad, 0, 5);
    	super.getGridPaneSonderwunsch().add(txtPreisVorrichtungBad, 1, 5);
    	txtPreisVorrichtungBad.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblVorrichtungBadEuro, 2, 5);
    	super.getGridPaneSonderwunsch().add(chckBxVorrichtungBad, 3, 5);

        super.getGridPaneSonderwunsch().add(lblAusfuehrungBad, 0, 6);
    	super.getGridPaneSonderwunsch().add(txtPreisAusfuehrungBad, 1, 6);
    	txtPreisAusfuehrungBad.setEditable(false);
    	super.getGridPaneSonderwunsch().add(lblAusfuehrungBadEuro, 2, 6);
    	super.getGridPaneSonderwunsch().add(chckBxAusfuehrungBad, 3, 6);
    }  
    
    /**
	 * macht das GrundrissView-Objekt sichtbar.
	 */
	public void oeffneGrundrissView(){ 
		super.oeffneBasisView();
	}
    
    private void leseGrundrissSonderwuensche(){
    	this.grundrissControl.leseGrundrissSonderwuensche();
    }
    
 	/* berechnet den Preis der ausgesuchten Sonderwuensche und zeigt diesen an */
  	protected void berechneUndZeigePreisSonderwuensche(){
		int[] ausgewaehlteSw = getAusgewaehlteSonderwuensche();
  		if (this.grundrissControl.pruefeKonstellationSonderwuensche(ausgewaehlteSw)){
			int gesamtPreis = 0;
			if (chckBxWandKueche.isSelected()) gesamtPreis += Integer.parseInt(txtPreisWandKueche.getText());
			if (chckBxTuerKueche.isSelected()) gesamtPreis += Integer.parseInt(txtPreisTuerKueche.getText());
			if (chckBxGrossesZimmer.isSelected()) gesamtPreis += Integer.parseInt(txtPreisGrossesZimmer.getText());
			if (chckBxTreppenraum.isSelected()) gesamtPreis += Integer.parseInt(txtPreisTreppenraum.getText());
			if (chckBxVorrichtungBad.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorrichtungBad.getText());
			if (chckBxAusfuehrungBad.isSelected()) gesamtPreis += Integer.parseInt(txtPreisAusfuehrungBad.getText());

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Preisberechnung");
			alert.setHeaderText("Gesamtpreis der Sonderwuensche");
			alert.setContentText("Der Gesamtpreis betraegt: " + gesamtPreis + " Euro");
			alert.showAndWait();
		}
  	}
  	
   	/* speichert die ausgesuchten Sonderwuensche in der Datenbank ab */
  	protected void speichereSonderwuensche(){
		int[] ausgewaehlteSw = getAusgewaehlteSonderwuensche();
 		if (this.grundrissControl.pruefeKonstellationSonderwuensche(ausgewaehlteSw)){
			this.grundrissControl.speichereSonderwuensche(ausgewaehlteSw);
		}
  	}

	private int[] getAusgewaehlteSonderwuensche() {
		int[] ausgewaehlteSw = new int[6];
		if (chckBxWandKueche.isSelected()) ausgewaehlteSw[0] = 1;
		if (chckBxTuerKueche.isSelected()) ausgewaehlteSw[1] = 1;
		if (chckBxGrossesZimmer.isSelected()) ausgewaehlteSw[2] = 1;
		if (chckBxTreppenraum.isSelected()) ausgewaehlteSw[3] = 1;
		if (chckBxVorrichtungBad.isSelected()) ausgewaehlteSw[4] = 1;
		if (chckBxAusfuehrungBad.isSelected()) ausgewaehlteSw[5] = 1;
		return ausgewaehlteSw;
	}

	public void setPreise(int[] preise) {
		txtPreisWandKueche.setText(String.valueOf(preise[0]));
		txtPreisTuerKueche.setText(String.valueOf(preise[1]));
		txtPreisGrossesZimmer.setText(String.valueOf(preise[2]));
		txtPreisTreppenraum.setText(String.valueOf(preise[3]));
		txtPreisVorrichtungBad.setText(String.valueOf(preise[4]));
		txtPreisAusfuehrungBad.setText(String.valueOf(preise[5]));
	}

	public void setAusgewaehlteSonderwuensche(int[] ausgewaehlteSw) {
		chckBxWandKueche.setSelected(ausgewaehlteSw[0] == 1);
		chckBxTuerKueche.setSelected(ausgewaehlteSw[1] == 1);
		chckBxGrossesZimmer.setSelected(ausgewaehlteSw[2] == 1);
		chckBxTreppenraum.setSelected(ausgewaehlteSw[3] == 1);
		chckBxVorrichtungBad.setSelected(ausgewaehlteSw[4] == 1);
		chckBxAusfuehrungBad.setSelected(ausgewaehlteSw[5] == 1);
	}

  	
 	
 }


