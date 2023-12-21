package caro;
import java.util.Arrays;

public class CaroAI {
    private static final int SIZE = 15;
    private static final String EMPTY = "-";
    private static final String PLAYER_X = "X";
    private static final String PLAYER_O = "O";

    public static void main(String[] args) {
        CaroAI caroAI = new CaroAI();
        String[][] board = initializeBoard();

        // Example: Player X makes some moves
        board[0][0] = PLAYER_X;
        board[1][1] = PLAYER_O;
        board[2][2] = PLAYER_X;

        // Get the best defensive move for Player O after Player X's move
        int[] defensiveMove = caroAI.getBestDefensiveMove(board, PLAYER_O);
        System.out.println("Defensive Move: Row " + defensiveMove[0] + ", Column " + defensiveMove[1]);
    }

    private static String[][] initializeBoard() {
        String[][] board = new String[SIZE][SIZE];
        for (String[] row : board) {
            Arrays.fill(row, EMPTY);
        }
        return board;
    }

    public int[] getBestDefensiveMove(String[][] board, String player) {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col].equals(EMPTY)) {
                    board[row][col] = player;
                    int score = evaluateDefensiveHeuristic(board, row, col, player);
                    board[row][col] = EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }

        return bestMove;
    }

    private int evaluateDefensiveHeuristic(String[][] board, int moveRow, int moveCol, String player) {
        int score = 0;

        for (int row = Math.max(0, moveRow - 5); row <= Math.min(SIZE - 1, moveRow + 5); row++) {
            for (int col = Math.max(0, moveCol - 5); col <= Math.min(SIZE - 1, moveCol + 5); col++) {
                if (board[row][col].equals(EMPTY) && !(row == moveRow && col == moveCol)) {
                    board[row][col] = player;
                    int opponentScore = evaluateBoard(board, PLAYER_X); // Assuming X is the opponent
                    board[row][col] = EMPTY;

                    score += opponentScore;
                }
            }
        }

        return score;
    }

    private int evaluateBoard(String[][] board, String currentPlayer) {
        int score = 0;

        for (int i = 0; i < SIZE; i++) {
            score += evaluateLine(board[i], currentPlayer);
            score += evaluateLine(getColumn(board, i), currentPlayer);
        }

        score += evaluateLine(getDiagonal(board, true), currentPlayer);
        score += evaluateLine(getDiagonal(board, false), currentPlayer);

        return score;
    }

    private int evaluateLine(String[] line, String player) {
        int consecutive = 0;
        int openEnds = 0;
        int score = 0;

        for (String cell : line) {
            if (cell.equals(player)) {
                consecutive++;
            } else if (cell.equals(EMPTY)) {
                openEnds++;
            } else {
                score += evaluateSequence(consecutive, openEnds, player);
                consecutive = 0;
                openEnds = 0;
            }
        }

        score += evaluateSequence(consecutive, openEnds, player);

        return score;
    }

    private int evaluateSequence(int consecutive, int openEnds, String player) {
        if (consecutive >= 5) {
            return Integer.MAX_VALUE;
        } else if (consecutive == 4 && openEnds == 2) {
            return 10000;
        } else if (consecutive == 3 && openEnds == 2) {
            return 1000;
        } else if (consecutive == 2 && openEnds == 2) {
            return 100;
        } else if (consecutive == 1 && openEnds == 2) {
            return 10;
        } else if (consecutive == 3 && openEnds == 1) {
            return 100;
        } else if (consecutive == 2 && openEnds == 1) {
            return 10;
        } else {
            return 0;
        }
    }

    private String[] getColumn(String[][] board, int col) {
        String[] column = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            column[i] = board[i][col];
        }
        return column;
    }

    private String[] getDiagonal(String[][] board, boolean mainDiagonal) {
        String[] diagonal = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            diagonal[i] = mainDiagonal ? board[i][i] : board[i][SIZE - 1 - i];
        }
        return diagonal;
    }
}
