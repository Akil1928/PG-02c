package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CoinPainter {

    // ── Configuración visual ────────────────────────────────────────────────
    private static final double RADIUS      = 44;   // radio monedas grandes
    private static final double RADIUS_1    = 34;   // radio moneda ₡1 (más pequeña)
    private static final double GAP_H       = 20;   // espacio horizontal entre monedas
    private static final double GAP_V       = 20;   // espacio vertical entre filas
    private static final double MARGIN_X    = 16;
    private static final double MARGIN_Y    = 16;

    // ── Denominaciones en orden descendente ────────────────────────────────
    private static final int[] COINS = {500, 100, 25, 10, 5, 1};

    // ── Color de relleno por moneda ────────────────────────────────────────
    private static Color fillColor(int coin) {
        return switch (coin) {
            case 500 -> Color.web("#D4A017");   // dorado
            case 100 -> Color.web("#B0B0B0");   // plata
            case  25 -> Color.web("#C08040");   // bronce
            case  10 -> Color.web("#B87333");   // cobre
            case   5 -> Color.web("#C07820");   // cobre naranja
            default  -> Color.web("#9E9E9E");   // gris (₡1)
        };
    }

    // ── Color de borde por moneda ──────────────────────────────────────────
    private static Color strokeColor(int coin) {
        return switch (coin) {
            case 500 -> Color.web("#A87800");
            case 100 -> Color.web("#808080");
            case  25 -> Color.web("#8B5A10");
            case  10 -> Color.web("#7A4A10");
            case   5 -> Color.web("#8A5200");
            default  -> Color.web("#616161");
        };
    }

    // ── Color del texto de la denominación ────────────────────────────────
    private static Color textColor(int coin) {
        return switch (coin) {
            case 500 -> Color.web("#3B2A00");
            case 100 -> Color.web("#1A1A1A");
            default  -> Color.web("#2C1200");
        };
    }


    public static void paint(Canvas canvas, int[] amounts) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Limpiar canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double x = MARGIN_X;
        double y = MARGIN_Y;
        double maxRowHeight = 0;

        for (int i = 0; i < COINS.length; i++) {
            int coin     = COINS[i];
            int quantity = amounts[i];

            // Saltamos monedas con cantidad 0
            if (quantity == 0) continue;

            double r    = (coin == 1) ? RADIUS_1 : RADIUS;
            double diam = r * 2;

            // Salto de línea si no cabe
            if (x + diam + MARGIN_X > canvas.getWidth() && x > MARGIN_X) {
                x  = MARGIN_X;
                y += maxRowHeight + GAP_V;
                maxRowHeight = 0;
            }

            drawCoin(gc, x + r, y + r, r, coin, quantity);

            x += diam + GAP_H;
            maxRowHeight = Math.max(maxRowHeight, diam);
        }
    }

    // ── Dibuja un círculo de moneda con badge ──────────────────────────────
    private static void drawCoin(GraphicsContext gc,
                                 double cx, double cy, double r,
                                 int coin, int quantity) {

        // Sombra suave
        gc.setFill(Color.rgb(0, 0, 0, 0.10));
        gc.fillOval(cx - r + 3, cy - r + 4, r * 2, r * 2);

        // Círculo relleno
        gc.setFill(fillColor(coin));
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);

        // Borde
        gc.setStroke(strokeColor(coin));
        gc.setLineWidth(3);
        gc.strokeOval(cx - r, cy - r, r * 2, r * 2);

        // Texto denominación  ₡500 / ₡100 / ...
        gc.setFill(textColor(coin));
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, r * 0.38));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("₡" + coin, cx, cy + r * 0.13);

        // ── Badge rojo con cantidad ──────────────────────────────────────
        String badge   = "x" + quantity;
        double bRadius = 9;
        double bx      = cx + r - bRadius;
        double by      = cy - r - bRadius + 4;

        gc.setFill(Color.web("#E74C3C"));
        gc.fillOval(bx - bRadius, by - bRadius, bRadius * 2 + 6, bRadius * 2);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(badge, bx + 2, by + 4);
    }
}
