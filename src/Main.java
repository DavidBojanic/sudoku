import java.util.*;

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int k = 40; // number of cells to be removed
        long startTime = System.nanoTime();
        int[][] grid = generateGrid(); // complete grid (solution)
        int[][] playableGrid = createPlayableGrid(grid, k);
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1000000.0 + "ms");
        System.out.println("Sudoku grid:");
        printGrid(playableGrid);

        System.out.println("Do you want to see the solution step by step? [y/n]");
        String ans = sc.nextLine();
        if (Objects.equals(ans, "y") || Objects.equals(ans, "Y")) {
            System.out.println("\nSolving the Sudoku:");
            solveSudoku(playableGrid);
            System.out.println("Solved!!!");
        }


    }


    public static int[][] generateGrid() {

        Random rnd = new Random();

        ArrayList<Integer> numbers129 = new ArrayList<>(); // numbers 1...9
        for (int i = 1; i < 10; i++) {
            numbers129.add(i);
        }
        ArrayList<Integer> numbers1292 = new ArrayList<>(numbers129); //backup
        Collections.shuffle(numbers129);

        int[][] grid = new int[9][9];

        // fill matrices along diagonal todo: one nicer for loop
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = numbers129.get(0);
                numbers129.remove(0);
            }
        }

        numbers129 = new ArrayList<>(numbers1292);
        Collections.shuffle(numbers129);

        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                grid[i][j] = numbers129.get(0);
                numbers129.remove(0);
            }
        }

        numbers129 = new ArrayList<>(numbers1292);
        Collections.shuffle(numbers129);

        for (int i = 6; i < 9; i++) {
            for (int j = 6; j < 9; j++) {
                grid[i][j] = numbers129.get(0);
                numbers129.remove(0);
            }
        }

        fillRemaining(grid);

        return grid;
    }

    // backtracking to fill the rest
    public static boolean fillRemaining(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {

                    for (int num = 1; num <= 9; num++) {
                        if (validPos(grid, row, col, num)) {
                            grid[row][col] = num;

                            if (fillRemaining(grid)) {
                                return true;
                            }

                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean validPos(int[][] grid, int i, int j, int candidate) {
        // check row
        for (int k = 0; k < 9; k++) {
            if (grid[i][k] == candidate) {
                return false;
            }
        }
        // check column
        for (int k = 0; k < 9; k++) {
            if (grid[k][j] == candidate) {
                return false;
            }
        }
        // check subgrid
        int rowStart = (i / 3) * 3;
        int colStart = (j / 3) * 3;
        for (int row = rowStart; row < rowStart + 3; row++) {
            for (int col = colStart; col < colStart + 3; col++) {
                if (grid[row][col] == candidate) {
                    return false;
                }
            }
        }
        return true;
    }



    public static void printGrid(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            for (int j = 0; j < 9; j++) {
                if (j != 0 && j % 3 == 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static int[][] createPlayableGrid(int[][] grid, int removeCells) {
        int[][] playableGrid = copyGrid(grid);
        Random rnd = new Random();
        int emptyCells = 0;

        while (emptyCells < removeCells) {
            int row = rnd.nextInt(9);
            int col = rnd.nextInt(9);

            if (playableGrid[row][col] != 0) {
                int backup = playableGrid[row][col];
                playableGrid[row][col] = 0;


                if (countSolutions(playableGrid) != 1) {
                    playableGrid[row][col] = backup;
                } else {
                    emptyCells++;
                }
            }
        }

        return playableGrid;
    }

    public static int[][] copyGrid(int[][] originalGrid) {
        int[][] copy = new int[originalGrid.length][originalGrid[0].length];
        for (int i = 0; i < originalGrid.length; i++) {
            for (int j = 0; j < originalGrid[i].length; j++) {
                copy[i][j] = originalGrid[i][j];
            }
        }
        return copy;
    }


    public static int countSolutions(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    int solutionCount = 0;

                    for (int num = 1; num < 10; num++) {
                        if (validPos(grid, row, col, num)) {
                            grid[row][col] = num;
                            solutionCount += countSolutions(grid);
                            grid[row][col] = 0;

                            if (solutionCount > 1) {
                                return solutionCount;
                            }
                        }
                    }
                    return solutionCount;
                }
            }
        }
        return 1;
    }
    public static boolean solveSudoku(int[][] grid) { // couldve been all 1 function



        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (validPos(grid, row, col, num)) {
                            grid[row][col] = num;
                            printGrid(grid);

                            try {
                                Thread.sleep(100);
                                if (hasEmptyCells(grid)) {
                                    clearConsole();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            if (solveSudoku(grid)) {
                                return true;
                            }


                            grid[row][col] = 0;
                            printGrid(grid);


                            try {
                                Thread.sleep(100);
                                if (hasEmptyCells(grid)) {
                                    clearConsole();
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return false; // go backtrack
                }
            }
        }
        return true;
    }

    public static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasEmptyCells(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    return true;
                }
            }
        }
        return false;
    }



}
