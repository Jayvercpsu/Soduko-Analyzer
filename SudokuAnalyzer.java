import java.util.Arrays;
import java.util.Scanner;

public class SudokuAnalyzer {
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final int[] POSSIBLE_VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Sudoku puzzle program header
        System.out.println("Sudoku Puzzle Program");
        System.out.println("Please provide the Sudoku problem with the Sudoku values.");
        System.out.println("Please enter the values for each row in the following format (example: 000100200)");

        // Input Sudoku values
        int[][] puzzle = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.out.print("Please enter value for Row #" + (i + 1) + ": ");
            String rowValues = scanner.next();

            // Validate input
            if (!isValidInput(rowValues)) {
                System.out.println("Invalid input! Only values from 0 to 9 are allowed.");
                return;
            }

            // Convert string values to integer array
            for (int j = 0; j < SIZE; j++) {
                puzzle[i][j] = Character.getNumericValue(rowValues.charAt(j));
            }
        }

        // Display Sudoku puzzle
        System.out.println("Entered Sudoku Puzzle:");
        displaySudoku(puzzle);

        // Main loop
        boolean exit = false;
        while (!exit) {
            // Display menu
            System.out.println("\nWhat action do you like to perform?");
            System.out.println("1. - Provide an answer for this cell");
            System.out.println("2. - Check another cell");
            System.out.println("3. - Reset the Puzzle");
            System.out.println("4. - Exit the application");
            System.out.print("Please enter your choice: ");

            // Handle user choice
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    provideAnswer(scanner, puzzle);
                    break;
                case 2:
                    checkAnotherCell(scanner, puzzle);
                    break;
                case 3:
                    resetPuzzle(puzzle);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
            }
        }
    }

    // Input validation for Sudoku puzzle
    private static boolean isValidInput(String input) {
        if (input.length() != SIZE) return false;
        for (char c : input.toCharArray()) {
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    // Display Sudoku puzzle
    private static void displaySudoku(int[][] puzzle) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(puzzle[i][j] + " ");
                if ((j + 1) % 3 == 0 && j != SIZE - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % 3 == 0 && i != SIZE - 1) {
                System.out.println("- - - - - - - - - - -");
            }
        }
    }

    // Provide an answer for a specific cell
    private static void provideAnswer(Scanner scanner, int[][] puzzle) {
        // Analyze cell
        System.out.print("What cell would you like to provide an answer for? (e.g., G2): ");
        String cell = scanner.next();

        // Validate cell input
        if (!isValidCellInput(cell)) {
            System.out.println("Invalid cell input! Please enter a valid cell (e.g., G2).");
            return;
        }

        // Convert cell to coordinates
        int[] cellCoordinates = cellToCoordinates(cell);
        int row = cellCoordinates[0];
        int col = cellCoordinates[1];

        // Retrieve values in current row, column, and box
        int[] rowValues = getRowValues(puzzle, row);
        int[] colValues = getColumnValues(puzzle, col);
        int[] boxValues = getBoxValues(puzzle, row, col);

        // Generate hints
        int[] rowHints = generateHints(rowValues);
        int[] colHints = generateHints(colValues);
        int[] boxHints = generateHints(boxValues);

        // Print hints
        System.out.println("Row Hints: " + Arrays.toString(rowHints));
        System.out.println("Column Hints: " + Arrays.toString(colHints));
        System.out.println("Box Hints: " + Arrays.toString(boxHints));

        // Compute possible answers
        int[] possibleAnswers = computePossibleAnswers(rowHints, colHints, boxHints);

        // Print possible answers
        System.out.println("Possible Answer: " + Arrays.toString(possibleAnswers));

        // Answer interface
        System.out.print("Please provide the answer for the cell (" + (row + 1) + ", " + (col + 1) + "): ");
        int answer = scanner.nextInt();

        // Validate and assign answer
        if (isValidAnswer(possibleAnswers, answer)) {
            puzzle[row][col] = answer;
            System.out.println("Answer assigned successfully!");
        } else {
            System.out.println("Invalid answer! Please enter a valid answer from the possible answers.");
        }
    }

    // Check another cell
    private static void checkAnotherCell(Scanner scanner, int[][] puzzle) {
        // Analyze cell
        System.out.print("What cell would you like to analyze? (e.g., G2): ");
        String cell = scanner.next();

        // Validate cell input
        if (!isValidCellInput(cell)) {
            System.out.println("Invalid cell input! Please enter a valid cell (e.g., G2).");
            return;
        }

        // Convert cell to coordinates
        int[] cellCoordinates = cellToCoordinates(cell);
        int row = cellCoordinates[0];
        int col = cellCoordinates[1];

        // Retrieve values in current row, column, and box
        int[] rowValues = getRowValues(puzzle, row);
        int[] colValues = getColumnValues(puzzle, col);
        int[] boxValues = getBoxValues(puzzle, row, col);

        // Display values
        System.out.println("Row Values: " + Arrays.toString(rowValues));
        System.out.println("Column Values: " + Arrays.toString(colValues));
        System.out.println("Box Values: " + Arrays.toString(boxValues));
    }

    // Reset the puzzle
    private static void resetPuzzle(int[][] puzzle) {
        // Create a new empty puzzle
        int[][] newPuzzle = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(newPuzzle[i], 0);
        }
        // Copy values from the new puzzle to the original puzzle
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(newPuzzle[i], 0, puzzle[i], 0, SIZE);
        }
        System.out.println("Puzzle reset successfully!");
    }

    // Input validation for cell input
    private static boolean isValidCellInput(String cell) {
        if (cell.length() != 2) return false;
        char rowChar = cell.charAt(0);
        char colChar = cell.charAt(1);
        return rowChar >= 'A' && rowChar <= 'I' && colChar >= '1' && colChar <= '9';
    }

    // Convert cell input to coordinates
    private static int[] cellToCoordinates(String cell) {
        int row = cell.charAt(0) - 'A';
        int col = Integer.parseInt(cell.substring(1)) - 1;
        return new int[]{row, col};
    }

    // Retrieve values in the current row
    private static int[] getRowValues(int[][] puzzle, int row) {
        return puzzle[row];
    }

    // Retrieve values in the current column
    private static int[] getColumnValues(int[][] puzzle, int col) {
        int[] columnValues = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            columnValues[i] = puzzle[i][col];
        }
        return columnValues;
    }

    // Retrieve values in the box
    private static int[] getBoxValues(int[][] puzzle, int row, int col) {
        int[] boxValues = new int[SIZE];
        int index = 0;
        int boxRow = row - row % BOX_SIZE;
        int boxCol = col - col % BOX_SIZE;
        for (int i = boxRow; i < boxRow + BOX_SIZE; i++) {
            for (int j = boxCol; j < boxCol + BOX_SIZE; j++) {
                boxValues[index++] = puzzle[i][j];
            }
        }
        return boxValues;
    }

    // Generate hints
    private static int[] generateHints(int[] values) {
        int[] hints = new int[SIZE];
        Arrays.fill(hints, 1);
        for (int value : values) {
            if (value != 0) {
                hints[value - 1] = 0;
            }
        }
        return hints;
    }

    // Compute possible answers
    private static int[] computePossibleAnswers(int[] rowHints, int[] colHints, int[] boxHints) {
        int[] possibleAnswers = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (rowHints[i] != 0 && colHints[i] != 0 && boxHints[i] != 0) {
                possibleAnswers[i] = i + 1;
            }
        }
        return possibleAnswers;
    }

    // Validate answer
    private static boolean isValidAnswer(int[] possibleAnswers, int answer) {
        for (int possibleAnswer : possibleAnswers) {
            if (possibleAnswer == answer) {
                return true;
            }
        }
        return false;
    }
}
