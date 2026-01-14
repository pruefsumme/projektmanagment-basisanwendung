package gui.innentueren;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class InnentuerenView extends BasisView {

    private InnentuerenControl control;

    // UI Elements
    // 4.1) Mehrpreis für die Ausführung eines Glasausschnitts (Klarglas) in einer Innentür
    private Label lblGlasKlar = new Label("Glasausschnitt (Klarglas) in einer Innentuer");
    private TextField txtPreisGlasKlar = new TextField();
    private Label lblGlasKlarEuro = new Label("Euro");
    private CheckBox chckBxGlasKlar = new CheckBox();

    // 4.2) Mehrpreis für die Ausführung eines Glasausschnitts (Milchglas) in einer Innentür
    private Label lblGlasMilch = new Label("Glasausschnitt (Milchglas) in einer Innentuer");
    private TextField txtPreisGlasMilch = new TextField();
    private Label lblGlasMilchEuro = new Label("Euro");
    private CheckBox chckBxGlasMilch = new CheckBox();

    // 4.3) Innentür zur Garage als Holztür
    private Label lblGarageHolz = new Label("Innentuer zur Garage als Holztuer");
    private TextField txtPreisGarageHolz = new TextField();
    private Label lblGarageHolzEuro = new Label("Euro");
    private CheckBox chckBxGarageHolz = new CheckBox();

    // Buttons fuer Pruefung und Export
    private Button btnCsvExport = new Button("Csv Export");


    public InnentuerenView(InnentuerenControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwuensche zu Innentueren");
        this.initKomponenten();
        // Erneuter Aufruf, da beim ersten Aufruf durch super() die Buttons noch null waren
        this.initListener();
    }

    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Innentueren");

        // Row 1
        super.getGridPaneSonderwunsch().add(lblGlasKlar, 0, 1);
        super.getGridPaneSonderwunsch().add(txtPreisGlasKlar, 1, 1);
        txtPreisGlasKlar.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGlasKlarEuro, 2, 1);
        super.getGridPaneSonderwunsch().add(chckBxGlasKlar, 3, 1);

        // Row 2
        super.getGridPaneSonderwunsch().add(lblGlasMilch, 0, 2);
        super.getGridPaneSonderwunsch().add(txtPreisGlasMilch, 1, 2);
        txtPreisGlasMilch.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGlasMilchEuro, 2, 2);
        super.getGridPaneSonderwunsch().add(chckBxGlasMilch, 3, 2);

        // Row 3
        super.getGridPaneSonderwunsch().add(lblGarageHolz, 0, 3);
        super.getGridPaneSonderwunsch().add(txtPreisGarageHolz, 1, 3);
        txtPreisGarageHolz.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGarageHolzEuro, 2, 3);
        super.getGridPaneSonderwunsch().add(chckBxGarageHolz, 3, 3);
        
        // Buttons hinzufuegen
        super.getGridPaneButtons().add(btnCsvExport, 3, 0);
        btnCsvExport.setMinSize(150, 25);
    }

    public void oeffneView() {
        super.oeffneBasisView();
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (btnCsvExport != null) {
            btnCsvExport.setOnAction(aEvent -> {
                exportiereCsv();
            });
        }
    }

    private void exportiereCsv() {
        control.exportiereCsv();
    }

    /* zeigt eine Fehlermeldung an */
    private void zeigeFehlermeldung(String ueberschrift, String meldung){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehlermeldung");
        alert.setHeaderText(ueberschrift);
        alert.setContentText(meldung);
        alert.show();
    }

    @Override
    protected void berechneUndZeigePreisSonderwuensche() {
        int[] selection = getAusgewaehlteSonderwuensche();
        if (control.pruefeKonstellationSonderwuensche(selection)) {
            int total = 0;
            if (chckBxGlasKlar.isSelected()) total += Integer.parseInt(txtPreisGlasKlar.getText());
            if (chckBxGlasMilch.isSelected()) total += Integer.parseInt(txtPreisGlasMilch.getText());
            if (chckBxGarageHolz.isSelected()) total += Integer.parseInt(txtPreisGarageHolz.getText());

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Preisberechnung");
            alert.setHeaderText("Gesamtpreis der Sonderwuensche");
            alert.setContentText("Der Gesamtpreis betraegt: " + total + " Euro");
            alert.showAndWait();
        } else {
            zeigeFehlermeldung("Ungültige Konstellation", "Die Kombination der gewählten Sonderwünsche ist nicht zulässig.");
        }
    }

    @Override
    protected void speichereSonderwuensche() {
        int[] selection = getAusgewaehlteSonderwuensche();
        if (control.pruefeKonstellationSonderwuensche(selection)) {
            control.speichereSonderwuensche(selection);
        } else {
            zeigeFehlermeldung("Ungültige Konstellation", "Die Kombination der gewählten Sonderwünsche ist nicht zulässig.");
        }
    }

    private int[] getAusgewaehlteSonderwuensche() {
        int[] sel = new int[3];
        sel[0] = chckBxGlasKlar.isSelected() ? 1 : 0;
        sel[1] = chckBxGlasMilch.isSelected() ? 1 : 0;
        sel[2] = chckBxGarageHolz.isSelected() ? 1 : 0;
        return sel;
    }

    public void setPreise(int[] prices) {
        if (prices.length >= 3) {
            txtPreisGlasKlar.setText(String.valueOf(prices[0]));
            txtPreisGlasMilch.setText(String.valueOf(prices[1]));
            txtPreisGarageHolz.setText(String.valueOf(prices[2]));
        }
    }

    public void setSelection(int[] selection) {
        if (selection.length >= 3) {
            chckBxGlasKlar.setSelected(selection[0] == 1);
            chckBxGlasMilch.setSelected(selection[1] == 1);
            chckBxGarageHolz.setSelected(selection[2] == 1);
        }
    }
}
