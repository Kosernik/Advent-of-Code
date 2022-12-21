package year2022;

import java.util.*;

public class Day21 {

    public static void main(String[] args) {
        Day21 solution = new Day21();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day21.txt");

        Map<String, Monkey> monkeys = solution.parseInput(inputData);

        System.out.println("Task 1: " + solution.getRootNumber(monkeys));
    }

    private long getRootNumber(Map<String, Monkey> monkeys) {
        Map<Monkey, Long> monkeysWithNumbers = getMonkeysWithNumbers(monkeys);
        System.out.println(monkeys.get("root").name);

        return dfs(monkeys.get("root"), monkeysWithNumbers);
    }

    private long dfs(Monkey monkey, Map<Monkey, Long> monkeysWithNumbers) {
        if (monkeysWithNumbers.containsKey(monkey)) {
            return monkeysWithNumbers.get(monkey);
        }
        long first = dfs(monkey.first, monkeysWithNumbers);

        long second = dfs(monkey.second, monkeysWithNumbers);

        long number = doOperation(first, second, monkey.operation);
        monkey.number = number;
        monkeysWithNumbers.put(monkey, number);
        return number;
    }

    private Map<Monkey, Long> getMonkeysWithNumbers(Map<String, Monkey> monkeys) {
        Map<Monkey, Long> monkeysWithNumbers = new HashMap<>();

        for (Monkey monkey : monkeys.values()) {
            if (monkey.number != null) {
                monkeysWithNumbers.put(monkey, monkey.number);
            }
        }

        return monkeysWithNumbers;
    }

    private Map<String, Monkey> parseInput(List<String> inputData) {
        Map<String, Monkey> monkeys = new HashMap<>();

        //  dbpl: 5
        //  cczh: sllz + lgvd
        for (String line : inputData) {
            String[] splitLine = line.split("\\s+");

            String monkeyName = splitLine[0].substring(0, splitLine[0].length()-1);
            Monkey monkey = getOrCreate(monkeyName, monkeys);

            if (Character.isDigit(splitLine[1].charAt(0))) {
                monkey.number = Long.parseLong(splitLine[1]);
            } else {
                Monkey first = getOrCreate(splitLine[1], monkeys);
                Monkey second = getOrCreate(splitLine[3], monkeys);
                Operation operation = getOperation(splitLine[2]);
                monkey.first = first;
                monkey.second = second;
                monkey.operation = operation;

                monkey.first.listeners.add(monkey);
                monkey.second.listeners.add(monkey);
            }
        }

        return monkeys;
    }

    private long doOperation(long first, long second, Operation operation) {
        return switch (operation) {
            case ADD -> first + second;
            case SUBTRACT -> first - second;
            case MULTIPLY -> first * second;
            default -> first / second;
        };
    }

    private Operation getOperation(String operationName) {
        switch (operationName) {
            case "+" -> {
                return Operation.ADD;
            }
            case "-" -> {
                return Operation.SUBTRACT;
            }
            case "*" -> {
                return Operation.MULTIPLY;
            }
            case "/" -> {
                return Operation.DIVIDE;
            }
        }
        throw new IllegalArgumentException();
    }

    private Monkey getOrCreate(String name, Map<String, Monkey> monkeys) {
        if (monkeys.containsKey(name)) {
            return monkeys.get(name);
        }
        Monkey monkey = new Monkey(name);
        monkeys.put(name, monkey);
        return monkey;
    }

    static class Monkey {
        String name;
        Long number = null;
        Operation operation = null;
        Monkey first = null;
        Monkey second = null;
        List<Monkey> listeners = new ArrayList<>();

        Monkey (String name) {
            this.name = name;
        }
    }

    enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
}
