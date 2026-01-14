package gui.heizung;

import gui.basis.BasisView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class HeizungView extends BasisView {

    private HeizungControl control;
    
    // UI Elements
    // 5.1
    private Label lblHeizungZusatz = new Label("Zusaetzlicher Standard-Heizkoerper");
    private TextField txtPreisHeizungZusatz = new TextField();
    private Label lblHeizungZusatzEuro = new Label("Euro");
    private CheckBox chckBxHeizungZusatz = new CheckBox();

    // 5.2
    private Label lblHeizungGlatt = new Label("Heizkoerper mit glatter Oberflaeche");
    private TextField txtPreisHeizungGlatt = new TextField();
    private Label lblHeizungGlattEuro = new Label("Euro");
    private CheckBox chckBxHeizungGlatt = new CheckBox();

    // 5.3
    private Label lblHeizungHandtuch = new Label("Handtuchheizkoerper");
    private TextField txtPreisHeizungHandtuch = new TextField();
    private Label lblHeizungHandtuchEuro = new Label("Euro");
    private CheckBox chckBxHeizungHandtuch = new CheckBox();

    // 5.4
    private Label lblFbhOhneDG = new Label("Fussbodenheizung ohne DG");
    private TextField txtPreisFbhOhneDG = new TextField();
    private Label lblFbhOhneDGEuro = new Label("Euro");
    private CheckBox chckBxFbhOhneDG = new CheckBox();

    // 5.5
    private Label lblFbhMitDG = new Label("Fussbodenheizung mit DG");
    private TextField txtPreisFbhMitDG = new TextField();
    private Label lblFbhMitDGEuro = new Label("Euro");
    private CheckBox chckBxFbhMitDG = new CheckBox();
    
    private Button btnCsvExport = new Button("Csv Export");

    public HeizungView(HeizungControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwuensche zu Heizungen");
        this.initKomponenten();
        this.initListener();
    }

    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Heizungen");

        // Row 1
        super.getGridPaneSonderwunsch().add(lblHeizungZusatz, 0, 1);
        super.getGridPaneSonderwunsch().add(txtPreisHeizungZusatz, 1, 1);
        txtPreisHeizungZusatz.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblHeizungZusatzEuro, 2, 1);
        super.getGridPaneSonderwunsch().add(chckBxHeizungZusatz, 3, 1);

        // Row 2
        super.getGridPaneSonderwunsch().add(lblHeizungGlatt, 0, 2);
        super.getGridPaneSonderwunsch().add(txtPreisHeizungGlatt, 1, 2);
        txtPreisHeizungGlatt.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblHeizungGlattEuro, 2, 2);
        super.getGridPaneSonderwunsch().add(chckBxHeizungGlatt, 3, 2);

        // Row 3
        super.getGridPaneSonderwunsch().add(lblHeizungHandtuch, 0, 3);
        super.getGridPaneSonderwunsch().add(txtPreisHeizungHandtuch, 1, 3);
        txtPreisHeizungHandtuch.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblHeizungHandtuchEuro, 2, 3);
        super.getGridPaneSonderwunsch().add(chckBxHeizungHandtuch, 3, 3);

        // Row 4
        super.getGridPaneSonderwunsch().add(lblFbhOhneDG, 0, 4);
        super.getGridPaneSonderwunsch().add(txtPreisFbhOhneDG, 1, 4);
        txtPreisFbhOhneDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblFbhOhneDGEuro, 2, 4);
        super.getGridPaneSonderwunsch().add(chckBxFbhOhneDG, 3, 4);

        // Row 5
        super.getGridPaneSonderwunsch().add(lblFbhMitDG, 0, 5);
        super.getGridPaneSonderwunsch().add(txtPreisFbhMitDG, 1, 5);
        txtPreisFbhMitDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblFbhMitDGEuro, 2, 5);
        super.getGridPaneSonderwunsch().add(chckBxFbhMitDG, 3, 5);
        
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
            if (chckBxHeizungZusatz.isSelected()) total += Integer.parseInt(txtPreisHeizungZusatz.getText());
            if (chckBxHeizungGlatt.isSelected()) total += Integer.parseInt(txtPreisHeizungGlatt.getText());
            if (chckBxHeizungHandtuch.isSelected()) total += Integer.parseInt(txtPreisHeizungHandtuch.getText());
            if (chckBxFbhOhneDG.isSelected()) total += Integer.parseInt(txtPreisFbhOhneDG.getText());
            if (chckBxFbhMitDG.isSelected()) total += Integer.parseInt(txtPreisFbhMitDG.getText());

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
        int[] sel = new int[5];
        sel[0] = chckBxHeizungZusatz.isSelected() ? 1 : 0;
        sel[1] = chckBxHeizungGlatt.isSelected() ? 1 : 0;
        sel[2] = chckBxHeizungHandtuch.isSelected() ? 1 : 0;
        sel[3] = chckBxFbhOhneDG.isSelected() ? 1 : 0;
        sel[4] = chckBxFbhMitDG.isSelected() ? 1 : 0;
        return sel;
    }

    public void setPreise(int[] prices) {
        if (prices.length >= 5) {
            txtPreisHeizungZusatz.setText(String.valueOf(prices[0]));
            txtPreisHeizungGlatt.setText(String.valueOf(prices[1]));
            txtPreisHeizungHandtuch.setText(String.valueOf(prices[2]));
            txtPreisFbhOhneDG.setText(String.valueOf(prices[3]));
            txtPreisFbhMitDG.setText(String.valueOf(prices[4]));
        }
    }

    public void setSelection(int[] selection) {
        if (selection.length >= 5) {
            chckBxHeizungZusatz.setSelected(selection[0] == 1);
            chckBxHeizungGlatt.setSelected(selection[1] == 1);
            chckBxHeizungHandtuch.setSelected(selection[2] == 1);
            chckBxFbhOhneDG.setSelected(selection[3] == 1);
            chckBxFbhMitDG.setSelected(selection[4] == 1);
        }
    }
}
