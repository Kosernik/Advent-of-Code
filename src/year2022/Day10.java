package year2022;

import java.util.List;

public class Day10 {

    public static void main(String[] args) {
        Day10 solution = new Day10();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day10.txt");

        System.out.println("Task 1: " + solution.getSumOfStrengths(inputData));

        solution.renderImage(inputData);
    }

    private void renderImage(List<String> inputData) {
        int position = 1;
        int tick = 0;

        for (String input : inputData) {
            int increment = 0;
            if (!input.equals("noop")) {
                increment = Integer.parseInt(input.substring(5));
                tick ++;
                updateScreen(tick, position);
            }

            tick++;
            updateScreen(tick, position);

            position += increment;
        }
    }

    private void updateScreen(int tick, int position) {
        System.out.print(getPixel(tick, position));
        if (tick % 40 == 0) {
            System.out.println();
        }
    }

    private char getPixel(int tick, int position) {
        int currentPos = tick % 40 - 1;

        if (position-1 == currentPos || position == currentPos || position+1 == currentPos) {
            return '#';
        } else {
            return ' ';
        }
    }

    private int getSumOfStrengths(List<String> inputData) {
        int result = 0;
        int value = 1;
        int tick = 0;

        for (String input : inputData) {
            int increment = 0;
            if (!input.equals("noop")) {
                increment = Integer.parseInt(input.substring(5));

                tick ++;
                if (isTickValid(tick)) {
                    result = result + (value * tick);
                }
            }

            tick++;
            if (isTickValid(tick)) {
                result = result + (value * tick);
            }

            value += increment;
        }

        System.out.println("Total ticks: " + tick);

        return result;
    }

    private boolean isTickValid(int tick) {
        //  20th, 60th, 100th, 140th, 180th, and 220th
        return tick == 20 || tick == 60 || tick == 100 || tick == 140 || tick == 180 || tick == 220;
    }
}
