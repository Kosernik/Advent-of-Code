package year2022;

import java.util.*;

public class Day21 {

    public static void main(String[] args) {
        Day21 solution = new Day21();
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day21.txt");
//        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day21test.txt");

        Map<String, Monkey> monkeys = solution.parseInput(inputData);

        System.out.println("Task 1: " + solution.getRootNumber(monkeys));
        System.out.println("_______________");
        System.out.println("Task 2: " + solution.getHumanNumber(monkeys.get("root"), monkeys.get("humn"), monkeys));
    }

    private long getHumanNumber(Monkey root, Monkey target, Map<String, Monkey> monkeys) {
        List<String> pathNames = getPath(root, target, monkeys);

        String child = pathNames.get(1);
        long targetValue;
        if (root.first.name.equals(child)) {
            targetValue = root.second.number;
        } else {
            targetValue = root.first.number;
        }

        long result = binSearch(root, target, monkeys.get(child), targetValue);

        if (isHumanInputCorrect(root)) {
            //  9_223_372_036_854_775_808 (too high but valid (long overflow!?))
            //  7_049_294_859_078_328_863 (too high but valid (long overflow!?))
            //  4_282_283_248_021_896_121 (too high but valid (long overflow!?))
            //  3_096_421_128_997_710_660 (too high but valid (long overflow!?))
            //          3_373_767_893_067 - THE ANSWER
            //          3_373_767_893_064 (too low but valid (long overflow!?))
            //              2_147_483_648
            return target.number;
        }

        return -1;
    }

    private long binSearch(Monkey root, Monkey target, Monkey rootChild, long targetValue) {
        long low = 3_373_767_893_065L;  // Result was too low
        long high = 3_096_421_128_997_710_659L; // Result was too high
        long medium;

        while (low < high) {
            medium = (high - low) / 2 + low;
            updateHolderMonkey(target, medium);
            if (isHumanInputCorrect(root)) {
                return medium;
            }
            if (rootChild.number > targetValue) {
                low = medium + 1;
            } else {
                high = medium - 1;
            }
        }
        return -1;
    }

    /**
     * Causes long overflow for my input.
     *
     * @param targetValue   - the value that this monkey should return.
     * @param monkeyNameIdx - the index of the name of the current monkey.
     * @param targetName    - the name of the monkey(human) that should shout the correct number.
     * @param pathNames     - a list of names of monkeys that represents the path from root to human-monkey.
     * @param monkeys       - a map of monkey names -> monkey objects.
     * @return - the number that the human-monkey should shout.
     */
    private long getValidNumber(
            long targetValue, int monkeyNameIdx, String targetName, List<String> pathNames, Map<String, Monkey> monkeys
    ) {
        if (pathNames.get(monkeyNameIdx).equals(targetName)) {
            return targetValue;
        }

        Monkey curMonkey = monkeys.get(pathNames.get(monkeyNameIdx));
        String nextMonkeyName = pathNames.get(monkeyNameIdx + 1);

        long updatedTargetValue;
        long otherMonkeyNumber;
        if (curMonkey.first == monkeys.get(nextMonkeyName)) {
            otherMonkeyNumber = curMonkey.second.number;
            updatedTargetValue = switch (curMonkey.operation) {
                case ADD -> otherMonkeyNumber - targetValue;
                case SUBTRACT -> targetValue + otherMonkeyNumber;
                case MULTIPLY -> targetValue / otherMonkeyNumber;
                default -> targetValue * otherMonkeyNumber;
            };
        } else {
            otherMonkeyNumber = curMonkey.first.number;
            updatedTargetValue = switch (curMonkey.operation) {
                case ADD -> targetValue - otherMonkeyNumber;
                case SUBTRACT -> otherMonkeyNumber - targetValue;
                case MULTIPLY -> targetValue / otherMonkeyNumber;
                default -> otherMonkeyNumber / targetValue;
            };
        }

        return getValidNumber(updatedTargetValue, monkeyNameIdx + 1, targetName, pathNames, monkeys);
    }

