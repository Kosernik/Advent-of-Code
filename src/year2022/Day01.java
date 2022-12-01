package year2022;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Day01 {

    public static void main(String[] args) {
        Day01.task1();
        System.out.println("----");
        Day01.task2();
    }

    private static void task1() {
        try {
            FileInputStream file = new FileInputStream("src/year2022/Day01.txt");
            Scanner scanner = new Scanner(file);

            long maxCalories = 0;
            long curCalories = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank() || line.isEmpty()) {
                    maxCalories = Math.max(maxCalories, curCalories);
                    curCalories = 0L;
                } else {
                    curCalories += Long.parseLong(line);
                }
            }

            System.out.println("Result is: " + maxCalories);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void task2() {
        try {
            FileInputStream file = new FileInputStream("src/year2022/Day01.txt");
            Scanner scanner = new Scanner(file);

            List<Long> calories = new ArrayList<>();
            long curCalories = 0L;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank() || line.isEmpty()) {
                    calories.add(curCalories);
                    curCalories = 0L;
                } else {
                    curCalories += Long.parseLong(line);
                }
            }

            calories.sort(Collections.reverseOrder());
            System.out.println("Result is: " + (calories.get(0) + calories.get(1) + calories.get(2)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
