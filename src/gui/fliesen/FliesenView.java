package gui.fliesen;

import gui.basis.BasisView;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FliesenView extends BasisView {

    private FliesenControl control;

    // UI Elements
    // 7.1
    private Label lblKeineKueche = new Label("Keine Fliesen im Küchenbereich des EG");
    private TextField txtPreisKeineKueche = new TextField();
    private Label lblKeineKuecheEuro = new Label("Euro");
    private CheckBox chckBxKeineKueche = new CheckBox();

    // 7.2
    private Label lblKeineBadOG = new Label("Keine Fliesen im Bad des OG");
    private TextField txtPreisKeineBadOG = new TextField();
    private Label lblKeineBadOGEuro = new Label("Euro");
    private CheckBox chckBxKeineBadOG = new CheckBox();

    // 7.3
    private Label lblGrossKueche = new Label("Mehrpreis großform. Fliesen Küche EG");
    private TextField txtPreisGrossKueche = new TextField();
    private Label lblGrossKuecheEuro = new Label("Euro");
    private CheckBox chckBxGrossKueche = new CheckBox();

    // 7.4
    private Label lblGrossBadOG = new Label("Mehrpreis großform. Fliesen Bad OG");
    private TextField txtPreisGrossBadOG = new TextField();
    private Label lblGrossBadOGEuro = new Label("Euro");
    private CheckBox chckBxGrossBadOG = new CheckBox();

    // 7.5
    private Label lblBadDG = new Label("Fliesen im Bad des DG");
    private TextField txtPreisBadDG = new TextField();
    private Label lblBadDGEuro = new Label("Euro");
    private CheckBox chckBxBadDG = new CheckBox();

    // 7.6
    private Label lblGrossBadDG = new Label("Mehrpreis großform. Fliesen Bad DG");
    private TextField txtPreisGrossBadDG = new TextField();
    private Label lblGrossBadDGEuro = new Label("Euro");
    private CheckBox chckBxGrossBadDG = new CheckBox();

    // Buttons
    private Button btnCsvExport = new Button("Csv Export");

    public FliesenView(FliesenControl control, Stage stage) {
        super(stage);
        this.control = control;
        stage.setTitle("Sonderwünsche zu Fliesen");
        this.initKomponenten();
        this.initListener();
    }

    @Override
    protected void initKomponenten() {
        super.initKomponenten();
        super.getLblSonderwunsch().setText("Fliesen");

        // Row 1
        super.getGridPaneSonderwunsch().add(lblKeineKueche, 0, 1);
        super.getGridPaneSonderwunsch().add(txtPreisKeineKueche, 1, 1);
        txtPreisKeineKueche.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblKeineKuecheEuro, 2, 1);
        super.getGridPaneSonderwunsch().add(chckBxKeineKueche, 3, 1);

        // Row 2
        super.getGridPaneSonderwunsch().add(lblKeineBadOG, 0, 2);
        super.getGridPaneSonderwunsch().add(txtPreisKeineBadOG, 1, 2);
        txtPreisKeineBadOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblKeineBadOGEuro, 2, 2);
        super.getGridPaneSonderwunsch().add(chckBxKeineBadOG, 3, 2);

        // Row 3
        super.getGridPaneSonderwunsch().add(lblGrossKueche, 0, 3);
        super.getGridPaneSonderwunsch().add(txtPreisGrossKueche, 1, 3);
        txtPreisGrossKueche.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGrossKuecheEuro, 2, 3);
        super.getGridPaneSonderwunsch().add(chckBxGrossKueche, 3, 3);

        // Row 4
        super.getGridPaneSonderwunsch().add(lblGrossBadOG, 0, 4);
        super.getGridPaneSonderwunsch().add(txtPreisGrossBadOG, 1, 4);
        txtPreisGrossBadOG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGrossBadOGEuro, 2, 4);
        super.getGridPaneSonderwunsch().add(chckBxGrossBadOG, 3, 4);

        // Row 5
        super.getGridPaneSonderwunsch().add(lblBadDG, 0, 5);
        super.getGridPaneSonderwunsch().add(txtPreisBadDG, 1, 5);
        txtPreisBadDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblBadDGEuro, 2, 5);
        super.getGridPaneSonderwunsch().add(chckBxBadDG, 3, 5);

        // Row 6
        super.getGridPaneSonderwunsch().add(lblGrossBadDG, 0, 6);
        super.getGridPaneSonderwunsch().add(txtPreisGrossBadDG, 1, 6);
        txtPreisGrossBadDG.setEditable(false);
        super.getGridPaneSonderwunsch().add(lblGrossBadDGEuro, 2, 6);
        super.getGridPaneSonderwunsch().add(chckBxGrossBadDG, 3, 6);

        // Export Button
        super.getGridPaneButtons().add(btnCsvExport, 3, 0);
        btnCsvExport.setMinSize(150, 25);
    }

    public void oeffneFliesenView() {
        // Load initial state
        berechneUndZeigePreisSonderwuensche();
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

    @Override
    protected void berechneUndZeigePreisSonderwuensche() {
        int[] prices = control.lesePreise();
        // Assuming order matches descriptions
        if (prices.length >= 6) {
            txtPreisKeineKueche.setText(String.valueOf(prices[0]));
            txtPreisKeineBadOG.setText(String.valueOf(prices[1]));
            txtPreisGrossKueche.setText(String.valueOf(prices[2]));
            txtPreisGrossBadOG.setText(String.valueOf(prices[3]));
            txtPreisBadDG.setText(String.valueOf(prices[4]));
            txtPreisGrossBadDG.setText(String.valueOf(prices[5]));
        }
    }

    @Override
    protected void speichereSonderwuensche() {
        int[] selection = new int[6];
        selection[0] = chckBxKeineKueche.isSelected() ? 1 : 0;
        selection[1] = chckBxKeineBadOG.isSelected() ? 1 : 0;
        selection[2] = chckBxGrossKueche.isSelected() ? 1 : 0;
        selection[3] = chckBxGrossBadOG.isSelected() ? 1 : 0;
        selection[4] = chckBxBadDG.isSelected() ? 1 : 0;
        selection[5] = chckBxGrossBadDG.isSelected() ? 1 : 0;

        if (control.pruefeKonstellationSonderwuensche(selection)) {
            control.speichereSonderwuensche(selection);
        }
    }

    private void setSelection(int[] selection) {
        if (selection.length >= 6) {
            chckBxKeineKueche.setSelected(selection[0] == 1);
            chckBxKeineBadOG.setSelected(selection[1] == 1);
            chckBxGrossKueche.setSelected(selection[2] == 1);
            chckBxGrossBadOG.setSelected(selection[3] == 1);
            chckBxBadDG.setSelected(selection[4] == 1);
            chckBxGrossBadDG.setSelected(selection[5] == 1);
        }
    }
}
