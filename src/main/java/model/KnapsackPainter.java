package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Dibuja la mochila en un Canvas usando barras horizontales apiladas,
 * igual a la imagen de referencia del Tab-4_Mochila.
 */
public class KnapsackPainter {

    // Paleta de colores para los ítems (rota cíclicamente)
    private static final Color[] ITEM_COLORS = {
            Color.web("#E74C3C"),   // rojo
            Color.web("#95A5A6"),   // gris
            Color.web("#E8A020"),   // naranja/ámbar
            Color.web("#F1C40F"),   // amarillo
            Color.web("#2ECC71"),   // verde
            Color.web("#AED6F1"),   // azul claro
            Color.web("#BDC3C7"),   // gris claro
            Color.web("#808B96"),   // gris oscuro
            Color.web("#48C9B0"),   // teal
            Color.web("#F0B27A"),   // durazno
            Color.web("#A569BD"),   // morado
            Color.web("#5DADE2"),   // azul
    };

    /**
     * Pinta la mochila con los ítems seleccionados.
     *
     * @param canvas   Canvas fx:id="canvasKnapsack"
     * @param selected Lista de ítems que entran en la mochila
     * @param capacity Capacidad máxima en kg
     */
    public static void paint(Canvas canvas, List<Item> selected, int capacity) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double W = canvas.getWidth();
        double H = canvas.getHeight();

        // Limpiar
        gc.clearRect(0, 0, W, H);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, W, H);

        if (selected == null || selected.isEmpty()) return;

        // ── Dimensiones de la mochila ────────────────────────────────────
        double bagX      = 60;
        double bagY      = 30;
        double bagWidth  = 200;
        double bagHeight = H - 80;
        double cornerR   = 18;

        // Calcular altura total de ítems para escalar dentro de la bolsa
        double innerH    = bagHeight - 20;   // margen interno
        double innerTop  = bagY + 10;

        // Total de peso seleccionado
        double totalWeight = selected.stream().mapToInt(Item::getWeight).sum();
        double scale       = innerH / Math.max(totalWeight, capacity);

        // ── Dibujar ítems de abajo hacia arriba dentro de la mochila ─────
        double currentY = innerTop + innerH; // empezamos desde abajo

        for (int i = 0; i < selected.size(); i++) {
            Item item   = selected.get(i);
            Color color = ITEM_COLORS[i % ITEM_COLORS.length];

            double itemH = item.getWeight() * scale;
            currentY    -= itemH;

            // Rectángulo del ítem (redondeado)
            gc.setFill(color);
            gc.fillRoundRect(bagX + 4, currentY, bagWidth - 8, itemH, 8, 8);

            // Borde del ítem
            gc.setStroke(color.darker());
            gc.setLineWidth(1.5);
            gc.strokeRoundRect(bagX + 4, currentY, bagWidth - 8, itemH, 8, 8);

            // Texto del ítem centrado
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Calibri", FontWeight.BOLD, Math.min(13, itemH * 0.4)));
            gc.setTextAlign(TextAlignment.CENTER);
            String label = item.getName() + " (" + item.getWeight() + "kg)";
            if (itemH >= 16) {
                gc.fillText(label, bagX + bagWidth / 2.0, currentY + itemH / 2.0 + 5);
            }
        }

        // ── Silueta de la mochila (encima de los ítems) ──────────────────
        // Borde exterior
        gc.setStroke(Color.web("#8B5E3C"));   // café
        gc.setLineWidth(4);
        gc.strokeRoundRect(bagX, bagY, bagWidth, bagHeight, cornerR, cornerR);

        // Asa de la mochila
        gc.setStroke(Color.web("#8B5E3C"));
        gc.setLineWidth(5);
        gc.strokeArc(bagX + bagWidth / 2.0 - 30, bagY - 22, 60, 36, 0, 180, javafx.scene.shape.ArcType.OPEN);

        // ── Leyenda de ítems a la derecha ────────────────────────────────
        double legendX = bagX + bagWidth + 30;
        double legendY = bagY + 10;
        double boxSize = 14;
        double lineH   = 24;

        gc.setFont(Font.font("Calibri", FontWeight.BOLD, 13));
        gc.setFill(Color.web("#0D1B4B"));
        gc.setTextAlign(TextAlignment.LEFT);

        for (int i = 0; i < selected.size(); i++) {
            Item item   = selected.get(i);
            Color color = ITEM_COLORS[i % ITEM_COLORS.length];
            double ly   = legendY + i * lineH;

            // Cuadrado de color
            gc.setFill(color);
            gc.fillRoundRect(legendX, ly, boxSize, boxSize, 3, 3);

            // Nombre + ratio
            gc.setFill(Color.web("#0D1B4B"));
            gc.setFont(Font.font("Calibri", 11));
            String txt = item.getName() + "  r=" + String.format("%.2f", item.getRatio());
            gc.fillText(txt, legendX + boxSize + 6, ly + 11);
        }
    }

    /** Limpia el canvas completamente */
    public static void clear(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
