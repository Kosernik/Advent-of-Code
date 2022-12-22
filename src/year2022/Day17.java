package year2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 {

    //  SHAPES
    //
    //  ####
    //
    //  .#.
    //  ###
    //  .#.
    //
    //  ..#
    //  ..#
    //  ###
    //
    //  #
    //  #
    //  #
    //  #
    //
    //  ##
    //  ##
    //
    private final Shape[] orderOfShapes = {new Horizontal(), new Plus(), new Corner(), new Vertical(), new Square()};

    private final int CHAMBER_WIDTH = 7;
    private final int HEIGHT_ABOVE = 3;
    private final int LEFT_INDENT = 2;

    private final char EMPTY_CELL = '.';
    private final char ROCK = '#';
    private final char MOVING_ROCK = '@';

    private int[] directions;
    private int nextDirectionIdx = 0;

    public static void main(String[] args) {
        Day17 solution = new Day17();

        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day17.txt");
//        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day17test.txt");

        solution.directions = solution.convertDirections(inputData);
        List<char[]> chamber = solution.getChamber();

        int task1 = solution.task1(chamber, 2022) + 1;

        System.out.println("Final state");
        solution.printChamber(chamber);
        System.out.println("Task 1: " + task1);

    }

    private int task1(List<char[]> chamber, int numberOfShapes) {
        for (int i = 0; i < numberOfShapes; i++) {
            int shapeIdx = i % orderOfShapes.length;
            int[] topLeftCoordinates = addNextShapeToChamber(chamber, shapeIdx);
            moveShape(chamber, shapeIdx, topLeftCoordinates);
        }

        return getFloorIdx(chamber);
    }

    private void moveShape(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        while (true) {
            boolean successfulMovement = applyDirectionalMove(chamber, shapeIdx, topLeftCoordinate);
            if (!successfulMovement) return;
        }
    }

    private boolean applyDirectionalMove(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        int nextDirection = directions[nextDirectionIdx];
        nextDirectionIdx = (nextDirectionIdx + 1) % directions.length;

        if (nextDirection == 1) {
            return moveRight(chamber, shapeIdx, topLeftCoordinate);
        } else {
            return moveLeft(chamber, shapeIdx, topLeftCoordinate);
        }
    }

    private boolean moveLeft(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        int leftIdx = topLeftCoordinate[1]-1;
        if (leftIdx >= 0) {
            topLeftCoordinate[1]--;
            char[][] mask = orderOfShapes[shapeIdx].getShape();
            char[][] currentState = getCurrentState(chamber, shapeIdx, topLeftCoordinate);

            if (isShapeNotOverlapping(mask, currentState)) {
                topLeftCoordinate[1]++;
                eraseShape(chamber, topLeftCoordinate, mask);
                topLeftCoordinate[1]--;
                redrawShape(chamber, topLeftCoordinate, mask);
            } else {
                topLeftCoordinate[1]++;
            }
        }

        return moveDown(chamber, shapeIdx, topLeftCoordinate);
    }

    private boolean moveRight(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        int rightIdx = topLeftCoordinate[1] + orderOfShapes[shapeIdx].getWidth();
        if (rightIdx < CHAMBER_WIDTH) {
            topLeftCoordinate[1]++;
            char[][] mask = orderOfShapes[shapeIdx].getShape();
            char[][] currentState = getCurrentState(chamber, shapeIdx, topLeftCoordinate);

            if (isShapeNotOverlapping(mask, currentState)) {
                topLeftCoordinate[1]--;
                eraseShape(chamber, topLeftCoordinate, mask);
                topLeftCoordinate[1]++;
                redrawShape(chamber, topLeftCoordinate, mask);
            } else {
                topLeftCoordinate[1]--;
            }
        }

        return moveDown(chamber, shapeIdx, topLeftCoordinate);
    }

    private boolean moveDown(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        topLeftCoordinate[0]--;
        int bottomIdx = topLeftCoordinate[0] - orderOfShapes[shapeIdx].getHeight()+1;
        if (bottomIdx < 0) {
            topLeftCoordinate[0]++;
            fixTheShape(chamber, shapeIdx, topLeftCoordinate);
            return false;
        }

        char[][] mask = orderOfShapes[shapeIdx].getShape();
        char[][] currentState = getCurrentState(chamber, shapeIdx, topLeftCoordinate);

        if (isShapeNotOverlapping(mask, currentState)) {
            topLeftCoordinate[0]++;
            eraseShape(chamber, topLeftCoordinate, mask);
            topLeftCoordinate[0]--;
            redrawShape(chamber, topLeftCoordinate, mask);

            return true;
        } else {
            topLeftCoordinate[0]++;
            fixTheShape(chamber, shapeIdx, topLeftCoordinate);
            return false;
        }
    }

    private void fixTheShape(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        Shape shape = orderOfShapes[shapeIdx];
        char[][] mask = shape.getShape();
        for (int i = 0; i < mask.length; i++) {
            char[] line = chamber.get(topLeftCoordinate[0]-i);
            for (int j = 0; j < mask[0].length; j++) {
                if (mask[i][j] == MOVING_ROCK) {
                    line[topLeftCoordinate[1]+j] = ROCK;
                }
            }
        }
    }

    private boolean isShapeNotOverlapping(char[][] shapeMask, char[][] currentState) {
        if (shapeMask.length != currentState.length || shapeMask[0].length != currentState[0].length) {
            throw new IllegalArgumentException("Shapes are different sizes");
        }
        for (int i = shapeMask.length - 1; i >= 0; i--) {
            for (int j = 0; j < shapeMask[0].length; j++) {
                if (shapeMask[i][j] == MOVING_ROCK && currentState[i][j] == ROCK) {
                    return false;
                }
            }
        }

        return true;
    }

    private void redrawShape(List<char[]> chamber, int[] topLeftCoordinate, char[][] mask) {
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                if (mask[i][j] == MOVING_ROCK) {
                    chamber.get(topLeftCoordinate[0] - i)[topLeftCoordinate[1] + j] = MOVING_ROCK;
                }
            }
        }
    }
    private void eraseShape(List<char[]> chamber, int[] topLeftCoordinate, char[][] mask) {
        for (int i = 0; i < mask.length; i++) {
            char[] line = chamber.get(topLeftCoordinate[0] - i);
            for (int j = 0; j < mask[0].length; j++) {
                if (mask[i][j] == MOVING_ROCK) {
                    line[topLeftCoordinate[1] + j] = EMPTY_CELL;
                }
            }
        }
    }

    private char[][] getCurrentState(List<char[]> chamber, int shapeIdx, int[] topLeftCoordinate) {
        int height = orderOfShapes[shapeIdx].getHeight();
        int width = orderOfShapes[shapeIdx].getWidth();
        char[][] state = new char[height][width];

        for (int i = 0; i < height; i++) {
            char[] line = chamber.get(topLeftCoordinate[0] - i);
            System.arraycopy(line, topLeftCoordinate[1], state[i], 0, width);
        }
        return state;
    }

    private int[] addNextShapeToChamber(List<char[]> chamber, int shapeIdx) {
        int floorIdx = getFloorIdx(chamber);
        int shapeHeight = orderOfShapes[shapeIdx].getHeight();

        int requiredHeight = floorIdx + HEIGHT_ABOVE + shapeHeight;
        if (requiredHeight >= chamber.size()) {
            addLines(chamber, shapeHeight);
        }

        int[] topLeftCoordinates = {requiredHeight, LEFT_INDENT};

        drawAddedShape(chamber, shapeIdx, requiredHeight);

        return topLeftCoordinates;
    }

    private void drawAddedShape(List<char[]> chamber, int shapeIdx, int ceilingIdx) {
        Shape shape = orderOfShapes[shapeIdx];
        char[][] shapeMask = shape.getShape();

        for (int i = 0; i < shapeMask.length; i++) {
            char[] line = chamber.get(ceilingIdx - i);
            for (int j = 0; j < shapeMask[0].length; j++) {
                if (shapeMask[i][j] == MOVING_ROCK) {
                    line[LEFT_INDENT + j] = MOVING_ROCK;
                }
            }
        }
    }

    private List<char[]> getChamber() {
        List<char[]> chamber = new ArrayList<>();

        for (int i = orderOfShapes[0].getHeight() + HEIGHT_ABOVE; i > 0; i--) {
            char[] line = getEmptyLine();
            chamber.add(line);
        }

        return chamber;
    }

    private void addLines(List<char[]> chamber, int nextShapeHeight) {
        int floorIdx = getFloorIdx(chamber);
        int nextHeight = floorIdx + HEIGHT_ABOVE + nextShapeHeight + 1;

        for (int i = nextHeight - chamber.size(); i > 0; i--) {
            char[] line = getEmptyLine();
            chamber.add(line);
        }
    }

    private int getFloorIdx(List<char[]> chamber) {
        for (int i = chamber.size() - 1; i >= 0; i--) {
            for (char ch : chamber.get(i)) {
                if (ch == ROCK) {
                    return i;
                }
            }
        }
        return -1;
    }

    private char[] getEmptyLine() {
        char[] line = new char[CHAMBER_WIDTH];
        Arrays.fill(line, EMPTY_CELL);
        return line;
    }

    private int[] convertDirections(List<String> inputData) {
        int[] directions = new int[inputData.get(0).length()];
        int idx = 0;
        for (char ch : inputData.get(0).toCharArray()) {
            if (ch == '>') {
                directions[idx] = 1;
            } else {
                directions[idx] = -1;
            }
            idx++;
        }
        return directions;
    }

    private void printChamber(List<char[]> chamber) {
        for (int i = chamber.size() - 1; i >= 0; i--) {
            System.out.print('|');
            for (char ch : chamber.get(i)) {
                System.out.print(ch);
            }
            System.out.println('|');
        }
        System.out.print("\\");
        for (int i = 0; i < chamber.get(0).length; i++) {
            System.out.print("=");
        }
        System.out.println("/");
    }

    abstract class Shape {
        abstract int getHeight();

        abstract int getWidth();

        abstract char[][] getShape();

    }

    class Horizontal extends Shape {
        private final char[][] shape = {
                {MOVING_ROCK, MOVING_ROCK, MOVING_ROCK, MOVING_ROCK}
        };

        @Override
        int getHeight() {
            return shape.length;
        }

        @Override
        int getWidth() {
            return shape[0].length;
        }

        @Override
        char[][] getShape() {
            return shape;
        }
    }

    class Plus extends Shape {
        private final char[][] shape = {
                {EMPTY_CELL, MOVING_ROCK, EMPTY_CELL},
                {MOVING_ROCK, MOVING_ROCK, MOVING_ROCK},
                {EMPTY_CELL, MOVING_ROCK, EMPTY_CELL}
        };

        @Override
        int getHeight() {
            return shape.length;
        }

        @Override
        int getWidth() {
            return shape[0].length;
        }

        @Override
        char[][] getShape() {
            return shape;
        }
    }

    class Corner extends Shape {
        private final char[][] shape = {
                {EMPTY_CELL, EMPTY_CELL, MOVING_ROCK},
                {EMPTY_CELL, EMPTY_CELL, MOVING_ROCK},
                {MOVING_ROCK, MOVING_ROCK, MOVING_ROCK}
        };

        @Override
        int getHeight() {
            return shape.length;
        }

        @Override
        int getWidth() {
            return shape[0].length;
        }

        @Override
        char[][] getShape() {
            return shape;
        }
    }

    class Vertical extends Shape {
        private final char[][] shape = {
                {MOVING_ROCK},
                {MOVING_ROCK},
                {MOVING_ROCK},
                {MOVING_ROCK}
        };

        @Override
        int getHeight() {
            return shape.length;
        }

        @Override
        int getWidth() {
            return shape[0].length;
        }

        @Override
        char[][] getShape() {
            return shape;
        }
    }

    class Square extends Shape {
        private final char[][] shape = {
                {MOVING_ROCK, MOVING_ROCK},
                {MOVING_ROCK, MOVING_ROCK}
        };

        @Override
        int getHeight() {
            return shape.length;
        }

        @Override
        int getWidth() {
            return shape[0].length;
        }

        @Override
        char[][] getShape() {
            return shape;
        }
    }
}
