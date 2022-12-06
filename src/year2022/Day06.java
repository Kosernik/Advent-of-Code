package year2022;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day06 {

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day06.txt");

        task1(inputData);
        System.out.println("----");
        task2(inputData);
    }

    private static void task1(List<String> inputData) {
        String input = inputData.get(0);

        LinkedList<Character> marker = new LinkedList<>();
        marker.addLast(input.charAt(0));
        marker.addLast(input.charAt(1));
        marker.addLast(input.charAt(2));

        for (int i = 3; i < input.length(); i++) {
            marker.addLast(input.charAt(i));
            if (allCharsUnique(marker)) {
                System.out.println("Task 1: " + (i+1));
                return;
            }
            marker.removeFirst();
        }

        System.out.println("ERROR");
    }

    private static boolean allCharsUnique(List<Character> marker) {
        Set<Character> chars = new HashSet<>();
        for (Character m : marker) {
            if (!chars.add(m)) {
                return false;
            }
        }
        return true;
    }

    private static void task2(List<String> inputData) {
        String input = inputData.get(0);

        LinkedList<Character> marker = new LinkedList<>();

        for (int i = 0; i < 13; i++) {
            marker.addLast(input.charAt(i));
        }

        for (int i = 13; i < input.length(); i++) {
            marker.addLast(input.charAt(i));
            if (allCharsUnique(marker)) {
                System.out.println("Task 2: " + (i+1));
                return;
            }
            marker.removeFirst();
        }

        System.out.println("ERROR");
    }
}
