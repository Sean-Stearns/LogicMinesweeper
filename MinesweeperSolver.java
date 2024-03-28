import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class MinesweeperSolver {
    private int[][] board;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private int width;
    private int height;
    private int numMines;
    
    public MinesweeperSolver(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        this.board = new int[width][height];
        this.revealed = new boolean[width][height];
        this.flagged = new boolean[width][height];
        initializeBoard();
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
    public List<int[]> getNeighbors(int x, int y, int[] count){
        List<int[]> neighbors = new ArrayList<>();
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    neighbors.add(new int[]{nx, ny});
                    count[0]++;
                }
            }
        }
        
        return neighbors;
    }
    public void uncoverCell(int x, int y) {
        revealed[x][y] = true;
    }
    
    public void flagCell(int x, int y) {
        flagged[x][y] = true;
    }

    private boolean isMine(int x, int y) {
        return (board[x][y] == -1);
    }
    
    public void solve() {
        uncoverCell(height/2,width/2); //uncover cell in center of board and pray it not a bomb
        if(isMine(height/2,width/2)) {
            System.out.println("Failed on first guess");
        }
        else{
            solver(height/2,width/2);
        }

    }
    public void solver(int x, int y){
        int[] count = {0};
        getNeighbors(x,y,count);
        int c = count[0];
    }
    
    public static void main(String[] args) {
        MinesweeperSolver solver = new MinesweeperSolver(10, 10, 10);
        solver.solve();
        solver.displayBoard();
    }
}
