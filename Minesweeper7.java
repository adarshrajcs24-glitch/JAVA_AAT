import java.util.Random;
import java.util.Scanner;

public class Minesweeper7 {

    static final int SIZE = 7;
    static final int MINES = 8;

    static char[][] board = new char[SIZE][SIZE];
    static boolean[][] revealed = new boolean[SIZE][SIZE];
    static boolean[][] mines = new boolean[SIZE][SIZE];

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        initBoard();
        placeMines();
        fillNumbers();

        System.out.println("=== 7x7 MINESWEEPER ===");
        System.out.println("Enter row and column (0-6). Example: 2 3");
        System.out.println("Goal: Reveal all safe cells.\n");

        int safeCells = SIZE * SIZE - MINES;
        int revealedCount = 0;

        while (true) {

            printBoard();

            System.out.print("\nEnter row and col: ");
            int r = sc.nextInt();
            int c = sc.nextInt();

            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                System.out.println("Invalid input!");
                continue;
            }

            // stepping on mine
            if (mines[r][c]) {
                revealAll();
                printBoard();
                System.out.println("\n💥 BOOM! You hit a mine. Game Over!");
                break;
            }

            // reveal cell
            revealedCount += reveal(r, c);

            // win condition
            if (revealedCount == safeCells) {
                revealAll();
                printBoard();
                System.out.println("\n🎉 Congratulations! You cleared all safe cells!");
                break;
            }
        }

        sc.close();
    }

    // initialize board with hidden cells
    static void initBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';
    }

    // randomly place mines
    static void placeMines() {
        Random rand = new Random();
        int placed = 0;

        while (placed < MINES) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);

            if (!mines[r][c]) {
                mines[r][c] = true;
                placed++;
            }
        }
    }

    // Fill numbers based on adjacent mines
    static void fillNumbers() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                if (mines[r][c]) continue;

                int count = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        int nr = r + i;
                        int nc = c + j;

                        if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && mines[nr][nc]) {
                            count++;
                        }
                    }
                }

                board[r][c] = (count == 0) ? ' ' : (char) ('0' + count);
            }
        }
    }

    // reveal cell (recursive for blank spaces)
    static int reveal(int r, int c) {
        if (revealed[r][c]) return 0;

        revealed[r][c] = true;

        // count this reveal if safe
        int count = (board[r][c] != ' ') ? 1 : 1;

        // if empty space, reveal neighbors
        if (board[r][c] == ' ') {

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    int nr = r + i;
                    int nc = c + j;

                    if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                        if (!revealed[nr][nc] && !mines[nr][nc]) {
                            count += reveal(nr, nc);
                        }
                    }
                }
            }
        }

        return count;
    }

    // reveal entire board
    static void revealAll() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                revealed[i][j] = true;
    }

    // print board
    static void printBoard() {
        System.out.print("\n   ");
        for (int c = 0; c < SIZE; c++) System.out.print(c + " ");
        System.out.println();

        for (int r = 0; r < SIZE; r++) {
            System.out.print(r + ": ");
            for (int c = 0; c < SIZE; c++) {

                if (!revealed[r][c]) {
                    System.out.print(". ");
                }
                else if (mines[r][c]) {
                    System.out.print("* ");
                }
                else {
                    System.out.print(board[r][c] + " ");
                }
            }
            System.out.println();
        }
    }
}