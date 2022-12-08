package year2022;

import java.util.List;

public class Day08 {
    private static int[][] treeGreed;

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day08.txt");

        fillGreed(inputData);

        System.out.println("Task 1: " + countVisibleTrees());
        System.out.println("Task 2: " + getBestScenicScore());
    }


    /**
     * Returns the best scenic score on the greed
     * <p>
     * Complexity - O(N*N)
     * Memory - O(1)
     *
     * @return - the best scenic score on the greed.
     */
    private static long getBestScenicScore() {
        long bestScore = 0;
        for (int row = 1; row < treeGreed.length - 1; row++) {
            for (int col = 1; col < treeGreed[0].length - 1; col++) {
                long score = getScenicScore(row, col);
                bestScore = Math.max(bestScore, score);
            }
        }

        return bestScore;
    }

    /**
     * Calculates the scenic score for a given grid cell.
     * Scenic score is a product of the number of visible trees in 4 directions: top, bottom, left and right directions.
     * <p>
     * Complexity - O(N*N)
     * Memory - O(1)
     *
     * @param row - starting row position on the tree grid.
     * @param col - starting col position on the tree grid.
     * @return - the scenic score for the given cell.
     */
    private static long getScenicScore(int row, int col) {
        int treeHeight = treeGreed[row][col];
        long leftToRight = 0;
        long rightToLeft = 0;
        long topToBottom = 0;
        long bottomToTop = 0;

        for (int r = row + 1; r < treeGreed.length; r++) {
            if (treeGreed[r][col] >= treeHeight) {
                topToBottom++;
                break;
            } else {
                topToBottom++;
            }
        }
        for (int r = row - 1; r >= 0; r--) {
            if (treeGreed[r][col] >= treeHeight) {
                bottomToTop++;
                break;
            } else {
                bottomToTop++;
            }
        }
        for (int c = col + 1; c < treeGreed[0].length; c++) {
            if (treeGreed[row][c] >= treeHeight) {
                leftToRight++;
                break;
            } else {
                leftToRight++;
            }
        }
        for (int c = col - 1; c >= 0; c--) {
            if (treeGreed[row][c] >= treeHeight) {
                rightToLeft++;
                break;
            } else {
                rightToLeft++;
            }
        }

        return leftToRight * rightToLeft * topToBottom * bottomToTop;
    }

    /**
     * Returns the number of trees in a tree grid that are visible from one of four directions: top, bottom, left or
     * right directions.
     * <p>
     * Complexity - O(N)
     * Memory - O(N)
     *
     * @return - the number of visible trees.
     */
    private static int countVisibleTrees() {
        boolean[][] visibleTrees = new boolean[treeGreed.length][treeGreed[0].length];
        for (int i = 0; i < visibleTrees.length; i++) {
            visibleTrees[i][0] = true;
            visibleTrees[i][visibleTrees[0].length - 1] = true;
        }
        for (int i = 1; i < visibleTrees[0].length - 1; i++) {
            visibleTrees[0][i] = true;
            visibleTrees[visibleTrees.length - 1][i] = true;
        }

        int myHeight = treeGreed.length - 1;
        int myWidth = treeGreed[0].length - 1;

        int farRightIDX = treeGreed[0].length - 1;

        // Checking horizontal visibility
        for (int row = 1; row < myHeight; row++) {
            // Checking left-to-right visibility
            int prevMaxHeightLtoR = treeGreed[row][0];
            for (int col = 1; col < myWidth; col++) {
                if (treeGreed[row][col] > prevMaxHeightLtoR) {
                    visibleTrees[row][col] = true;
                    prevMaxHeightLtoR = treeGreed[row][col];
                }
            }

            // Checking right-to-left visibility
            int prevMaxHeightRtoL = treeGreed[row][farRightIDX];
            for (int col = farRightIDX; col > 0; col--) {
                if (treeGreed[row][col] > prevMaxHeightRtoL) {
                    visibleTrees[row][col] = true;
                    prevMaxHeightRtoL = treeGreed[row][col];
                }
            }
        }

        // Checking vertical visibility
        for (int col = 1; col < myWidth; col++) {
            // Checking top-to-bottom visibility
            int prevMaxHeightTtoB = treeGreed[0][col];
            for (int row = 1; row < myHeight; row++) {
                if (treeGreed[row][col] > prevMaxHeightTtoB) {
                    visibleTrees[row][col] = true;
                    prevMaxHeightTtoB = treeGreed[row][col];
                }
            }

            //Checking bottom-to-top visibility
            int prevMaxHeightBtoT = treeGreed[treeGreed.length - 1][col];
            for (int row = treeGreed.length - 2; row > 0; row--) {
                if (treeGreed[row][col] > prevMaxHeightBtoT) {
                    visibleTrees[row][col] = true;
                    prevMaxHeightBtoT = treeGreed[row][col];
                }
            }
        }

        int numberOfVisibleTrees = 0;
        for (boolean[] row : visibleTrees) {
            for (boolean visibility : row) {
                numberOfVisibleTrees += visibility ? 1 : 0;
            }
        }

        return numberOfVisibleTrees;
    }

    /**
     * Converts input data to a grid of heights of trees.
     * <p>
     * Complexity - O(N)
     * Memory - O(1)
     *
     * @param inputData - a list of strings representing the tree grid.
     */
    private static void fillGreed(List<String> inputData) {
        treeGreed = new int[inputData.size()][inputData.get(0).length()];

        int row = 0;

        for (String line : inputData) {
            for (int col = 0; col < line.length(); col++) {
                treeGreed[row][col] = line.charAt(col) - '0';
            }
            row++;
        }
    }
}
