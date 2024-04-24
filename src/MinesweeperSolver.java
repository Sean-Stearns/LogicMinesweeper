package src;
import java.util.*;

public class MinesweeperSolver {
    Set<MinesweeperBoard> boards;

    public MinesweeperSolver(MinesweeperBoard board) {
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
    public void displaySolutions() {
        for (MinesweeperBoard board : boards) {
            board.displayBoard();
            System.out.println();
        }
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
    class intArrComparator implements Comparator<int[]>{
        @Override
        public int compare(int[] o1, int[] o2) {
            if (o1[0] > o2[0]) { return 1;}
            return 0; 
        }  
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
        board.uncoverCell(0,0); //uncover cell in corner of board
        solver(board, 0, 0);
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
            /*int h = neighbor[1];
            if (!board.getFlagged()[w][h]){
                System.out.println("splitting: "+flag);
                MinesweeperBoard tempBoard = new MinesweeperBoard(board);
                tempBoard.uncoverCell(w, h);
                solver(tempBoard, w, h);
            }*/
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
