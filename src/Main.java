package src;
class Main {
    public static void main(String[] args) {
        MinesweeperBoard board = new MinesweeperBoard(10, 10, 8);
        board.displayBoard();
        MineSolver solver = new MineSolver(board);
        solver.displaySolutions();
    }

}