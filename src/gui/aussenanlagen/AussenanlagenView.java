package gui.aussenanlagen;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AussenanlagenView extends BasisView {

    private AussenanlagenControl control;

    // UI Elements
    // 9.1
    private Label lblAbstellraum = new Label("Abstellraum auf der Terrasse des EG");
    private TextField txtPreisAbstellraum = new TextField();
    private Label lblAbstellraumEuro = new Label("Euro");
    private CheckBox chckBxAbstellraum = new CheckBox();

    // 9.2
    private Label lblVorbMarkiseEG = new Label("Vorbereitung für elektrische Antriebe Markise EG");
    private TextField txtPreisVorbMarkiseEG = new TextField();
    private Label lblVorbMarkiseEGEuro = new Label("Euro");
    private CheckBox chckBxVorbMarkiseEG = new CheckBox();

    // 9.3
    private Label lblVorbMarkiseDG = new Label("Vorbereitung für elektrische Antriebe Markise DG");
    private TextField txtPreisVorbMarkiseDG = new TextField();
    private Label lblVorbMarkiseDGEuro = new Label("Euro");
    private CheckBox chckBxVorbMarkiseDG = new CheckBox();

    // 9.4
    private Label lblMarkiseEG = new Label("Elektrische Markise EG");
    private TextField txtPreisMarkiseEG = new TextField();
    private Label lblMarkiseEGEuro = new Label("Euro");
    private CheckBox chckBxMarkiseEG = new CheckBox();

    // 9.5
    private Label lblMarkiseDG = new Label("Elektrische Markise DG");
    private TextField txtPreisMarkiseDG = new TextField();
    private Label lblMarkiseDGEuro = new Label("Euro");
    private CheckBox chckBxMarkiseDG = new CheckBox();

    // 9.6
    private Label lblAntriebGarage = new Label("Elektrischen Antrieb für das Garagentor");
    private TextField txtPreisAntriebGarage = new TextField();
    private Label lblAntriebGarageEuro = new Label("Euro");
    private CheckBox chckBxAntriebGarage = new CheckBox();

    // 9.7
    private Label lblSektionaltor = new Label("Sektionaltor anstatt Schwingtor für die Garage");
    private TextField txtPreisSektionaltor = new TextField();
    private Label lblSektionaltorEuro = new Label("Euro");
    private CheckBox chckBxSektionaltor = new CheckBox();

    // Buttons
    private Button btnCsvExport = new Button("Csv Export");

    public AussenanlagenView(AussenanlagenControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwünsche zu Außenanlagen");
        this.initKomponenten();
        this.initListener();
    }

    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Außenanlagen");

        int row = 1;
        // Row 1
        super.getGridPaneSonderwunsch().add(lblAbstellraum, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisAbstellraum, 1, row);
        txtPreisAbstellraum.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblAbstellraumEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxAbstellraum, 3, row);
        row++;

        // Row 2
        super.getGridPaneSonderwunsch().add(lblVorbMarkiseEG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisVorbMarkiseEG, 1, row);
        txtPreisVorbMarkiseEG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblVorbMarkiseEGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxVorbMarkiseEG, 3, row);
        row++;

        // Row 3
        super.getGridPaneSonderwunsch().add(lblVorbMarkiseDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisVorbMarkiseDG, 1, row);
        txtPreisVorbMarkiseDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblVorbMarkiseDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxVorbMarkiseDG, 3, row);
        row++;

        // Row 4
        super.getGridPaneSonderwunsch().add(lblMarkiseEG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisMarkiseEG, 1, row);
        txtPreisMarkiseEG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblMarkiseEGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxMarkiseEG, 3, row);
        row++;

        // Row 5
        super.getGridPaneSonderwunsch().add(lblMarkiseDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisMarkiseDG, 1, row);
        txtPreisMarkiseDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblMarkiseDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxMarkiseDG, 3, row);
        row++;

        // Row 6
        super.getGridPaneSonderwunsch().add(lblAntriebGarage, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisAntriebGarage, 1, row);
        txtPreisAntriebGarage.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblAntriebGarageEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxAntriebGarage, 3, row);
        row++;

        // Row 7
        super.getGridPaneSonderwunsch().add(lblSektionaltor, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisSektionaltor, 1, row);
        txtPreisSektionaltor.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblSektionaltorEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxSektionaltor, 3, row);

        // Export Button
        super.getGridPaneButtons().add(btnCsvExport, 3, 0);
        btnCsvExport.setMinSize(150, 25);
    }

    public void oeffneAussenanlagenView() {
        zeigeEinzelPreise();
        int[] selection = control.leseSelection();
        setSelection(selection);
        super.oeffneBasisView();
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (btnCsvExport != null) {
            btnCsvExport.setOnAction(aEvent -> {
                control.exportiereCsv();
            });
        }
    }

    private void zeigeEinzelPreise() {
        int[] prices = control.lesePreise();
        if (prices.length >= 7) {
            txtPreisAbstellraum.setText(String.valueOf(prices[0]));
            txtPreisVorbMarkiseEG.setText(String.valueOf(prices[1]));
            txtPreisVorbMarkiseDG.setText(String.valueOf(prices[2]));
            txtPreisMarkiseEG.setText(String.valueOf(prices[3]));
            txtPreisMarkiseDG.setText(String.valueOf(prices[4]));
            txtPreisAntriebGarage.setText(String.valueOf(prices[5]));
            txtPreisSektionaltor.setText(String.valueOf(prices[6]));
        }
    }

    @Override
    protected void berechneUndZeigePreisSonderwuensche() {
        zeigeEinzelPreise();

        int gesamtPreis = 0;
        if (chckBxAbstellraum.isSelected()) gesamtPreis += Integer.parseInt(txtPreisAbstellraum.getText());
        if (chckBxVorbMarkiseEG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorbMarkiseEG.getText());
        if (chckBxVorbMarkiseDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisVorbMarkiseDG.getText());
        if (chckBxMarkiseEG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisMarkiseEG.getText());
        if (chckBxMarkiseDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisMarkiseDG.getText());
        if (chckBxAntriebGarage.isSelected()) gesamtPreis += Integer.parseInt(txtPreisAntriebGarage.getText());
        if (chckBxSektionaltor.isSelected()) gesamtPreis += Integer.parseInt(txtPreisSektionaltor.getText());
        
        int[] selection = new int[7];
        selection[0] = chckBxAbstellraum.isSelected() ? 1 : 0;
        selection[1] = chckBxVorbMarkiseEG.isSelected() ? 1 : 0;
        selection[2] = chckBxVorbMarkiseDG.isSelected() ? 1 : 0;
        selection[3] = chckBxMarkiseEG.isSelected() ? 1 : 0;
        selection[4] = chckBxMarkiseDG.isSelected() ? 1 : 0;
        selection[5] = chckBxAntriebGarage.isSelected() ? 1 : 0;
        selection[6] = chckBxSektionaltor.isSelected() ? 1 : 0;

        if (control.pruefeKonstellationSonderwuensche(selection)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Preisberechnung");
            alert.setHeaderText("Gesamtpreis der Sonderwuensche");
            alert.setContentText("Der Gesamtpreis betraegt: " + gesamtPreis + " Euro");
            alert.showAndWait();
        }
    }
    
    @Override
    protected void speichereSonderwuensche() {
        int[] selection = new int[7];
        selection[0] = chckBxAbstellraum.isSelected() ? 1 : 0;
        selection[1] = chckBxVorbMarkiseEG.isSelected() ? 1 : 0;
        selection[2] = chckBxVorbMarkiseDG.isSelected() ? 1 : 0;
        selection[3] = chckBxMarkiseEG.isSelected() ? 1 : 0;
        selection[4] = chckBxMarkiseDG.isSelected() ? 1 : 0;
        selection[5] = chckBxAntriebGarage.isSelected() ? 1 : 0;
        selection[6] = chckBxSektionaltor.isSelected() ? 1 : 0;
        
        control.speichereSonderwuensche(selection);
    }
    
    private void setSelection(int[] selection) {
        if (selection.length >= 7) {
            chckBxAbstellraum.setSelected(selection[0] == 1);
            chckBxVorbMarkiseEG.setSelected(selection[1] == 1);
            chckBxVorbMarkiseDG.setSelected(selection[2] == 1);
            chckBxMarkiseEG.setSelected(selection[3] == 1);
            chckBxMarkiseDG.setSelected(selection[4] == 1);
            chckBxAntriebGarage.setSelected(selection[5] == 1);
            chckBxSektionaltor.setSelected(selection[6] == 1);
        }
    }
}
