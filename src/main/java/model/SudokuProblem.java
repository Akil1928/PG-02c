package model;

import java.util.ArrayList;
import java.util.List;

public class SudokuProblem {

    public static List<String> steps = new ArrayList<>();

    public String solveSudoku(int[][] board) {
        steps.clear();

        if (solve(board)) {
            return printBoard(board);
        } else {
            return "No existe solución para el sudoku dado.";
        }
    }

    //BACKTRACKING RECURSIVO

    private boolean solve(int[][] board) {
        int[] emptyCell = findEmptyCell(board);

        //Caso base: no hay celdas vacías = solución encontrada
        if (emptyCell == null) return true;

        int row = emptyCell[0];
        int col = emptyCell[1];

        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                steps.add("Colocando " + num + " en [" + row + "," + col + "]");

                //Backtracking: intentar con siguiente celda vacía
                if (solve(board)) return true;

                //Backtracking: no funcionó, remover
                board[row][col] = 0;
                steps.add("Backtracking --> removiendo de [" + row + "," + col + "]");
            }
        }

        return false;
    }

    //ENCUENTRA LA SIGUIENTE CELDA VACÍA
    private int[] findEmptyCell(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) return new int[]{row, col};
            }
        }
        return null;
    }


    //VERIFICA SI ES SEGURO COLOCAR UN NÚMERO
    private boolean isSafe(int[][] board, int row, int col, int num) {
        // Revisar fila y columna
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) return false;
            if (board[i][col] == num) return false;
        }

        //Revisar subcuadro 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }

        return true;
    }

    //IMPRIME EL TABLERO
    public String printBoard(int[][] board) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result.append(board[i][j]).append(" ");
                if ((j + 1) % 3 == 0 && j != 8) result.append("| ");
            }
            result.append("\n");

            if ((i + 1) % 3 == 0 && i != 8) result.append("---------------------\n");
        }

        return result.toString();
    }
}