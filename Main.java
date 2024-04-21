class Main {
    public static void main(String[] args) {
        MinesweeperBoard board = new MinesweeperBoard(10, 10, 9);
        MinesweeperSolver solver = new MinesweeperSolver(board);
        solver.displaySolutions();
    }

}