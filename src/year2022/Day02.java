package year2022;

import java.util.List;

public class Day02 {
    private static final int LOOSE = 0;
    private static final int DRAW = 3;
    private static final int VICTORY = 6;

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day02.txt");

        task01(inputData);
        System.out.println("----");
        task02(inputData);
    }

    private static void task01(List<String> inputData) {
        long score = 0L;

        for (String line : inputData) {
            char opponent = line.charAt(0);
            char myPlay = line.charAt(2);

            if (myPlay == 'X') {
                score += 1;
                if (opponent == 'A') {
                    score += DRAW;
                } else if (opponent == 'B') {
                    score += LOOSE;
                } else if (opponent == 'C') {
                    score += VICTORY;
                }
            } else if (myPlay == 'Y') {
                score += 2;
                if (opponent == 'A') {
                    score += VICTORY;
                } else if (opponent == 'B') {
                    score += DRAW;
                } else if (opponent == 'C') {
                    score += LOOSE;
                }
            } else if (myPlay == 'Z') {
                score += 3;
                if (opponent == 'A') {
                    score += LOOSE;
                } else if (opponent == 'B') {
                    score += VICTORY;
                } else if (opponent == 'C') {
                    score += DRAW;
                }
            }
        }

        System.out.println("The result for task 1 is: " + score);
    }

    private static void task02(List<String> inputData) {
        long score = 0L;

        for (String line : inputData) {
            char opponent = line.charAt(0);
            char myResult = line.charAt(2);

            if (opponent == 'A') {
                if (myResult == 'X') {  // should loose
                    score += 3 + LOOSE;
                } else if (myResult == 'Y') {   // need a draw
                    score += 1 + DRAW;
                } else if (myResult == 'Z') {   // should win
                    score += 2 + VICTORY;
                }
            } else if (opponent == 'B') {
                if (myResult == 'X') {  // should loose
                    score += 1 + LOOSE;
                } else if (myResult == 'Y') {   // need a draw
                    score += 2 + DRAW;
                } else if (myResult == 'Z') {   // should win
                    score += 3 + VICTORY;
                }
            } else if (opponent == 'C') {
                if (myResult == 'X') {  // should loose
                    score += 2 + LOOSE;
                } else if (myResult == 'Y') {   // need a draw
                    score += 3 + DRAW;
                } else if (myResult == 'Z') {   // should win
                    score += 1 + VICTORY;
                }
            }
        }

        System.out.println("The result for task 2 is: " + score);
    }
}
