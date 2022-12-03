package year2022;

import java.util.List;

public class Day03 {

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day03.txt");

        task01(inputData);
        System.out.println("----");
        task02(inputData);
    }

    private static void task01(List<String> inputData) {
        int totalPriority = 0;

        for (String input : inputData) {
            totalPriority += getRucksackPriority(input);
        }

        System.out.println("The result for task 1 is: " + totalPriority);
    }

    private static int getRucksackPriority(String input) {
        int result = 0;
        boolean[] items = new boolean[52];

        char[] letters = input.toCharArray();
        for (int i = 0; i < (letters.length/2); i++) {
            if ('a' <= letters[i] && letters[i] <= 'z') {
                items[letters[i] - 'a'] = true;
            } else {
                items[letters[i] - 'A' + 26] = true;
            }
        }

        for (int i = letters.length/2; i < letters.length; i++) {
            if ('a' <= letters[i] && letters[i] <= 'z') {
                if (items[letters[i] - 'a']) {
                    result += (letters[i] - 'a' + 1);
                    items[letters[i] - 'a'] = false;
                }
            } else {
                if (items[letters[i] - 'A' + 26]) {
                    result += (letters[i] - 'A' + 26 + 1);
                    items[letters[i] - 'A' + 26] = false;
                }
            }
        }
        return result;
    }

    private static void task02(List<String> inputData) {
        int totalPriority = 0;

        for (int i = 0; i < inputData.size(); i += 3) {
            totalPriority += getGroupPriority(inputData.get(i), inputData.get(i+1), inputData.get(i+2));
        }

        System.out.println("The result for task 2 is: " + totalPriority);
    }

    private static int getGroupPriority(String firstRucksack, String secondRucksack, String thirdRucksack) {
        int[] items = new int[52];

        for (char item : firstRucksack.toCharArray()) {
            if ('a' <= item && item <= 'z') {
                items[item - 'a'] = 1;
            } else {
                items[item - 'A' + 26] = 1;
            }
        }

        for (char item : secondRucksack.toCharArray()) {
            if ('a' <= item && item <= 'z') {
                if (items[item - 'a'] == 1) {
                    items[item - 'a'] = 2;
                }
            } else {
                if (items[item - 'A' + 26] == 1) {
                    items[item - 'A' + 26] = 2;
                }
            }
        }

        for (char item : thirdRucksack.toCharArray()) {
            if ('a' <= item && item <= 'z') {
                if (items[item - 'a'] == 2) {
                    return (item - 'a' + 1);
                }
            } else {
                if (items[item - 'A' + 26] == 2) {
                    return (item - 'A' + 26 + 1);
                }
            }
        }

        return 0;
    }
}
