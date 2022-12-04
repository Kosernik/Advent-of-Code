package year2022;

import java.util.List;

public class Day04 {
    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day04.txt");

        task01(inputData);
        System.out.println("----");
        task02(inputData);
    }

    private static void task02(List<String> inputData) {
        int numberOfOverlappingPairs = 0;

        for (String pair : inputData) {
            long[][] intervals = getPairs(pair);

            if (isOverlappingPair(intervals)) numberOfOverlappingPairs++;
        }

        System.out.println("Task 2 : " + numberOfOverlappingPairs);
    }

    private static boolean isOverlappingPair(long[][] intervals) {
        long[] first = intervals[0];
        long[] second = intervals[1];

        return isOverlappingPair(first, second) || isOverlappingPair(second, first);
    }

    private static boolean isOverlappingPair(long[] first, long[] second) {
        return (second[0] <= first[0] && first[0] <= second[1]) || (second[0] <= first[1] && first[1] <= second[1]);
    }

    private static void task01(List<String> inputData) {
        int numberOfFullyContainingPairs = 0;

        for (String pair : inputData) {
            long[][] intervals = getPairs(pair);

            if (isFullyContainingPair(intervals)) numberOfFullyContainingPairs++;
        }

        System.out.println("Task 1 : " + numberOfFullyContainingPairs);
    }

    private static boolean isFullyContainingPair(long[][] intervals) {
        long[] first = intervals[0];
        long[] second = intervals[1];

        return isFullyContainingPair(first, second) || isFullyContainingPair(second, first);
    }

    private static boolean isFullyContainingPair(long[] first, long[] second) {
        return first[0] <= second[0] && second[1] <= first[1];
    }

    private static long[][] getPairs(String input) {
        String[] pairs = input.split(",");

        long[][] result = new long[2][];
        result[0] = getInterval(pairs[0]);
        result[1] = getInterval(pairs[1]);
        return result;
    }

    private static long[] getInterval(String input) {
        String[] stringInterval = input.split("-");
        long[] interval = new long[2];
        interval[0] = Long.parseLong(stringInterval[0]);
        interval[1] = Long.parseLong(stringInterval[1]);
        return interval;
    }

}
