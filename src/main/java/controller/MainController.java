package controller;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import model.ArrayPainter;
import model.Greedy;
import model.SearchEngine;
import model.SearchResult;

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
    private TableColumn colMonto;
    @FXML
    private Button btnCambioMonedas;
    @FXML
    private TableView tableMonedas;
    @FXML
    private Canvas canvasBin;
    @FXML
    private Button btnLimpiarMonedas;
    @FXML
    private TableColumn colMoneda;
    @FXML
    private Button btnBinReset;
    @FXML
    private Label lblBinComplexity;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private ListView listMonedasSteps;
    @FXML
    private TableColumn colRestante;
    @FXML
    private Canvas CanvasCoin;
    @FXML
    private Slider sliderCoinAmount;
    @FXML
    private TextField txtCoinValue;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupBinTab(); //TAB 1 - BINARIA
        setupCoinTab(); //TAB 2 - MONEDA
    }

    private void setupCoinTab() {
        sliderCoinAmount.setMin(100);
        sliderCoinAmount.setMax(5000);
        sliderCoinAmount.setValue(787);
        sliderCoinAmount.valueProperty().addListener((o, ov, nv) -> txtCoinValue.setText(String.valueOf(nv.intValue())));
        txtCoinValue.setText(String.valueOf(sliderCoinAmount.getValue()));
        btnCambioMonedas.setOnAction(event -> generateCoinChange());
    }

    private void generateCoinChange() {
        int monto=Integer.parseInt(txtCoinValue.getText());
        //tablleview
        listMonedasSteps.getItems().clear();
        List<String> coinList = Collections.singletonList(Greedy.coinChangeString(monto));
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < coinList.size();i++) {
            items.add(String.format("[%02d] %s", i + 1, coinList.get(i)));

        }
        items.add("Monto total: "+monto+" | Monedas: "+coinList.size());
        listMonedasSteps.setItems(items);
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