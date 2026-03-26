package controller;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.ArrayPainter;
import model.SearchEngine;
import model.SearchResult;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Canvas binCanvas;

    @FXML
    private Button btnBinAnimate;

    @FXML
    private Button btnBinClear;

    @FXML
    private Button btnBinGen;

    @FXML
    private Button btnBinSearch;

    @FXML
    private Label lblBinArray;

    @FXML
    private Label lblBinComplex;

    @FXML
    private Label lblBinComps;

    @FXML
    private Label lblBinResult;

    @FXML
    private Label lblBinSize;

    @FXML
    private Label lblBinTime;

    @FXML
    private ListView<?> listBinSteps;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupBinTab();
    }

    private void setupBinTab() {
        configSlider(sliderBinSize, 10, 50, 20, lblBinArray);
        btnBinAnimate.setOnAction(event -> generateBin());
    }

    private void generateBin() {
        int size = (int) sliderBinSize.getValue();
        binArray = searchEngine.generateSorted(size, size *15);
        binResult = null;
        repaintBin();

    }

    private void repaintBin() {
        if(binResult == null)return;

        SearchResult.Step step = null;
        boolean[] visited = null;
        int found =-1;
        if(binResult != null){
            step = binResult.steps.isEmpty() ? null : binResult.steps.get(binResult.steps.size() - 1);
            visited = buildVisited(binResult.steps, binArray.length, binResult.steps.size());
            found = binResult.foundIndex;
            updateArrayLabel(lblBinArray, binArray);
            clearStarts(lblBinArray, lblBinComps,lblBinTime,lblBinComplex);
        }
    }

    private void clearStarts(Label... labels) {
        for(Label l : labels){
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
        if (arr == null || arr.length == 0) { lbl.setText(""); return; }
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
        s.setMin(min); s.setMax(max); s.setValue(val);
        s.setMajorTickUnit(5); s.setSnapToTicks(false);
        s.valueProperty().addListener((o, ov, nv) -> lbl.setText(String.valueOf(nv.intValue())));
        lbl.setText(String.valueOf(val));
    }
}
