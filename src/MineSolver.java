package src;
import java.util.*;
import java.util.HashMap;
import Jama.Matrix;

public class MineSolver {
    Set<MinesweeperBoard> boards;

    public MineSolver(MinesweeperBoard board) {
        boards = new HashSet<>();
        solve(board);
    }
    public ArrayList<int[]> getNeighbors(MinesweeperBoard board, int x, int y, int[] count){
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
                    count[0]++;
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

        // Print all combinations using temporary array 'data[]'
        combinationUtil(arr, n, r, 0, data, 0, combos);
    }
    static void combinationUtil(ArrayList<int[]> arr, int n, int r, int index, ArrayList<int[]> data, int i, ArrayList<ArrayList<int[]>> combos){
        // Current combination is ready, print it
        if (index == r) {
            ArrayList<int[]> combo = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                combo.add(data.get(j));
            }
            combos.add(combo);
            return;
        }

        // When no more elements are there to be put
        if (i >= n)
        return;

        // current is included, put next at next location
        if (data.size() > index){
            data.set(index,arr.get(i));}
        else{
            data.add(arr.get(i));
        }
        combinationUtil(arr, n, r, index+1, data, i+1, combos);

        // current is excluded, replace it with next (Note that
        // i+1 is passed, but index is not changed)
        combinationUtil(arr, n, r, index, data, i+1, combos);
    }
    
    public void solve(MinesweeperBoard board) {
        board.uncoverCell((int)board.getWidth()/2,(int)board.getHeight()/2); //uncover cell in corner of board
        solver(board,(int)board.getWidth()/2,(int)board.getHeight()/2);
        //displaySolutions();
    }
    public void solver(MinesweeperBoard board, int x, int y){
        //board.displayBoard();
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (board.getRevealed()[i][j] && board.getBoard()[i][j] == -1){
                    return;
                }
            }
        }
        if (board.getBoard()[x][y] == -1){ return; }
        int bombs = 0;
        for (boolean[] row: board.getFlagged()){
            for (boolean bomb: row){
                if (bomb){
                    bombs++;
                }
            }
        }
        ArrayList<int[]> left = board.anyLeft();

        if (left.size()==0 && bombs == board.getNumMines()){
            //System.out.println(left.size());
            //board.displayBoard();
            boards.add(board); return;}
        /*if (left.size() == 1 && bombs+1 == board.getNumMines()){
            System.out.println(left.size());
            board.displayBoard();
            board.flagCell(left.get(0)[0], left.get(0)[1]);
            boards.add(board); 
            return;}*/
        if (bombs > board.getNumMines()){return;}
        int[] count = {0};
        ArrayList<int[]> neighbors = getNeighbors(board,x,y,count);
        int c = count[0];
        int flag = 0;
        for (int[] neighbor : neighbors){
            int w = neighbor[0];
            int h = neighbor[1];
            if (board.getFlagged()[w][h]){
                flag++;
            }
        }
        if (board.getBoard()[x][y] < flag){
            return;
        }
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
                double[][] matrix = new double[m][cells.size()];
                for (int i = 0; i < m; i++){
                    ArrayList<Integer> row = new ArrayList<>();
                    //matrix[i] = new int[cells.size()];
                    int j = 0;
                    for (ArrayList<Integer> r: cells){
                        if (coeffs.get(i).contains(r)){
                            matrix[i][j] = 1;
                        }else{
                            matrix[i][j] = 0;
                        }
                        j++;
                    }
                }
                Matrix newMat = new Matrix(matrix);
                Matrix newb = new Matrix(needs, needs.length);
                if (newMat.rank() < newMat.getColumnDimension()) {
                    //System.out.println("System of equations is rank deficient. Cannot solve.");
                    continue;
                }
                else { 
                    //System.out.println("System of equations is not rank deficient!");
                    Matrix solution = newMat.solve(newb);
                    //System.out.print("solution: \n");
                    //solution.print(5,2);
                    double [][] sol = solution.getArray();
                    //System.out.println(cells);
                    int i = 0;
                    for (ArrayList<Integer> r: cells){
                        if(sol[i][0] > 0){
                            board.flagCell(r.get(0),r.get(1));
                        }
                        else {
                            board.uncoverCell(r.get(0),r.get(1));
                        }
                    }
                    solver(board, x, y);
                }
            }
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
        if (board.getBoard()[x][y] == c){
            for (int[] neighbor : neighbors){
                int w = neighbor[0];
                int h = neighbor[1];
                board.flagCell(w, h);
                //solver(board, w, h);
            }
        }
    }
}
