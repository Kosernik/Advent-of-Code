package year2022;

import java.util.*;

public class Day12 {

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day12.txt");
        char[][] map = convertInput(inputData);

        System.out.println("Task 1: " + task1(map));
        System.out.println();
        System.out.println("Task 2: " + task2(map));
    }

    private static int task1(char[][] map) {
        int[] startCoordinates = getCoordinates(map, 'S');
        int[] finishCoordinates = getCoordinates(map, 'E');

        return bfsBottomTop(map, startCoordinates, finishCoordinates);
    }

    private static int task2(char[][] map) {
        int[] startCoordinates = getCoordinates(map, 'S');
        map[startCoordinates[0]][startCoordinates[1]] = 'a';
        int[] finishCoordinates = getCoordinates(map, 'E');

        return bfsTopBottom(map, finishCoordinates);
    }

    private static char[][] convertInput(List<String> inputData) {
        char[][] map = new char[inputData.size()][];
        int idx = 0;
        for (String line : inputData) {
            map[idx] = line.toCharArray();
            idx++;
        }

        return map;
    }

    private static int bfsTopBottom(char[][] map, int[] finishCoordinates) {
        int result = 0;
        map[finishCoordinates[0]][finishCoordinates[1]] = 'z';

        int[] neighbours = {0, 1, 0, -1, 0};

        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(finishCoordinates);
        Set<String> visited = new HashSet<>();
        visited.add(finishCoordinates[0] + "*" + finishCoordinates[1]);

        while (!queue.isEmpty()) {
            for (int i = queue.size(); i > 0; i--) {
                int[] curCoordinates = queue.removeFirst();

                if (map[curCoordinates[0]][curCoordinates[1]] == 'a') {
                    map[finishCoordinates[0]][finishCoordinates[1]] = 'E';
                    return result;
                }

                for (int n = 0; n < 4; n++) {
                    int nextX = curCoordinates[0] + neighbours[n];
                    int nextY = curCoordinates[1] + neighbours[n + 1];
                    if (nextX < 0 || nextY < 0 || nextX >= map.length || nextY >= map[0].length) {
                        continue;
                    }
                    int[] nextNeighbour = {nextX, nextY};

                    if (!visited.contains(nextX + "*" + nextY) &&
                            (map[curCoordinates[0]][curCoordinates[1]] -1 <= map[nextX][nextY])) {
                        visited.add(nextX + "*" + nextY);
                        queue.offerLast(nextNeighbour);
                    }
                }
            }

            result++;
        }

        map[finishCoordinates[0]][finishCoordinates[1]] = 'E';
        return -1;
    }

    private static int bfsBottomTop(char[][] map, int[] startCoordinates, int[] finishCoordinates) {
        int result = 0;

        map[startCoordinates[0]][startCoordinates[1]] = 'a';
        map[finishCoordinates[0]][finishCoordinates[1]] = 'z';

        int[] neighbours = {0, 1, 0, -1, 0};

        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(startCoordinates);
        Set<String> visited = new HashSet<>();
        visited.add(startCoordinates[0] + "*" + startCoordinates[1]);

        while (!queue.isEmpty()) {
            for (int i = queue.size(); i > 0; i--) {
                int[] curCoordinates = queue.removeFirst();

                if (curCoordinates[0] == finishCoordinates[0] && curCoordinates[1] == finishCoordinates[1]) {
                    map[startCoordinates[0]][startCoordinates[1]] = 'S';
                    map[finishCoordinates[0]][finishCoordinates[1]] = 'E';
                    return result;
                }

                for (int n = 0; n < 4; n++) {
                    int nextX = curCoordinates[0] + neighbours[n];
                    int nextY = curCoordinates[1] + neighbours[n + 1];
                    if (nextX < 0 || nextY < 0 || nextX >= map.length || nextY >= map[0].length) {
                        continue;
                    }
                    int[] nextNeighbour = {nextX, nextY};

                    if (!visited.contains(nextX + "*" + nextY) &&
                            (map[nextX][nextY] - 1 <= map[curCoordinates[0]][curCoordinates[1]])) {
                        visited.add(nextX + "*" + nextY);
                        queue.offerLast(nextNeighbour);
                    }
                }
            }

            result++;
        }

        map[startCoordinates[0]][startCoordinates[1]] = 'S';
        map[finishCoordinates[0]][finishCoordinates[1]] = 'E';
        return -1;
    }

    private static int[] getCoordinates(char[][] map, char target) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == target) {
                    return new int[]{row, col};
                }
            }
        }

        return null;
    }
}
