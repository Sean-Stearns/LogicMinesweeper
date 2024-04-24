package src;
class Main {
    public static void main(String[] args) {
        MinesweeperBoard board = new MinesweeperBoard(9, 9, 10);
        board.displayBoard();
        long startTime = System.nanoTime();
        MinesweeperSolver solver = new MinesweeperSolver(board);
        long endTime = System.nanoTime();
        solver.displaySolutions();
        long elapsedTime = (endTime - startTime);
        System.out.println("Time taken to run: "+((double) elapsedTime / 1_000_000_000)
        +"s");
    }

}