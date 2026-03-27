package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SudokuProblemTest {

    //GENERADOR DE TABLERO INICIAL ALEATORIO VÁLIDO
    private int[][] generateBoard() {
        int[][] board = new int[9][9];

        //Llenar diagonal de bloques 3x3 (son independientes entre sí)
        for (int block = 0; block < 9; block += 3) {
            fillBlock(board, block, block);
        }

        return board;
    }

    private void fillBlock(int[][] board, int startRow, int startCol) {
        List<Integer> nums = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
        Collections.shuffle(nums);

        int idx = 0;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                board[i][j] = nums.get(idx++);
            }
        }
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }


    //TEST – Sudoku 9x9
    @Test
    void sudokuTest() {
        SudokuProblem sp = new SudokuProblem();

        int[][] board    = generateBoard();
        int[][] initial  = copyBoard(board);

        System.out.println("══════ Tablero Inicial ══════");
        System.out.println(sp.printBoard(initial));

        String result = sp.solveSudoku(board);

        System.out.println("══════ Solución Final ══════");
        System.out.println(result);

        System.out.println("── Pasos (primeros 20) ──");
        SudokuProblem.steps.stream()
                .limit(20)
                .forEach(System.out::println);
        System.out.println("... Total de pasos: " + SudokuProblem.steps.size());
    }
}