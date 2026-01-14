package gui.parkett;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class ParkettView extends BasisView {

    private ParkettControl control;

    // UI Elements
    // 8.1
    private Label lblLandhausEss = new Label("Landhausdielen massiv im Essbereich des EG");
    private TextField txtPreisLandhausEss = new TextField();
    private Label lblLandhausEssEuro = new Label("Euro");
    private CheckBox chckBxLandhausEss = new CheckBox();

    // 8.2
    private Label lblLandhausKueche = new Label("Landhausdielen massiv im Küchenbereich des EG");
    private TextField txtPreisLandhausKueche = new TextField();
    private Label lblLandhausKuecheEuro = new Label("Euro");
    private CheckBox chckBxLandhausKueche = new CheckBox();

    // 8.3
    private Label lblStabEss = new Label("Stäbchenparkett im Essbereich des EG");
    private TextField txtPreisStabEss = new TextField();
    private Label lblStabEssEuro = new Label("Euro");
    private CheckBox chckBxStabEss = new CheckBox();

    // 8.4
    private Label lblStabKueche = new Label("Stäbchenparkett im Küchenbereich des EG");
    private TextField txtPreisStabKueche = new TextField();
    private Label lblStabKuecheEuro = new Label("Euro");
    private CheckBox chckBxStabKueche = new CheckBox();

    // 8.5
    private Label lblLandhausOG = new Label("Landhausdielen massiv im OG");
    private TextField txtPreisLandhausOG = new TextField();
    private Label lblLandhausOGEuro = new Label("Euro");
    private CheckBox chckBxLandhausOG = new CheckBox();

    // 8.6
    private Label lblStabOG = new Label("Stäbchenparkett im OG");
    private TextField txtPreisStabOG = new TextField();
    private Label lblStabOGEuro = new Label("Euro");
    private CheckBox chckBxStabOG = new CheckBox();

    // 8.7
    private Label lblLandhausDG = new Label("Landhausdielen massiv komplett im DG");
    private TextField txtPreisLandhausDG = new TextField();
    private Label lblLandhausDGEuro = new Label("Euro");
    private CheckBox chckBxLandhausDG = new CheckBox();

    // 8.8
    private Label lblLandhausOhneBadDG = new Label("Landhausdielen massiv ohne Badbereich im DG");
    private TextField txtPreisLandhausOhneBadDG = new TextField();
    private Label lblLandhausOhneBadDGEuro = new Label("Euro");
    private CheckBox chckBxLandhausOhneBadDG = new CheckBox();

    // 8.9
    private Label lblStabDG = new Label("Stäbchenparkett im DG komplett im DG");
    private TextField txtPreisStabDG = new TextField();
    private Label lblStabDGEuro = new Label("Euro");
    private CheckBox chckBxStabDG = new CheckBox();

    // 8.10
    private Label lblStabOhneBadDG = new Label("Stäbchenparkett ohne Badbereich im DG");
    private TextField txtPreisStabOhneBadDG = new TextField();
    private Label lblStabOhneBadDGEuro = new Label("Euro");
    private CheckBox chckBxStabOhneBadDG = new CheckBox();

    // Buttons
    private Button btnCsvExport = new Button("Csv Export");

    public ParkettView(ParkettControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwünsche zu Parkett");
        this.initKomponenten();
        this.initListener();
    }

    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Parkett");

        // Use scrollpane if too many items? No, BasisView sets strict size. 
        // 10 items might be tight for 700px height.
        // Let's rely on standard layout for now.

        int row = 1;
        
        // 8.1
        super.getGridPaneSonderwunsch().add(lblLandhausEss, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisLandhausEss, 1, row);
        txtPreisLandhausEss.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblLandhausEssEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxLandhausEss, 3, row);
        row++;

        // 8.2
        super.getGridPaneSonderwunsch().add(lblLandhausKueche, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisLandhausKueche, 1, row);
        txtPreisLandhausKueche.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblLandhausKuecheEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxLandhausKueche, 3, row);
        row++;

        // 8.3
        super.getGridPaneSonderwunsch().add(lblStabEss, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisStabEss, 1, row);
        txtPreisStabEss.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblStabEssEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxStabEss, 3, row);
        row++;

        // 8.4
        super.getGridPaneSonderwunsch().add(lblStabKueche, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisStabKueche, 1, row);
        txtPreisStabKueche.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblStabKuecheEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxStabKueche, 3, row);
        row++;

        // 8.5
        super.getGridPaneSonderwunsch().add(lblLandhausOG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisLandhausOG, 1, row);
        txtPreisLandhausOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblLandhausOGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxLandhausOG, 3, row);
        row++;

        // 8.6
        super.getGridPaneSonderwunsch().add(lblStabOG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisStabOG, 1, row);
        txtPreisStabOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblStabOGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxStabOG, 3, row);
        row++;

        // 8.7
        super.getGridPaneSonderwunsch().add(lblLandhausDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisLandhausDG, 1, row);
        txtPreisLandhausDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblLandhausDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxLandhausDG, 3, row);
        row++;

        // 8.8
        super.getGridPaneSonderwunsch().add(lblLandhausOhneBadDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisLandhausOhneBadDG, 1, row);
        txtPreisLandhausOhneBadDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblLandhausOhneBadDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxLandhausOhneBadDG, 3, row);
        row++;

        // 8.9
        super.getGridPaneSonderwunsch().add(lblStabDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisStabDG, 1, row);
        txtPreisStabDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblStabDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxStabDG, 3, row);
        row++;

        // 8.10
        super.getGridPaneSonderwunsch().add(lblStabOhneBadDG, 0, row);
        super.getGridPaneSonderwunsch().add(txtPreisStabOhneBadDG, 1, row);
        txtPreisStabOhneBadDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblStabOhneBadDGEuro, 2, row);
        super.getGridPaneSonderwunsch().add(chckBxStabOhneBadDG, 3, row);
        row++;

        // Export Button
        super.getGridPaneButtons().add(btnCsvExport, 3, 0);
        btnCsvExport.setMinSize(150, 25);
    }

    public void oeffneParkettView() {
        // Load initial state
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
        if (prices.length >= 10) {
            txtPreisLandhausEss.setText(String.valueOf(prices[0]));
            txtPreisLandhausKueche.setText(String.valueOf(prices[1]));
            txtPreisStabEss.setText(String.valueOf(prices[2]));
            txtPreisStabKueche.setText(String.valueOf(prices[3]));
            txtPreisLandhausOG.setText(String.valueOf(prices[4]));
            txtPreisStabOG.setText(String.valueOf(prices[5]));
            txtPreisLandhausDG.setText(String.valueOf(prices[6]));
            txtPreisLandhausOhneBadDG.setText(String.valueOf(prices[7]));
            txtPreisStabDG.setText(String.valueOf(prices[8]));
            txtPreisStabOhneBadDG.setText(String.valueOf(prices[9]));
        }
    }

    @Override
    protected void berechneUndZeigePreisSonderwuensche() {
        zeigeEinzelPreise();

        // Calculate total
        int gesamtPreis = 0;
        if (chckBxLandhausEss.isSelected()) gesamtPreis += Integer.parseInt(txtPreisLandhausEss.getText());
        if (chckBxLandhausKueche.isSelected()) gesamtPreis += Integer.parseInt(txtPreisLandhausKueche.getText());
        if (chckBxStabEss.isSelected()) gesamtPreis += Integer.parseInt(txtPreisStabEss.getText());
        if (chckBxStabKueche.isSelected()) gesamtPreis += Integer.parseInt(txtPreisStabKueche.getText());
        if (chckBxLandhausOG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisLandhausOG.getText());
        if (chckBxStabOG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisStabOG.getText());
        if (chckBxLandhausDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisLandhausDG.getText());
        if (chckBxLandhausOhneBadDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisLandhausOhneBadDG.getText());
        if (chckBxStabDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisStabDG.getText());
        if (chckBxStabOhneBadDG.isSelected()) gesamtPreis += Integer.parseInt(txtPreisStabOhneBadDG.getText());

        int[] selection = new int[10];
        selection[0] = chckBxLandhausEss.isSelected() ? 1 : 0;
        selection[1] = chckBxLandhausKueche.isSelected() ? 1 : 0;
        selection[2] = chckBxStabEss.isSelected() ? 1 : 0;
        selection[3] = chckBxStabKueche.isSelected() ? 1 : 0;
        selection[4] = chckBxLandhausOG.isSelected() ? 1 : 0;
        selection[5] = chckBxStabOG.isSelected() ? 1 : 0;
        selection[6] = chckBxLandhausDG.isSelected() ? 1 : 0;
        selection[7] = chckBxLandhausOhneBadDG.isSelected() ? 1 : 0;
        selection[8] = chckBxStabDG.isSelected() ? 1 : 0;
        selection[9] = chckBxStabOhneBadDG.isSelected() ? 1 : 0;

        if (control.pruefeKonstellationSonderwuensche(selection)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Preisberechnung");
            alert.setHeaderText("Gesamtpreis der Sonderwuensche");
            alert.setContentText("Der Gesamtpreis betraegt: " + gesamtPreis + " Euro");
            alert.showAndWait();
        }
    }

    @Override
    protected void speichereSonderwuensche() {
        int[] selection = new int[10];
        selection[0] = chckBxLandhausEss.isSelected() ? 1 : 0;
        selection[1] = chckBxLandhausKueche.isSelected() ? 1 : 0;
        selection[2] = chckBxStabEss.isSelected() ? 1 : 0;
        selection[3] = chckBxStabKueche.isSelected() ? 1 : 0;
        selection[4] = chckBxLandhausOG.isSelected() ? 1 : 0;
        selection[5] = chckBxStabOG.isSelected() ? 1 : 0;
        selection[6] = chckBxLandhausDG.isSelected() ? 1 : 0;
        selection[7] = chckBxLandhausOhneBadDG.isSelected() ? 1 : 0;
        selection[8] = chckBxStabDG.isSelected() ? 1 : 0;
        selection[9] = chckBxStabOhneBadDG.isSelected() ? 1 : 0;

        if (control.pruefeKonstellationSonderwuensche(selection)) {
            control.speichereSonderwuensche(selection);
        }
    }

    private void setSelection(int[] selection) {
        if (selection.length >= 10) {
            chckBxLandhausEss.setSelected(selection[0] == 1);
            chckBxLandhausKueche.setSelected(selection[1] == 1);
            chckBxStabEss.setSelected(selection[2] == 1);
            chckBxStabKueche.setSelected(selection[3] == 1);
            chckBxLandhausOG.setSelected(selection[4] == 1);
            chckBxStabOG.setSelected(selection[5] == 1);
            chckBxLandhausDG.setSelected(selection[6] == 1);
            chckBxLandhausOhneBadDG.setSelected(selection[7] == 1);
            chckBxStabDG.setSelected(selection[8] == 1);
            chckBxStabOhneBadDG.setSelected(selection[9] == 1);
        }
    }
}
