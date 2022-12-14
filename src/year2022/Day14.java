package year2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {
    private static final char STONE = '#';
    private static final char SAND = 'o';
    private static final char START = '+';
    private static final char EMPTY = '.';

    public static void main(String[] args) {
        Day14 solution = new Day14();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day14.txt");

        List<List<int[]>> listOfStones = solution.getStones(inputData);

        int[] dimensions1 = solution.getDimensions(listOfStones);    //  left, right, top, bottom

        char[][] mapTask1 = solution.getMap(dimensions1);
        mapTask1[0][500] = START;
        solution.fillMap(mapTask1, listOfStones);

        int result1 = solution.simulation1(mapTask1);
        System.out.println("Task 1: " + result1);

        int[] dimensions2 = Arrays.copyOf(dimensions1, 4);    //  left, right, top, bottom
        dimensions2[1] += 500;
        dimensions2[3] += 2;

        char[][] mapTask2 = solution.getMap(dimensions2);
        mapTask2[0][500] = START;
        solution.fillMap(mapTask2, listOfStones);
        solution.addFloor(mapTask2);

        int result2 = solution.simulation2(mapTask2);
        System.out.println("Task 2: " + result2);
    }

    private int simulation1(char[][] map) {
        int count = 0;

        while (true) {
            int[] coordinate = {0, 500};
            if (!moveStone(coordinate, map)) {
                return count;
            }
            count++;
        }
    }

    private int simulation2(char[][] map) {
        int count = 0;

        while (true) {
            int[] coordinate = {0, 500};
            if (map[0][500] == SAND) {
                return count;
            }
            if (!moveStone(coordinate, map)) {
                return count;
            }
            count++;
        }
    }

    private void addFloor(char[][] map) {
        for (int i = 0; i < map[0].length; i++) {
            map[map.length-1][i] = STONE;
        }
    }

    private boolean moveStone(int[] stone, char[][] map) {
        //  stone[0] = vertical coordinate, stone[1] = horizontal coordinate
        if (stone[0]+1 >= map.length) {
            return false;
        }
        if (map[stone[0]+1][stone[1]] == EMPTY) {
            stone[0]++;
            return moveStone(stone, map);
        }
        if (map[stone[0]+1][stone[1]] == STONE || map[stone[0]+1][stone[1]] == SAND) {
            if (stone[1] == 0) {
                return false;
            }
            if (map[stone[0]+1][stone[1]-1] == EMPTY) {
                stone[0]++;
                stone[1]--;
                return moveStone(stone, map);
            }
            if (stone[1]+1 >= map[0].length) {
                return false;
            }
            if (map[stone[0]+1][stone[1]+1] == EMPTY) {
                stone[0]++;
                stone[1]++;
                return moveStone(stone, map);
            }
        }
        map[stone[0]][stone[1]] = SAND;
        return true;
    }

    private void fillMap(char[][] map, List<List<int[]>> listOfStones) {
        for (List<int[]> stones : listOfStones) {
            //  stone[0] = vertical coordinate, stone[1] = horizontal coordinate
            int[] prevStone = stones.get(0);
            map[prevStone[0]][prevStone[1]] = STONE;

            for (int i = 1; i < stones.size(); i++) {
                int[] curStone = stones.get(i);

                if (prevStone[0] == curStone[0]) {
                    for (int coord = Math.min(prevStone[1], curStone[1]); coord <= Math.max(prevStone[1], curStone[1]); coord++) {
                        map[prevStone[0]][coord] = STONE;
                    }
                } else {
                    for (int coord = Math.min(prevStone[0], curStone[0]); coord <= Math.max(prevStone[0], curStone[0]); coord++) {
                        map[coord][prevStone[1]] = STONE;
                    }
                }

                prevStone = curStone;
            }
        }
    }

    private char[][] getMap(int[] dimensions) {
        //  dimensions = { left, right, top, bottom }
        char[][] map = new char[dimensions[3] + 1][dimensions[1] + 1];
        for (char[] line : map) {
            Arrays.fill(line, EMPTY);
        }
        return map;
    }

    private int[] getDimensions(List<List<int[]>> listOfStones) {
        //  left, right, top, bottom
        int[] dimensions = new int[4];
        dimensions[1] = 500;

        for (List<int[]> stones : listOfStones) {
            for (int[] stone : stones) {
                //  stone[0] = vertical coordinate, stone[1] = horizontal coordinate
                dimensions[0] = Math.min(dimensions[0], stone[1]);
                dimensions[1] = Math.max(dimensions[1], stone[1]);
                dimensions[2] = Math.min(dimensions[2], stone[0]);
                dimensions[3] = Math.max(dimensions[3], stone[0]);
            }
        }

        return dimensions;
    }

    private List<List<int[]>> getStones(List<String> inputData) {
        List<List<int[]>> listOfStones = new ArrayList<>();

        for (String input : inputData) {
            List<int[]> stones = new ArrayList<>();
            String[] coordinates = input.split("->");

            for (String coordinate : coordinates) {
                coordinate = coordinate.strip();
                String[] splited = coordinate.split(",");

                //  stone[0] = vertical coordinate, stone[1] = horizontal coordinate
                int[] stone = {Integer.parseInt(splited[1]), Integer.parseInt(splited[0])};
                if (stone[0] < 0 || stone[1] < 0) {
                    System.out.println("Found negative coordinate " + Arrays.toString(stone));
                }
                stones.add(stone);
            }
            listOfStones.add(stones);
        }
        return listOfStones;
    }
}
