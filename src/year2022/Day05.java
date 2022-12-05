package year2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Day05 {
    private static final List<Deque<Character>> cargoShip = new ArrayList<>();

    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day05.txt");

        Deque<Character> stack1 = new ArrayDeque<>();
        cargoShip.add(stack1);
        stack1.push('W');
        stack1.push('R');
        stack1.push('F');

        Deque<Character> stack2 = new ArrayDeque<>();
        cargoShip.add(stack2);
        stack2.push('T');
        stack2.push('H');
        stack2.push('M');
        stack2.push('C');
        stack2.push('D');
        stack2.push('V');
        stack2.push('W');
        stack2.push('P');

        Deque<Character> stack3 = new ArrayDeque<>();
        cargoShip.add(stack3);
        stack3.push('P');
        stack3.push('M');
        stack3.push('Z');
        stack3.push('N');
        stack3.push('L');

        Deque<Character> stack4 = new ArrayDeque<>();
        cargoShip.add(stack4);
        stack4.push('J');
        stack4.push('C');
        stack4.push('H');
        stack4.push('R');

        Deque<Character> stack5 = new ArrayDeque<>();
        cargoShip.add(stack5);
        stack5.push('C');
        stack5.push('P');
        stack5.push('G');
        stack5.push('H');
        stack5.push('Q');
        stack5.push('T');
        stack5.push('B');

        Deque<Character> stack6 = new ArrayDeque<>();
        cargoShip.add(stack6);
        stack6.push('G');
        stack6.push('C');
        stack6.push('W');
        stack6.push('L');
        stack6.push('F');
        stack6.push('Z');

        Deque<Character> stack7 = new ArrayDeque<>();
        cargoShip.add(stack7);
        stack7.push('W');
        stack7.push('V');
        stack7.push('L');
        stack7.push('Q');
        stack7.push('Z');
        stack7.push('J');
        stack7.push('G');
        stack7.push('C');

        Deque<Character> stack8 = new ArrayDeque<>();
        cargoShip.add(stack8);
        stack8.push('P');
        stack8.push('N');
        stack8.push('R');
        stack8.push('F');
        stack8.push('W');
        stack8.push('T');
        stack8.push('V');
        stack8.push('C');

        Deque<Character> stack9 = new ArrayDeque<>();
        cargoShip.add(stack9);
        stack9.push('J');
        stack9.push('W');
        stack9.push('H');
        stack9.push('G');
        stack9.push('R');
        stack9.push('S');
        stack9.push('V');

//        task01(inputData);
        System.out.println("----");
        task02(inputData);
    }

    private static void task02(List<String> inputData) {
        Deque<Character> temp = new ArrayDeque<>();

        for (int i = 10; i < inputData.size(); i++) {
            int[] operation = getOperation(inputData.get(i));
            Deque<Character> source = cargoShip.get(operation[1]-1);
            Deque<Character> target = cargoShip.get(operation[2]-1);

            for (int j = 0; j < operation[0]; j++) {
                temp.push(source.pop());
            }
            while (!temp.isEmpty()) {
                target.push(temp.pop());
            }
        }

        System.out.println("Task 2");
        printTopElements();
    }

    private static void task01(List<String> inputData) {
        for (int i = 10; i < inputData.size(); i++) {
            int[] operation = getOperation(inputData.get(i));
            Deque<Character> source = cargoShip.get(operation[1]-1);
            Deque<Character> target = cargoShip.get(operation[2]-1);

            for (int j = 0; j < operation[0]; j++) {
                target.push(source.pop());
            }
        }

        System.out.println("Task 1");
        printTopElements();
    }

    private static int[] getOperation(String input) {
        int[] operation = new int[3];
        String[] commands = input.split(" ");

        operation[0] = Integer.parseInt(commands[1]);
        operation[1] = Integer.parseInt(commands[3]);
        operation[2] = Integer.parseInt(commands[5]);
        return operation;
    }

    private static void printTopElements() {
        for (Deque<Character> stack : cargoShip) {
            System.out.print(stack.peek());
        }
        System.out.println();
    }
}
