package year2022;

import java.util.*;

public class Day15 {
    public final static char BEACON = 'B';
    public final static char SENSOR = 'S';
    public final static char EMPTY = '.';
    public final static char COVERED = '#';

    //  { lowX, lowY, highX, highY }
    private final int[] dimensions = new int[4];

    private int offsetX = 0;   //  horizontal
    private int offsetY = 0;   //  vertical

    private char[][] map;


    Map<Integer, List<Device>> xCoordinates = new HashMap<>();
    Map<Integer, List<Device>> yCoordinates = new HashMap<>();

    public static void main(String[] args) {
        Day15 solution = new Day15();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day15.txt");

        List<Pair> pairs = solution.parseInput(inputData);


//        solution.map = solution.getMap(pairs);
//        solution.fillMap(pairs);
//        solution.updateMap(pairs);
//        solution.printMap();
//        System.out.println("Dimensions: " + Arrays.toString(solution.dimensions));
//        System.out.println("Offsets: " + solution.offsetX + " - x, y - " + solution.offsetY);


        System.out.println("Task 1: " + solution.task1(pairs, 2000000));

        System.out.println("Task 2: " + solution.task2(pairs, 0, 4_000_000));
    }

    private int task1(List<Pair> pairs, int line) {
        Set<String> coveredCells = new HashSet<>();

        for (Pair pair : pairs) {
            int distance = getDistance(pair.sensor, pair.beacon);
            if ((pair.sensor.y - distance) <= line && line <= (pair.sensor.y + distance)) {
                int width = distance - (Math.abs(pair.sensor.y - line) - 1);

                for (int i = 0; i < width; i++) {
                    int xLeft = pair.sensor.x - i;
                    if (isCellEmpty(xLeft, line)) {
                        coveredCells.add(xLeft + "*" + line);
                    }
                    int xRight = pair.sensor.x + i;
                    if (isCellEmpty(xRight, line)) {
                        coveredCells.add(xRight + "*" + line);
                    }
                }
            }
        }

        return coveredCells.size();
    }

    private long task2(List<Pair> pairs, int low, int high) {
        for (int line = high; line >= low; line--) {
            List<int[]> coveredIntervals = new ArrayList<>();
            for (Pair pair : pairs) {
                if (pair.sensor.y + pair.distance >= low && pair.sensor.y - pair.distance <= high &&
                        pair.sensor.x + pair.distance >= low && pair.sensor.x - pair.distance <= high) {
                    int[] interval = pair.sensor.intervalOfCoveredCellsInLine(pair.distance, line);
                    coveredIntervals.add(interval);
                }
            }

            coveredIntervals.sort((a, b) -> Integer.compare(a[0], b[0]));

            int finish = coveredIntervals.get(0)[1];

            for (int i = 1; i < coveredIntervals.size(); i++) {
                int[] curInterval = coveredIntervals.get(i);
                if (finish+1 < curInterval[0] && finish+1 <= high) {
                    System.out.println("Found cell at line " + line + ", x: " + (finish+1));
                    return (finish + 1L) * 4_000_000L + line;
                }
                finish = Math.max(finish, curInterval[1]);
            }
        }

        return -1;
    }

    private boolean isUncoveredCell(List<Pair> pairs, int x, int y) {
        return !isCellCoveredByAnySensor(pairs, x, y) && areNeighboursCovered(pairs, x, y);
    }

    private boolean isCellEmpty(int x, int y) {
        for (Device device : yCoordinates.getOrDefault(y, new ArrayList<>())) {
            if (device.x == x) {
                return false;
            }
        }
        return true;
    }

    private boolean areNeighboursCovered(List<Pair> pairs, int x, int y) {
        int[] neighbours = {0, 1, 0, -1, 0};
        for (int n = 0; n < 4; n++) {
            if (!isCellCoveredByAnySensor(pairs, x + neighbours[n], y + neighbours[n + 1])) {
                return false;
            }
        }
        if (!isCellCoveredByAnySensor(pairs, x - 1, y - 1)) {
            return false;
        }
        if (!isCellCoveredByAnySensor(pairs, x + 1, y - 1)) {
            return false;
        }
        if (!isCellCoveredByAnySensor(pairs, x - 1, y + 1)) {
            return false;
        }
        if (!isCellCoveredByAnySensor(pairs, x + 1, y + 1)) {
            return false;
        }

        return true;
    }

