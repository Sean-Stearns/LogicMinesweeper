package src;
import java.util.*;

public class MinesweeperSolver {
    Set<MinesweeperBoard> boards;

    public MinesweeperSolver(MinesweeperBoard board) {
        boards = new HashSet<>();
        solve(board);
    }
    public ArrayList<int[]> getNeighbors(MinesweeperBoard board, int x, int y){
        ArrayList<int[]> neighbors = new ArrayList<>();
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < board.getWidth() && ny >= 0 && ny < board.getHeight() && !board.getRevealed()[nx][ny]) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        
        return neighbors;
    }
    public ArrayList<int[]> getUnmarkUnsatNeighbors(MinesweeperBoard board, int x, int y){
        ArrayList<int[]> neighbors = new ArrayList<>();
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < board.getWidth() && ny >= 0 && ny < board.getHeight() && board.getRevealed()[nx][ny] && numLeft(board,nx,ny) != 0) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        
        return neighbors;
    }
    public ArrayList<int[]> getUnflagNeighbors(MinesweeperBoard board, int x, int y){
        ArrayList<int[]> neighbors = new ArrayList<>();
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < board.getWidth() && ny >= 0 && ny < board.getHeight() && !board.getFlagged()[nx][ny] && !board.getRevealed()[nx][ny]) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        
        return neighbors;
    }
    public ArrayList<int[]> getFlagNeighbors(MinesweeperBoard board, int x, int y){
        ArrayList<int[]> neighbors = new ArrayList<>();
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < board.getWidth() && ny >= 0 && ny < board.getHeight() && board.getFlagged()[nx][ny]) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        
        return neighbors;
    }
    public void displaySolutions() {
        for (MinesweeperBoard board : boards) {
            board.displayBoard();
            System.out.println();
        }
    }

    public int numLeft(MinesweeperBoard board, int x, int y){
        ArrayList<int[]> n = getFlagNeighbors(board, x, y);
        return board.getBoard()[x][y]-n.size();
    }

    static int fact(int number) {  
        int f = 1;  
        int j = 1;  
        while(j <= number) {  
           f = f * j;  
           j++;  
        }  
        return f;  
    }
    static void combinationDriver(ArrayList<int[]> arr, int n, int r, ArrayList<ArrayList<int[]>> combos) {
        ArrayList<int[]> data = new ArrayList<>();
        combinationUtil(arr, n, r, 0, data, 0, combos);
    }
    static void combinationUtil(ArrayList<int[]> arr, int n, int r, int index, ArrayList<int[]> data, int i, ArrayList<ArrayList<int[]>> combos){
        if (index == r) {
            ArrayList<int[]> combo = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                combo.add(data.get(j));
            }
            combos.add(combo);
            return;
        }
        if (i >= n)
        return;
        if (data.size() > index){
            data.set(index,arr.get(i));}
        else{
            data.add(arr.get(i));
        }
        combinationUtil(arr, n, r, index+1, data, i+1, combos);
        combinationUtil(arr, n, r, index, data, i+1, combos);
    }
    public static double[][] gaussianElimination(double[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        for (int row = 0; row < numRows; row++) {
            int pivot = row;
            while (pivot < numCols && matrix[row][pivot] == 0) {
                pivot++;
            }
            if (pivot == numCols) {
                continue;
            }
            if (pivot != row) {
                if (pivot < numRows) {
                    double[] temp = matrix[row];
                    matrix[row] = matrix[pivot];
                    matrix[pivot] = temp;
                }else {
                    continue;
                }
            }
            double divisor = matrix[row][row];
            for (int col = row; col < numCols; col++) {
                matrix[row][col] /= divisor;
            }
            for (int i = row + 1; i < numRows; i++) {
                double factor = matrix[i][row];
                for (int j = row; j < numCols; j++) {
                    matrix[i][j] -= factor * matrix[row][j];
                }
            }
        }

        return matrix;
    }
    public static double[] coeffSolution(double[][] rows) {
        int cols = rows[0].length - 1;
        double[] solution = new double[cols];

        for (double[] row : rows) {
            double lower = 0;
            double upper = 0;

            for (int i = 0; i < cols; i++) {
                if (row[i] == 1) {
                    lower++;
                    upper++;
                } else if (row[i] == -1) {
                    lower--;
                    upper--;
                }
            }
            if (row[cols] == lower) {
                for (int i = 0; i < cols; i++) {
                    if (row[i] == 1) {
                        solution[i] = 1;
                    }
                }
            } else if (row[cols] == upper) {
                for (int i = 0; i < cols; i++) {
                    if (row[i] == -1) {
                        solution[i] = 0;
                    }
                }
            }
        }
        return solution;
    }
    public void solve(MinesweeperBoard board) {
        int x = (int)board.getWidth()/2;
        int y = (int)board.getHeight()/2;
        ArrayList<int[]> neighbors = getNeighbors(board, x, y);
        int g = 1;
        while (board.getBoard()[x][y] == -1){
            System.out.println("Guess "+g+" was a bomb, trying again");
            x = neighbors.get(g-1)[0];
            y = neighbors.get(g-1)[1];
            g++;
        }
        board.uncoverCell(x,y);
        solver(board,x,y);
    }
    public void solver(MinesweeperBoard board, int x, int y){
        if (board.getBoard()[x][y] == -1){ return; }
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (board.getRevealed()[i][j] && board.getBoard()[i][j] == -1){
                    return;
                }
            }
        }
        int bombs = 0;
        for (boolean[] row: board.getFlagged()){
            for (boolean bomb: row){
                if (bomb){
                    bombs++;
                }
            }
        }
        if (bombs > board.getNumMines()){return;}
        if (board.anyLeft().size()==0 && bombs == board.getNumMines()){
            boards.add(board); 
            return;
        }
        ArrayList<int[]> neighbors = getNeighbors(board,x,y);
        int c = neighbors.size();
        if (board.getBoard()[x][y] == 0){
            board.satisfyCell(x,y);
            for (int[] neighbor : neighbors){
                int w = neighbor[0];
                int h = neighbor[1];
                if (!board.getFlagged()[w][h]){
                    board.uncoverCell(w, h);
                    solver(board, w, h);
                }
            }

        }
        if (board.getBoard()[x][y] == c){
            for (int[] neighbor : neighbors){
                int w = neighbor[0];
                int h = neighbor[1];
                board.flagCell(w, h);
                //solver(board, w, h);
            }
        }
        int flag = 0;
        for (int[] neighbor : neighbors){
            int w = neighbor[0];
            int h = neighbor[1];
            if (board.getFlagged()[w][h]){
                flag++;
            }
        }
        if (board.getBoard()[x][y] - flag > 0){
            ArrayList<int[]> tanNeigh = getUnmarkUnsatNeighbors(board,x,y);
            int m = 2;
            ArrayList<ArrayList<ArrayList<Integer>>> coeffs = new ArrayList<>();
            HashSet<ArrayList<Integer>> cells = new HashSet<>();
            double[] needs = new double[2];
            int s = 0;
            for (int[] tanN : tanNeigh){
                int ne = numLeft(board,tanN[0],tanN[1]);
                ArrayList<ArrayList<Integer>> unflag = new ArrayList<>();
                ArrayList<int[]> unflagNeigh = getUnflagNeighbors(board,tanN[0],tanN[1]);
                for (int[] n : unflagNeigh){
                    ArrayList<Integer> cell = new ArrayList<>();
                    
                    cell.add(n[0]);
                    cell.add(n[1]);
                    unflag.add(cell);
                    cells.add(cell);
                }
                coeffs.add(unflag);
                needs[s%2] = ne;
                s++;
                ne = numLeft(board,x,y);
                unflag = new ArrayList<>();
                unflagNeigh = getUnflagNeighbors(board,x,y);
                for (int[] n : unflagNeigh){
                    ArrayList<Integer> cell = new ArrayList<>();
                    
                    cell.add(n[0]);
                    cell.add(n[1]);
                    unflag.add(cell);
                    cells.add(cell);
                }
                coeffs.add(unflag);
                needs[s%2] = ne;
                s++;

                boolean singular = false;
                double[][] matrix = new double[m][cells.size()+1];
                for (int i = 0; i < m; i++) {
                    int j = 0;
                    boolean foundNonZero = false;
                    for (ArrayList<Integer> r : cells) {
                        if (coeffs.get(i).contains(r)) {
                            matrix[i][j] = 1;
                            if (!foundNonZero && j == i) {
                                foundNonZero = true;
                            }
                        } else {
                            matrix[i][j] = 0;
                        }
                        j++;
                    }
                    if (!foundNonZero) {
                        boolean foundSwap = false;
                        for (int k = i + 1; k < m; k++) {
                            if (matrix[k][i] != 0) {
                                double[] temp = matrix[i];
                                matrix[i] = matrix[k];
                                matrix[k] = temp;
                                foundSwap = true;
                                break;
                            }
                        }
                        if (!foundSwap) {
                            singular = true;
                            break;
                        }
                    }
                }
                if (!singular){
                    for (int i = 0; i < matrix.length; i++) {
                        matrix[i][cells.size()] = needs[i];
                    }
                    double [][] gaussMat = gaussianElimination(matrix);
                    double[] solution = coeffSolution(gaussMat);
                    int i = 0;
                    for (ArrayList<Integer> r: cells){
                        if(solution[i] == 1){
                            board.flagCell(r.get(0),r.get(1));
                        }
                        else {
                            board.uncoverCell(r.get(0),r.get(1));
                        }
                    }
                }
            }
        }
        if (board.getBoard()[x][y] < flag){
            return;
        }
        if (board.getBoard()[x][y] == flag){
            board.satisfyCell(x,y);
            for (int[] neighbor : neighbors){
                int w = neighbor[0];
                int h = neighbor[1];
                if (!board.getFlagged()[w][h]){
                    board.uncoverCell(w, h);
                    solver(board, w, h);
                }
            }
        }
        if(board.getBoard()[x][y] - flag > 0){
            ArrayList<ArrayList<int[]>> combos = new ArrayList<>();
            ArrayList<int[]> neighborNoFlag = new ArrayList<>();
            ArrayList<int[]> neighborFlag = new ArrayList<>();
            for (int[] neighbor : neighbors){
                if (!board.getFlagged()[neighbor[0]][neighbor[1]]){
                    neighborNoFlag.add(neighbor);
                }
            }
            for (int[] neighbor : neighbors){
                if (board.getFlagged()[neighbor[0]][neighbor[1]]){
                    neighborFlag.add(neighbor);
                }
            }
            combinationDriver(neighborNoFlag, neighborNoFlag.size(), board.getBoard()[x][y] - flag, combos);
            for (ArrayList<int[]> combo : combos){
                MinesweeperBoard tempBoard = new MinesweeperBoard(board);
                for (int i = 0; i < combo.size(); i++){
                    int w = combo.get(i)[0];
                    tempBoard.flagCell(w, combo.get(i)[1]);
                }
                for (int[] neighbor: neighbors) {
                    int w = neighbor[0];
                    int h = neighbor[1];
                    if (!tempBoard.getFlagged()[w][h]){
                        tempBoard.uncoverCell(w, h);
                        solver(tempBoard, w, h);
                    }
                }
            }
        }
    }
}
