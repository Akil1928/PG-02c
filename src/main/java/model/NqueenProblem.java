package model;

import java.util.ArrayList;
import java.util.List;

public class NqueenProblem {

    public static List<String> steps = new ArrayList<>();

    public String solveNqueens(int n) {
        String result = "";
        int[][] board = new int[n][n];
        steps.clear();

        if (placeFirstQueen(board)) {
            result += printBoard(board);
        } else {
            result += "No existe solución para un tablero de " + n + "x" + n;
        }

        return result;
    }


    //UBICA LA PRIMERA REINA EN CUALQUIER COLUMNA
    private boolean placeFirstQueen(int[][] board) {
        for (int col = 0; col < board.length; col++) {
            if (isSafe(board, 0, col)) {
                board[0][col] = 1;
                steps.add("Primera reina colocada en [0," + col + "]");

                if (placeQueens(board, 1)) return true;

                // Backtracking
                board[0][col] = 0;
                steps.add("Backtracking --> removiendo reina de [0," + col + "]");
            }
        }
        return false;
    }

    //COLOCA LAS REINAS RESTANTES FILA POR FILA

    private boolean placeQueens(int[][] board, int row) {
        //Caso base: todas las reinas colocadas
        if (row == board.length) return true;

        for (int col = 0; col < board.length; col++) {
            if (isSafe(board, row, col)) {
                board[row][col] = 1;
                steps.add("Reina colocada en [" + row + "," + col + "]");

                // Backtracking: intentar colocar siguiente fila
                if (placeQueens(board, row + 1)) return true;

                // Backtracking: no funcionó, remover
                board[row][col] = 0;
                steps.add("Backtracking --> removiendo reina de [" + row + "," + col + "]");
            }
        }

        return false;
    }


    //VERIFICA SI ES SEGURO COLOCAR UNA REINA

    private boolean isSafe(int[][] board, int row, int col) {
        //Revisar columna
        for (int i = 0; i < board.length; i++) {
            if (board[i][col] == 1) return false;
        }

        //Diagonal superior izquierda
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) return false;
        }

        //Diagonal superior derecha
        for (int i = row, j = col; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 1) return false;
        }

        return true;
    }


    //IMPRIME EL TABLERO
    private String printBoard(int[][] board) {
        String result = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                result += board[i][j] == 1 ? " Q " : " . ";
            }
            result += "\n";
        }
        return result;
    }
}