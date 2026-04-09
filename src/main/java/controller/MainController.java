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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.ArrayPainter;
import model.CoinPainter;
import model.Greedy;
import model.NqueenProblem;
import model.SearchEngine;
import model.SearchResult;

import java.net.URL;
import java.util.*;

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
    private Timeline queenTimeline; //para controlar la animación de las reinas
    private int[] binArray;
    private SearchResult binResult;

    @FXML
    private TableColumn<Greedy.Coin, Integer> colMonto;

    @FXML
    private Canvas canvasBin;

    @FXML
    private Button btnLimpiarMonedas;

    @FXML
    private TableColumn<Greedy.Coin, Integer> colMoneda;

    @FXML
    private Button btnBinReset;

    @FXML
    private Label lblBinComplexity;

    @FXML
    private TableColumn<Greedy.Coin, Integer> colCantidad;

    @FXML
    private TableColumn<Greedy.Coin, Integer> colRestante;

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

    @FXML
    private Button btnQueenClear;

    @FXML
    private ToggleButton toggle8x8;

    @FXML
    private Label lblQueenCalls;

    @FXML
    private Slider sliderQueenSpeed;

    @FXML
    private ListView listQueenSteps;

    @FXML
    private StackPane stackPaneBoard;

    @FXML
    private Button btnQueenStop;

    @FXML
    private ToggleButton toggle4x4;

    @FXML
    private Button btnQueenAnimate;

    @FXML
    private GridPane gridBoard;

    @FXML
    private Button btnQueenResolve;

    private final NqueenProblem nQueenSolver = new NqueenProblem();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupBinTab(); //TAB 1 - BINARIA
        setupCoinTab(); //TAB 2 - MONEDA
        setupQueenTab(); //TAB 3 - NQUEEN
    }

    private void setupQueenTab() { // TAB 3 NQueen
        //agrupar toggles para que solo uno esté activo
        ToggleGroup group = new ToggleGroup();
        toggle4x4.setToggleGroup(group);
        toggle8x8.setToggleGroup(group);

        //configuración inicial del Slider
        sliderQueenSpeed.setMin(1);
        sliderQueenSpeed.setMax(5);
        sliderQueenSpeed.setMajorTickUnit(1);
        sliderQueenSpeed.setMinorTickCount(0);
        sliderQueenSpeed.setSnapToTicks(true);
        sliderQueenSpeed.setShowTickLabels(true);
        sliderQueenSpeed.setLabelFormatter(new javafx.util.StringConverter<Double>() {
            @Override //Esto es para mostrar en el slider que velocidad queremos
            public String toString(Double n) {
                if (n == 1) return "Muy lento";
                if (n == 2) return "Lento";
                if (n == 3) return "Normal";
                if (n == 4) return "Rápido";
                return "Muy rápido";
            }
            @Override
            public Double fromString(String s) { return 0.0; }
        });

        //botón Resolver
        btnQueenResolve.setOnAction(e -> {
            if (queenTimeline != null) queenTimeline.stop(); //detener animación si hay una activa
            int n = toggle4x4.isSelected() ? 4 : 8;
            drawEmptyBoard(n);
            nQueenSolver.solveNqueens(n);
            listQueenSteps.setItems(FXCollections.observableArrayList(NqueenProblem.steps));
            lblQueenCalls.setText(String.valueOf(NqueenProblem.steps.size()));

            //pintar el resultado final de una vez
        });

        //botón Animar
        btnQueenAnimate.setOnAction(e -> animateNQueens());

        //BOTÓN DETENER
        btnQueenStop.setOnAction(e -> {
            if (queenTimeline != null) {
                queenTimeline.stop();
                listQueenSteps.getItems().add("Animación detenida por el usuario");
                btnQueenStop.setDisable(true);
                btnQueenAnimate.setDisable(false);
            }
        });

        //Botón Limpiar
        btnQueenClear.setOnAction(e -> {
            if (queenTimeline != null) queenTimeline.stop(); //IMPORTANTE:detener antes de limpiar
            gridBoard.getChildren().clear();
            listQueenSteps.getItems().clear();
            lblQueenCalls.setText("0");
            btnQueenStop.setDisable(true);
            btnQueenAnimate.setDisable(false);
        });
    }

    private void animateNQueens() {
        //si ya hay una animación corriendo, la detenemos
        if (queenTimeline != null) {
            queenTimeline.stop();
        }

        int n = toggle4x4.isSelected() ? 4 : 8;
        drawEmptyBoard(n);
        nQueenSolver.solveNqueens(n);

        List<String> allSteps = new ArrayList<>(NqueenProblem.steps);
        listQueenSteps.getItems().clear();

        queenTimeline = new Timeline(); //usamos la variable global
        double speed = 1100 - (sliderQueenSpeed.getValue() * 100);

        for (int i = 0; i < allSteps.size(); i++) {
            final int index = i;
            final String stepDescription = allSteps.get(i);

            javafx.animation.KeyFrame frame = new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(speed * (i + 1)),
                    e -> {
                        processStep(stepDescription, n);
                        listQueenSteps.getItems().add(stepDescription);
                        listQueenSteps.scrollTo(listQueenSteps.getItems().size() - 1);
                        lblQueenCalls.setText(String.valueOf(index + 1));
                    }
            );
            queenTimeline.getKeyFrames().add(frame);
        }

        //al finalizar la animación, podemos limpiar la variable o deshabilitar botones
        queenTimeline.setOnFinished(e -> btnQueenStop.setDisable(true));

        queenTimeline.play();
        btnQueenStop.setDisable(false); //habilitamos el botón de detener
    }

    private void processStep(String step, int n) {
        //ajustamos las coordenadas para que coincidan con el desplazamiento del índice (j+1, i+1)
        if (step.contains("[")) {
            String coords = step.substring(step.indexOf("[") + 1, step.indexOf("]"));
            String[] parts = coords.split(",");
            int row = Integer.parseInt(parts[0]) + 1; //+1 por el encabezado
            int col = Integer.parseInt(parts[1]) + 1;

            if (step.contains("colocada")) {
                addQueenToGrid(row, col, n);
                String log = "\uD83D\uDC51 [" + String.format("%04d", listQueenSteps.getItems().size() + 1) + "] Colocar reina en fila " + (row-1) + ", col " + (col-1);
                listQueenSteps.getItems().add(log);
            } else {
                removeQueenFromGrid(row, col);
                String log = "❌ [" + String.format("%04d", listQueenSteps.getItems().size() + 1) + "] Conflicto / Retroceder de " + (row-1) + "," + (col-1);
                listQueenSteps.getItems().add(log);
            }
        }
    }

    private void drawEmptyBoard(int n) {
        gridBoard.getChildren().clear();
        gridBoard.getRowConstraints().clear();
        gridBoard.getColumnConstraints().clear();

        double cellSize = 350.0 / n;

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 && j == 0) continue;//esquina superior izquierda vacía

                //encabezados de Columnas (Eje X)
                if (i == 0) {
                    Label lbl = new Label(String.valueOf(j - 1));
                    lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #8896A5; -fx-font-size: 14px;");
                    GridPane.setHalignment(lbl, javafx.geometry.HPos.CENTER);
                    gridBoard.add(lbl, j, 0);
                }
                //encabezados de Filas (Eje Y)
                else if (j == 0) {
                    Label lbl = new Label(String.valueOf(i - 1));
                    lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #8896A5; -fx-font-size: 14px;");
                    GridPane.setValignment(lbl, javafx.geometry.VPos.CENTER);
                    gridBoard.add(lbl, 0, i);
                }
                //celdas del Tablero
                else {
                    javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(cellSize, cellSize);
                    rect.setFill((i + j) % 2 == 0 ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.web("#DDE4ED"));
                    gridBoard.add(rect, j, i);
                }
            }
        }
    }

    private void addQueenToGrid(int row, int col, int n) {
        double size = 400.0 / n;
        //creamos un círculo verde con una corona
        javafx.scene.layout.StackPane queenContainer = new javafx.scene.layout.StackPane();
        queenContainer.setId("queen-" + row + "-" + col); //ID para poder borrarla luego

        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(size * 0.35);
        circle.setFill(javafx.scene.paint.Color.web("#1A8C7B")); // Verde turquesa de tu imagen

        Label crown = new Label("\uD83D\uDC51");
        crown.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        queenContainer.getChildren().addAll(circle, crown);
        gridBoard.add(queenContainer, col, row);
    }

    private void removeQueenFromGrid(int row, int col) {
        //buscamos el contenedor por su ID y lo removemos
        gridBoard.getChildren().removeIf(node ->
                ("queen-" + row + "-" + col).equals(node.getId())
        );
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
        int[] COINS = {500, 100, 25, 10, 5, 1};
        int[] amounts = new int[COINS.length]; // cantidad de cada moneda

        // ── Llenar TableView y calcular cantidades ────────────────────────────
        tableViewCoin.getItems().clear();
        int remaining = monto;

        for (int i = 0; i < COINS.length; i++) {
            int coin = COINS[i];
            int quantity = remaining / coin;
            if (quantity > 0) {
                amounts[i] = quantity;
                remaining %= coin;
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