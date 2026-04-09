package controller;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import model.ArrayPainter;
import model.Greedy;
import model.SearchEngine;
import model.SearchResult;
import model.CoinPainter;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button btnBinAnimate;

    @FXML
    private Button btnBinGen;

    @FXML
    private Button btnBinSearch;

    @FXML
    private Label lblBinArray;

    @FXML
    private Label lblBinComps;

    @FXML
    private Label lblBinResult;

    @FXML
    private Label lblBinSize;

    @FXML
    private Label lblBinTime;

    @FXML
    private ListView<String> listBinSteps;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private ProgressBar progressBarBin;

    @FXML
    private Slider sliderBinSize;

    @FXML
    private TextField txtBinValue;

    private final SearchEngine searchEngine = new SearchEngine();
    private ArrayPainter arrayPainter = new ArrayPainter();
    private Timeline animation;
    private int[] binArray;
    private SearchResult binResult;
    @FXML
    private TableColumn <Greedy.Coin, Integer>colMonto;
    @FXML
    private Canvas canvasBin;
    @FXML
    private Button btnLimpiarMonedas;
    @FXML
    private TableColumn <Greedy.Coin, Integer> colMoneda;
    @FXML
    private Button btnBinReset;
    @FXML
    private Label lblBinComplexity;
    @FXML
    private TableColumn <Greedy.Coin, Integer> colCantidad;
    @FXML
    private TableColumn <Greedy.Coin, Integer> colRestante;
    @FXML
    private Canvas CanvasCoin;
    @FXML
    private Slider sliderCoinAmount;
    @FXML
    private TextField txtCoinValue;
    @FXML
    private ListView listCoinSteps;
    @FXML
    private Button btnCoinChange;
    @FXML
    private TableView<Greedy.Coin> tableViewCoin;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupBinTab(); //TAB 1 - BINARIA
        setupCoinTab(); //TAB 2 - MONEDA
    }

    //TAB 2 - MONEDA - Atributos del controlador
    private final int[] MONEDAS_CR = {500, 100, 50, 25, 10, 5, 1};


    private void setupCoinTab() {
        colMoneda.setCellValueFactory(new PropertyValueFactory<>("coin"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colRestante.setCellValueFactory(new PropertyValueFactory<>("remaining"));

        sliderCoinAmount.setMin(100);
        sliderCoinAmount.setMax(9999);
        sliderCoinAmount.setValue(3441);
        sliderCoinAmount.setMajorTickUnit(1000);
        sliderCoinAmount.setSnapToTicks(false);
        sliderCoinAmount.valueProperty().addListener((obs, oldVal, newVal) ->
                txtCoinValue.setText(String.valueOf(newVal.intValue()))
        );
        txtCoinValue.setText("3441");

        btnCoinChange.setOnAction(event -> generateCoinChange());

        btnLimpiarMonedas.setOnAction(event -> {
            tableViewCoin.getItems().clear();
            listCoinSteps.getItems().clear();
            txtCoinValue.setText("");
            sliderCoinAmount.setValue(100);
            // Limpiar el canvas
            javafx.scene.canvas.GraphicsContext gc = CanvasCoin.getGraphicsContext2D();
            gc.clearRect(0, 0, CanvasCoin.getWidth(), CanvasCoin.getHeight());
        });
    }


    private void generateCoinChange() {
        int monto;
        try {
            monto = Integer.parseInt(txtCoinValue.getText().trim());
        } catch (NumberFormatException e) {
            return; // si el campo está vacío o inválido, no hacemos nada
        }

        // ── Denominaciones (mismo orden que CoinPainter.COINS) ───────────────
        int[] COINS    = {500, 100, 25, 10, 5, 1};
        int[] amounts  = new int[COINS.length]; // cantidad de cada moneda

        // ── Llenar TableView y calcular cantidades ────────────────────────────
        tableViewCoin.getItems().clear();
        int remaining = monto;

        for (int i = 0; i < COINS.length; i++) {
            int coin     = COINS[i];
            int quantity = remaining / coin;
            if (quantity > 0) {
                amounts[i]  = quantity;
                remaining  %= coin;
                tableViewCoin.getItems().add(
                        new Greedy.Coin(coin, quantity, quantity * coin, remaining)
                );
            }
        }

        // ── Pintar canvas con las monedas ─────────────────────────────────────
        CoinPainter.paint(CanvasCoin, amounts);

        // ── Llenar ListView de pasos ──────────────────────────────────────────
        listCoinSteps.getItems().clear();
        ObservableList<String> items = FXCollections.observableArrayList();

        // Reconstruimos los pasos paso a paso para mostrar cada denominación
        int rem2 = monto;
        int step = 1;
        for (int coin : COINS) {
            int qty = rem2 / coin;
            if (qty > 0) {
                rem2 -= qty * coin;
                items.add(String.format("[%02d] %d x %d = %d (remaining %d)",
                        step++, coin, qty, coin * qty, rem2));
            }
        }
        items.add("Monto Total: " + monto + "  |  Monedas: " +
                tableViewCoin.getItems().size());
        listCoinSteps.setItems(items);
    }


    private void setupBinTab() {
        configSlider(sliderBinSize, 10, 50, 20, lblBinSize);
        btnBinGen.setOnAction(event -> generateBin());
        btnBinSearch.setOnAction(event -> runSearch(false));
        btnBinAnimate.setOnAction(event -> runSearch(true));
    }

    private void runSearch(boolean animate) {
        if (binArray == null) {
            showError(txtBinValue, "Primero genere un arreglo");
            return;
        }
        //tomamos el valor a buscar del textfield
        //SI EL USUARIO NO INDICA ININGUN VALOR SE ASIGNA UNO DE LOS QUE EXISTEN EN EL ARREGLO DE FORMAR RANDOM
        int value;
        try {
            value = Integer.parseInt(txtBinValue.getText());
        } catch (NumberFormatException e) {
            //elegimos un elemento al azar del arreglo
            value = binArray[new Random().nextInt(binArray.length)];
            txtBinValue.setText(String.valueOf(value));
        }
        ;
        binArray = SearchEngine.ensureContains(binArray, value);
        updateArrayLabel(lblBinArray, binArray);
        SearchResult searchResult = searchEngine.binary(binArray, value);
        //llenamos el listview
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < searchResult.steps.size(); i++) {
            SearchResult.Step step = searchResult.steps.get(i);
            items.add(String.format("[%02d] %s", i + 1, step.description));
        }
        listBinSteps.setItems(items);
        updateStats(lblBinResult, lblBinComps, lblBinTime, lblBinComplexity, searchResult);
//agregamos la nimacion del canvas
        if (animate) {
            animateSearch(searchResult, binArray, canvasBin, progressBarBin, listBinSteps);
        } else {
            //pintamos en el canvas
            boolean[] vis = buildVisited(searchResult.steps, binArray.length, searchResult.steps.size());
            SearchResult.Step last = searchResult.steps.isEmpty() ? null
                    : searchResult.steps.getLast();
            arrayPainter.paint(canvasBin, binArray, last, vis, searchResult.foundIndex);
            progressBarBin.setProgress(1.0);

        }

    }

    private void animateSearch(SearchResult searchResult, int[] binArray, Canvas binCanvas, ProgressBar progressBarBin, ListView<String> listBinSteps) {

    }

    private void updateStats(Label lblBinResult, Label lblBinComps, Label lblBinTime, Label lblBinComplex, SearchResult searchResult) {
//adx
        if (searchResult == null) {
            clearStarts(lblBinResult, lblBinComps, lblBinTime, lblBinComplex);
            return;
        }

        if (searchResult.isFound()) {
            lblBinResult.setText("Encontrado en índice: " + searchResult.foundIndex);
            lblBinResult.setStyle("-fx-text-fill: #2ECC71; -fx-font-weight: bold;");
        } else {
            lblBinResult.setText("No encontrado");
            lblBinResult.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
        }


        lblBinComps.setText(String.valueOf(searchResult.comparisons));
        lblBinComps.setStyle("-fx-text-fill: #3498DB;");


        double timeMs = searchResult.nanoTime / 1_000_000.0;
        lblBinTime.setText(String.format("%.4f ms", timeMs));
        lblBinTime.setStyle("-fx-text-fill: #9B59B6;");


        lblBinComplex.setText(searchResult.complexityLabel());
        lblBinComplex.setStyle("-fx-text-fill: #F39C12;");
    }

    private void showError(TextField txt, String msg) {
        txt.setStyle("-fx-border-color: #E74C3C;");
        txt.setPromptText(msg);
        txt.setText("");
    }

    private void generateBin() {
        int size = (int) sliderBinSize.getValue();
        binArray = searchEngine.generateSorted(size, size * 15);
        binResult = null;
        repaintBin();
        updateArrayLabel(lblBinArray, binArray);


    }

    private void repaintBin() {
        if (binResult == null) return;

        SearchResult.Step step = null;
        boolean[] visited = null;
        int found = -1;
        if (binResult != null) {
            step = binResult.steps.isEmpty() ? null : binResult.steps.getLast();
            visited = buildVisited(binResult.steps, binArray.length, binResult.steps.size());
            found = binResult.foundIndex;
            updateArrayLabel(lblBinArray, binArray);

        }
    }

    private void clearStarts(Label... labels) {
        for (Label l : labels) {
            l.setText("-");
            l.setStyle("");
        }
    }

    private boolean[] buildVisited(List<SearchResult.Step> steps, int n, int upTo) {
        boolean[] vis = new boolean[n];
        int limit = Math.min(upTo, steps.size());
        for (int i = 0; i < limit; i++) {
            int idx = steps.get(i).index;
            if (idx >= 0 && idx < n) vis[idx] = true;
        }
        return vis;
    }

    private void updateArrayLabel(Label lbl, int[] arr) {
        if (arr == null || arr.length == 0) {
            lbl.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder("[");
        int show = Math.min(arr.length, 20);
        for (int i = 0; i < show; i++) {
            sb.append(arr[i]);
            if (i < show - 1) sb.append(", ");
        }
        if (arr.length > 20) sb.append(", …");
        sb.append("]  (n=").append(arr.length).append(")");
        lbl.setText(sb.toString());
    }

    private void configSlider(Slider s, int min, int max, int val, Label lbl) {
        s.setMin(min);
        s.setMax(max);
        s.setValue(val);
        s.setMajorTickUnit(5);
        s.setSnapToTicks(false);
        s.valueProperty().addListener((o, ov, nv) -> lbl.setText(String.valueOf(nv.intValue())));
        lbl.setText(String.valueOf(val));
    }
}