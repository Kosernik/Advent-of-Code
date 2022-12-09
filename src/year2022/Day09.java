package year2022;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day09 {

    public static void main(String[] args) {
        Day09 solution = new Day09();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day09.txt");

        // Total length is 2
        System.out.println("Task 1: " + solution.numberOfVisitedPositions(inputData, 1));

        // Total length is 10
        System.out.println("Task 2: " + solution.numberOfVisitedPositions(inputData, 9));
    }

    private int numberOfVisitedPositions(List<String> inputData, int lengthOfTail) {
        Rope head = new Head();
        for (int i = 0; i < lengthOfTail; i++) {
            head.addTail();
        }

        for (String command : inputData) {
            String directionCommand = command.substring(0, 1);
            Direction direction = switch (directionCommand) {
                case "U" -> Direction.UP;
                case "R" -> Direction.RIGHT;
                case "D" -> Direction.DOWN;
                default -> Direction.LEFT;
            };

            int steps = Integer.parseInt(command.substring(2));

            for (int step = 0; step < steps; step++) {
                head.moveSelf(direction);
            }
        }

        return head.getNumberOfVisitedByTailCells();
    }

    static class Head extends Rope {
        Head() {
            this.visitedCells.add("x0y0");
        }

        public void moveSelf(Direction direction) {
            if (direction == Direction.UP) {
                yCoordinate++;
            } else if (direction == Direction.RIGHT) {
                xCoordinate++;
            } else if (direction == Direction.DOWN) {
                yCoordinate--;
            } else if (direction == Direction.LEFT) {
                xCoordinate--;
            } else {
                throw new IllegalArgumentException();
            }

            moveTail();
        }

        @Override
        void addTail() {
            if (this.tail == null) {
                this.tail = new Head();
            } else {
                this.visitedCells = null;
                this.tail.addTail();
            }
        }
    }

    abstract static class Rope {
        int xCoordinate = 0;
        int yCoordinate = 0;
        Set<String> visitedCells = new HashSet<>();

        Rope tail = null;

        abstract void moveSelf(Direction direction);

        int getX() {
            return xCoordinate;
        }

        int getY() {
            return yCoordinate;
        }

        void moveX(int step) {
            xCoordinate += step;
        }

        void moveY(int step) {
            yCoordinate += step;
        }

        abstract void addTail();

        Rope getTail() {
            return tail;
        }

        void moveTail() {
            Rope tail = getTail();
            if (getTail() == null) {
                if (visitedCells != null) {
                    visitedCells.add("x" + getX() + "y" + getY());
                }
                return;
            }
            int xDiff = Math.abs(getX() - tail.getX());
            int yDiff = Math.abs(getY() - tail.getY());

            if (xDiff <= 1 && yDiff <= 1) return;

            int x = Integer.compare(getX(), tail.getX());
            int y = Integer.compare(getY(), tail.getY());

            tail.moveX(x);
            tail.moveY(y);

            tail.moveTail();
        }

        int getNumberOfVisitedByTailCells() {
            if (getTail() == null) {
                return visitedCells.size();
            } else {
                return getTail().getNumberOfVisitedByTailCells();
            }
        }
    }

    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }
}
