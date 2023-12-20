package de.arukone;

import org.junit.jupiter.api.Test;

class GridSolverTest {

    @Test
    void solve() {
        for (int x = 0; x < 10; x++) {
            Grid grid = new Grid(4);
            System.out.println(grid.getStringRepresentation());
        }
    }
}