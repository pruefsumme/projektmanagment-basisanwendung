package gui.sanitaer;

import gui.basis.BasisView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class SanitaerView extends BasisView {
    
    private SanitaerControl control;
    
    // UI Elements
    // 6.1
    private Label lblWaschbeckenOG = new Label("Groesseres Waschbecken im OG");
    private TextField txtPreisWaschbeckenOG = new TextField();
    private Label lblWaschbeckenOGEuro = new Label("Euro");
    private CheckBox chckBxWaschbeckenOG = new CheckBox();

    // 6.2
    private Label lblWaschbeckenDG = new Label("Groesseres Waschbecken im DG");
    private TextField txtPreisWaschbeckenDG = new TextField();
    private Label lblWaschbeckenDGEuro = new Label("Euro");
    private CheckBox chckBxWaschbeckenDG = new CheckBox();
    
    // 6.3
    private Label lblDuscheOG = new Label("Bodentiefe Dusche im OG");
    private TextField txtPreisDuscheOG = new TextField();
    private Label lblDuscheOGEuro = new Label("Euro");
    private CheckBox chckBxDuscheOG = new CheckBox();
    
    // 6.4
    private Label lblDuscheDG = new Label("Bodentiefe Dusche im DG");
    private TextField txtPreisDuscheDG = new TextField();
    private Label lblDuscheDGEuro = new Label("Euro");
    private CheckBox chckBxDuscheDG = new CheckBox();
    
    private Button btnCsvExport = new Button("Csv Export");
    
    public SanitaerView(SanitaerControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwuensche zur Sanitaerinstallation");
        this.initKomponenten();
        this.initListener();
    }
    
    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Sanitaerinstallation");
        
        // Row 1
        super.getGridPaneSonderwunsch().add(lblWaschbeckenOG, 0, 1);
        super.getGridPaneSonderwunsch().add(txtPreisWaschbeckenOG, 1, 1);
        txtPreisWaschbeckenOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblWaschbeckenOGEuro, 2, 1);
        super.getGridPaneSonderwunsch().add(chckBxWaschbeckenOG, 3, 1);
        
        // Row 2
        super.getGridPaneSonderwunsch().add(lblWaschbeckenDG, 0, 2);
        super.getGridPaneSonderwunsch().add(txtPreisWaschbeckenDG, 1, 2);
        txtPreisWaschbeckenDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblWaschbeckenDGEuro, 2, 2);
        super.getGridPaneSonderwunsch().add(chckBxWaschbeckenDG, 3, 2);
        
        // Row 3
        super.getGridPaneSonderwunsch().add(lblDuscheOG, 0, 3);
        super.getGridPaneSonderwunsch().add(txtPreisDuscheOG, 1, 3);
        txtPreisDuscheOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblDuscheOGEuro, 2, 3);
        super.getGridPaneSonderwunsch().add(chckBxDuscheOG, 3, 3);
        
        // Row 4
        super.getGridPaneSonderwunsch().add(lblDuscheDG, 0, 4);
        super.getGridPaneSonderwunsch().add(txtPreisDuscheDG, 1, 4);
        txtPreisDuscheDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblDuscheDGEuro, 2, 4);
        super.getGridPaneSonderwunsch().add(chckBxDuscheDG, 3, 4);
        
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
    
    private void zeigeFehlermeldung(String ueberschrift, String meldung) {
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
            if (chckBxWaschbeckenOG.isSelected()) total += Integer.parseInt(txtPreisWaschbeckenOG.getText());
            if (chckBxWaschbeckenDG.isSelected()) total += Integer.parseInt(txtPreisWaschbeckenDG.getText());
            if (chckBxDuscheOG.isSelected()) total += Integer.parseInt(txtPreisDuscheOG.getText());
            if (chckBxDuscheDG.isSelected()) total += Integer.parseInt(txtPreisDuscheDG.getText());

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
        int[] sel = new int[4];
        sel[0] = chckBxWaschbeckenOG.isSelected() ? 1 : 0;
        sel[1] = chckBxWaschbeckenDG.isSelected() ? 1 : 0;
        sel[2] = chckBxDuscheOG.isSelected() ? 1 : 0;
        sel[3] = chckBxDuscheDG.isSelected() ? 1 : 0;
        return sel;
    }

    public void setPreise(int[] prices) {
         if (prices.length >= 4) {
             txtPreisWaschbeckenOG.setText(String.valueOf(prices[0]));
             txtPreisWaschbeckenDG.setText(String.valueOf(prices[1]));
             txtPreisDuscheOG.setText(String.valueOf(prices[2]));
             txtPreisDuscheDG.setText(String.valueOf(prices[3]));
         }
    }
     
    public void setSelection(int[] selection) {
         if (selection.length >= 4) {
             chckBxWaschbeckenOG.setSelected(selection[0] == 1);
             chckBxWaschbeckenDG.setSelected(selection[1] == 1);
             chckBxDuscheOG.setSelected(selection[2] == 1);
             chckBxDuscheDG.setSelected(selection[3] == 1);
         }
    }
}