    private boolean isCellCoveredByAnySensor(List<Pair> pairs, int x, int y) {
        for (Pair pair : pairs) {
            if (isCellCoveredBySensor(pair, x, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCellCoveredBySensor(Pair pair, int x, int y) {
        int sensorRadius = pair.distance;
        int distance = getDistance(pair.sensor, new Beacon(x, y));

        return distance <= sensorRadius;
    }

    private void updateMap(List<Pair> pairs) {
        for (Pair pair : pairs) {
            updateSignalCoverage(pair);
        }
    }

    private void updateSignalCoverage(Pair pair) {
        Sensor sensor = pair.sensor;
        Beacon beacon = pair.beacon;
        int distance = pair.distance;

        int curWidth = 1;
        for (int i = sensor.y - distance - offsetY; i < sensor.y - offsetY; i++, curWidth++) {
            if (i < 0) continue;

            for (int j = 0; j < curWidth; j++) {
                if (this.map[i][Math.max(sensor.x - j - offsetX, 0)] == EMPTY) {
                    this.map[i][Math.max(sensor.x - j - offsetX, 0)] = COVERED;
                }
                if (this.map[i][Math.min(sensor.x + j - offsetX, map[0].length - 1)] == EMPTY) {
                    this.map[i][Math.min(sensor.x + j - offsetX, map[0].length - 1)] = COVERED;
                }
            }
        }
        for (int i = 0; i < distance; i++) {
            if (this.map[sensor.y - offsetY][Math.max(sensor.x - i - 1 - offsetX, 0)] == EMPTY) {
                this.map[sensor.y - offsetY][Math.max(sensor.x - i - 1 - offsetX, 0)] = COVERED;
            }
            if (this.map[sensor.y - offsetY][Math.min(sensor.x + i + 1 - offsetX, map[0].length - 1)] == EMPTY) {
                this.map[sensor.y - offsetY][Math.min(sensor.x + i + 1 - offsetX, map[0].length - 1)] = COVERED;
            }
        }
        curWidth = 1;
        for (int i = sensor.y + distance - offsetY; i > sensor.y - offsetY; i--, curWidth++) {
            if (i >= this.map.length) continue;

            for (int j = 0; j < curWidth; j++) {
                if (this.map[i][Math.max(sensor.x - j - offsetX, 0)] == EMPTY) {
                    this.map[i][Math.max(sensor.x - j - offsetX, 0)] = COVERED;
                }
                if (this.map[i][Math.min(sensor.x + j - offsetX, map[0].length - 1)] == EMPTY) {
                    this.map[i][Math.min(sensor.x + j - offsetX, map[0].length - 1)] = COVERED;
                }
            }
        }
    }

    private void fillMap(List<Pair> pairs) {
        for (Pair pair : pairs) {
            this.map[pair.sensor.y - offsetY][pair.sensor.x - offsetX] = SENSOR;
            this.map[pair.beacon.y - offsetY][pair.beacon.x - offsetX] = BEACON;
        }
    }

    private char[][] getMap(List<Pair> pairs) {
        updateDimensions(pairs);

        char[][] map = new char[dimensions[3] - offsetY + 1][dimensions[2] - offsetX + 1];
        for (char[] line : map) {
            Arrays.fill(line, EMPTY);
        }
        return map;
    }

    private void updateOffsets() {
        offsetX = dimensions[0];
        offsetY = dimensions[1];
    }

    private void updateDimensions(List<Pair> pairs) {
        int maxDistance = 0;
        for (Pair p : pairs) {
            dimensions[0] = Math.min(dimensions[0], p.sensor.x);
            dimensions[0] = Math.min(dimensions[0], p.beacon.x);

            dimensions[2] = Math.max(dimensions[2], p.sensor.x);
            dimensions[2] = Math.max(dimensions[2], p.beacon.x);

            dimensions[1] = Math.min(dimensions[1], p.sensor.y);
            dimensions[1] = Math.min(dimensions[1], p.beacon.y);

            dimensions[3] = Math.max(dimensions[3], p.sensor.y);
            dimensions[3] = Math.max(dimensions[3], p.beacon.y);

            maxDistance = Math.max(maxDistance, p.distance);
        }

        dimensions[0] -= maxDistance;
        dimensions[1] -= maxDistance;
        dimensions[2] += maxDistance;
        dimensions[3] += maxDistance;

        updateOffsets();
    }

    private int getDistance(Sensor sensor, Beacon beacon) {
        return Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
    }

    private List<Pair> parseInput(List<String> inputData) {
        List<Pair> pairs = new ArrayList<>();

        for (String line : inputData) {
            int sensorX;
            int sensorY;
            int beaconX;
            int beaconY;

            int idxX = line.indexOf("x=") + 2;
            int nextSpace = line.indexOf(',', idxX);
            sensorX = Integer.parseInt(line.substring(idxX, nextSpace));

            int idxY = line.indexOf("y=") + 2;
            nextSpace = line.indexOf(':', idxY);
            sensorY = Integer.parseInt(line.substring(idxY, nextSpace));

            idxX = line.indexOf("x=", nextSpace) + 2;
            nextSpace = line.indexOf(',', idxX);
            beaconX = Integer.parseInt(line.substring(idxX, nextSpace));

            idxY = line.indexOf("y=", nextSpace) + 2;
            beaconY = Integer.parseInt(line.substring(idxY));

            Sensor sensor = new Sensor(sensorX, sensorY);
            Beacon beacon = new Beacon(beaconX, beaconY);

            addToCoordinates(sensor);
            addToCoordinates(beacon);

            Pair pair = new Pair(sensor, beacon);
            pairs.add(pair);
        }

        return pairs;
    }

    private void addToCoordinates(Device device) {
        List<Device> curListX = xCoordinates.getOrDefault(device.x, new ArrayList<>());
        curListX.add(device);
        xCoordinates.put(device.x, curListX);

        List<Device> curListY = yCoordinates.getOrDefault(device.y, new ArrayList<>());
        curListY.add(device);
        yCoordinates.put(device.y, curListY);
    }

    private void printMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println(" " + i);
        }
        for (int i = 0; i < map[0].length; i++) {
            System.out.print(i % 10);
        }
        System.out.println();
    }

    abstract static class Device {
        int x;
        int y;
    }

    static class Pair {
        Sensor sensor;
        Beacon beacon;
        int distance;

        Pair(Sensor sensor, Beacon beacon) {
            this.sensor = sensor;
            this.beacon = beacon;
            distance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
        }

        @Override
        public String toString() {
            return "Sensor " + sensor.x + " " + sensor.y + ", beacon " + beacon.x + " " + beacon.y;
        }
    }

    static class Sensor extends Device {
        Sensor(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int[] intervalOfCoveredCellsInLine(int radius, int line) {
            int[] interval = new int[2];
            int width = radius - Math.abs(this.y - line);
            interval[0] = this.x - width;
            interval[1] = this.x + width;
            return interval;
        }
    }

    static class Beacon extends Device {
        Beacon(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
