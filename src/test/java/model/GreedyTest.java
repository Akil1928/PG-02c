package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedyTest {
    @Test
    void testCoinChange() {
        String resultado = Greedy.coinChangeString(787);
        System.out.println(resultado);
    }
}