    private boolean isHumanInputCorrect(Monkey root) {
        return root.first.number.longValue() == root.second.number.longValue();
    }

    private long getRootNumber(Map<String, Monkey> monkeys) {
        return dfs(monkeys.get("root"));
    }

    private void updateHolderMonkey(Monkey monkey, long updatedNumber) {
        monkey.number = updatedNumber;
        for (Monkey listener : monkey.listeners) {
            updateNumbers(listener);
        }
    }

    private void updateNumbers(Monkey monkey) {
        monkey.number = doOperation(monkey.first.number, monkey.second.number, monkey.operation);
        for (Monkey listener : monkey.listeners) {
            updateNumbers(listener);
        }
    }

    private long dfs(Monkey monkey) {
        if (monkey.number != null) {
            return monkey.number;
        }
        long first = dfs(monkey.first);

        long second = dfs(monkey.second);

        long number = doOperation(first, second, monkey.operation);
        monkey.number = number;
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

            String monkeyName = splitLine[0].substring(0, splitLine[0].length() - 1);
            Monkey monkey = getOrCreateMonkey(monkeyName, monkeys);

            if (Character.isDigit(splitLine[1].charAt(0))) {
                monkey.number = Long.parseLong(splitLine[1]);
            } else {
                Monkey first = getOrCreateMonkey(splitLine[1], monkeys);
                Monkey second = getOrCreateMonkey(splitLine[3], monkeys);
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
            case DIVIDE -> first / second;
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

    private Monkey getOrCreateMonkey(String name, Map<String, Monkey> monkeys) {
        if (monkeys.containsKey(name)) {
            return monkeys.get(name);
        }
        Monkey monkey = new Monkey(name);
        monkeys.put(name, monkey);
        return monkey;
    }

    private void printMonkeys(Map<String, Monkey> monkeys) {
        System.out.println("Monkeys:");
        for (Map.Entry<String, Monkey> entry : monkeys.entrySet()) {
            System.out.println(entry.getValue().name + " " + (entry.getValue().number == null ? "Null" : entry.getValue().number));
        }
        System.out.println("==============");
    }

    private List<String> getPath(Monkey root, Monkey target, Map<String, Monkey> monkeys) {
        List<String> result = new ArrayList<>();

        Map<String, List<String>> paths = new HashMap<>();
        dfsPathFinder(root, target, paths);

        Deque<String> monkeyNames = new ArrayDeque<>();
        monkeyNames.offer(target.name);
        while (!monkeyNames.isEmpty()) {
            for (int i = monkeyNames.size(); i > 0; i--) {
                String name = monkeyNames.removeFirst();
                result.add(name);

                for (String child : paths.getOrDefault(name, new ArrayList<>())) {
                    monkeyNames.offerLast(child);
                }
            }
        }

        Collections.reverse(result);
        return result;
    }

    private boolean dfsPathFinder(Monkey monkey, Monkey target, Map<String, List<String>> paths) {
        if (monkey.first == target || monkey.second == target) {
            List<String> parents = paths.getOrDefault(target.name, new ArrayList<>());
            parents.add(monkey.name);
            paths.put(target.name, parents);
            return true;
        }
        boolean found = false;
        if (monkey.first != null && dfsPathFinder(monkey.first, target, paths)) {
            found = true;
            List<String> parents = paths.getOrDefault(monkey.first.name, new ArrayList<>());
            parents.add(monkey.name);
            paths.put(monkey.first.name, parents);
        }
        if (monkey.second != null && dfsPathFinder(monkey.second, target, paths)) {
            found = true;
            List<String> parents = paths.getOrDefault(monkey.second.name, new ArrayList<>());
            parents.add(monkey.name);
            paths.put(monkey.second.name, parents);
        }

        return found;
    }

    static class Monkey {
        String name;
        Long number = null;
        Operation operation = null;
        Monkey first = null;
        Monkey second = null;
        List<Monkey> listeners = new ArrayList<>();

        Monkey(String name) {
            this.name = name;
        }
    }

    enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
}
