package src;
import java.util.*;

public class MinesweeperBoard {
    private int[][] board;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean[][] satisfied;
    private int width;
    private int height;
    private int numMines;

    public MinesweeperBoard(MinesweeperBoard board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.numMines = board.getNumMines();
        this.board = new int[board.getWidth()][board.getHeight()];
        copyBoard(this.board, board.getBoard());
        this.revealed = new boolean[board.getWidth()][board.getHeight()];
        copyBoolBoard(this.revealed, board.getRevealed());
        this.flagged = new boolean[board.getWidth()][board.getHeight()];
        copyBoolBoard(this.flagged, board.getFlagged());
        this.satisfied = new boolean[board.getWidth()][board.getHeight()];
        copyBoolBoard(this.satisfied, board.getSatisfied());
    }
    
    public MinesweeperBoard(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        this.board = new int[width][height];
        this.revealed = new boolean[width][height];
        this.flagged = new boolean[width][height];
        this.satisfied = new boolean[width][height];
        initializeBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinesweeperBoard)) return false;
        MinesweeperBoard that = (MinesweeperBoard) o;
        return Arrays.deepEquals(flagged, that.flagged)
        && Arrays.deepEquals(revealed, that.revealed);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(flagged);
        result = 31 * result + Arrays.deepHashCode(revealed);
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumMines() {
        return numMines;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean[][] getRevealed() {
        return revealed;
    }

    public boolean[][] getFlagged() {
        return flagged;
    }

    public boolean[][] getSatisfied() {
        return satisfied;
    }

    public int getValue(int x, int y) {
        return board[x][y];
    }   
    private void copyBoard(int[][] newBoard, int[][] oldBoard) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newBoard[i][j] = oldBoard[i][j];
            }
        }
    }

    private void copyBoolBoard(boolean[][] newBoard, boolean[][] oldBoard) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newBoard[i][j] = oldBoard[i][j];
            }
        }
    }
    
    private void initializeBoard(){
        Random random = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (board[x][y] != -1) {
                board[x][y] = -1;
                minesPlaced++;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != -1) {
                    board[i][j] = countAdjacentMines(i, j);
                }
            }
        }
    }
    
    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(height - 1, y + 1); j++) {
                if (board[i][j] == -1) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public void displayBoard() {
        System.out.print("  ");
        for (int i = 0; i < width; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < width; i++) {
            System.out.print("--");
        }
        System.out.println();
        for (int i = 0; i < height; i++) {
            System.out.print(i + "|");
            for (int j = 0; j < width; j++) {
                if (revealed[j][i]) {
                    if (board[j][i] == -1) {
                        System.out.print("* ");
                    } else {
                        System.out.print(board[j][i] + " ");
                    }
                } else if (flagged[j][i]) {
                    System.out.print("F ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }
    public void uncoverCell(int x, int y) {
        revealed[x][y] = true;
    }
    
    public void flagCell(int x, int y) {
        flagged[x][y] = true;
    }
    public void satisfyCell(int x, int y) {
        satisfied[x][y] = true;
    }

    public ArrayList<int[]> anyLeft() {
        ArrayList<int[]> any = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!revealed[i][j] && !flagged[i][j]){
                    any.add(new int[] {i,j});
                    break;
                }
            }
        }
        return any;
    }
}