package model;

import org.junit.jupiter.api.Test;

class NqueenProblemTest {

    //TEST – Tablero 4x4
    @Test
    void nqueenTest4() {
        NqueenProblem nq = new NqueenProblem();
        String result = nq.solveNqueens(4);

        System.out.println("══════ N-Reinas 4x4 ══════");
        System.out.println(result);

        System.out.println("── Pasos ──");
        for (String s : NqueenProblem.steps) {
            System.out.println(s);
        }
    }


    //TEST – Tablero 8x8
    @Test
    void nqueenTest8() {
        NqueenProblem nq = new NqueenProblem();
        String result = nq.solveNqueens(8);

        System.out.println("══════ N-Reinas 8x8 ══════");
        System.out.println(result);

        System.out.println("── Pasos ──");
        for (String s : NqueenProblem.steps) {
            System.out.println(s);
        }
    }
}