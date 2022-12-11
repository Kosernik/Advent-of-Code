package year2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {
        Day11 solution = new Day11();

        List<Monkey> monkeys = solution.getMonkeys();
        solution.simulation(monkeys, 20);

        monkeys = solution.getMonkeys();

        for (Monkey monkey : monkeys) {
            monkey.changeWorryLevelReduction(1L);
        }

        solution.simulation(monkeys, 10_000);
    }

    private void simulation(List<Monkey> monkeys, int numberOfRounds) {
        for (int i = 0; i < numberOfRounds; i++) {
            for (Monkey monkey : monkeys) {
                monkey.sendItems();
            }
        }

        List<Long> inspections = new ArrayList<>();
        for (Monkey monkey : monkeys) {
            inspections.add(monkey.getNumberOfInspections());
        }

        inspections.sort(Collections.reverseOrder());

        System.out.println("Result for " + numberOfRounds + " rounds is: " + (inspections.get(0) * inspections.get(1)));
    }

    private List<Monkey> getMonkeys() {
        List<Monkey> monkeys = new ArrayList<>();

        Monkey monkey0 = new Monkey(Operation.MULTIPLY, 11, 7);
        monkey0.addItems(new int[]{66, 79});

        Monkey monkey1 = new Monkey(Operation.MULTIPLY, 17, 13);
        monkey1.addItems(new int[]{84, 94, 94, 81, 98, 75});

        Monkey monkey2 = new Monkey(Operation.ADD, 8, 5);
        monkey2.addItems(new int[]{85, 79, 59, 64, 79, 95, 67});

        Monkey monkey3 = new Monkey(Operation.ADD, 3, 19);
        monkey3.addItems(new int[]{70});

        Monkey monkey4 = new Monkey(Operation.ADD, 4, 2);
        monkey4.addItems(new int[]{57, 69, 78, 78});

        Monkey monkey5 = new Monkey(Operation.ADD, 7, 11);
        monkey5.addItems(new int[]{65, 92, 60, 74, 72});

        Monkey monkey6 = new Monkey(Operation.SQUARE, 0, 17);
        monkey6.addItems(new int[]{77, 91, 91});

        Monkey monkey7 = new Monkey(Operation.ADD, 6, 3);
        monkey7.addItems(new int[]{76, 58, 57, 55, 67, 77, 54, 99});

        monkey0.addMonkeys(monkey6, monkey7);
        monkey1.addMonkeys(monkey5, monkey2);
        monkey2.addMonkeys(monkey4, monkey5);
        monkey3.addMonkeys(monkey6, monkey0);
        monkey4.addMonkeys(monkey0, monkey3);
        monkey5.addMonkeys(monkey3, monkey4);
        monkey6.addMonkeys(monkey1, monkey7);
        monkey7.addMonkeys(monkey2, monkey1);

        monkeys.add(monkey0);
        monkeys.add(monkey1);
        monkeys.add(monkey2);
        monkeys.add(monkey3);
        monkeys.add(monkey4);
        monkeys.add(monkey5);
        monkeys.add(monkey6);
        monkeys.add(monkey7);

        long commonReductionFactor = 1L;
        for (Monkey monkey : monkeys) {
            commonReductionFactor *= monkey.divisible;
        }

        for (Monkey monkey : monkeys) {
            monkey.changeCommonReductionFactor(commonReductionFactor);
        }

        return monkeys;
    }

    private List<Monkey> getTestMonkeys() {
        List<Monkey> testMonkeys = new ArrayList<>();

        Monkey monkey0 = new Monkey(Operation.MULTIPLY, 19, 23);
        Monkey monkey1 = new Monkey(Operation.ADD, 6, 19);
        Monkey monkey2 = new Monkey(Operation.SQUARE, 0, 13);
        Monkey monkey3 = new Monkey(Operation.ADD, 3, 17);

        monkey0.addMonkeys(monkey2, monkey3);
        monkey0.addItems(new int[]{79, 98});

        monkey1.addMonkeys(monkey2, monkey0);
        monkey1.addItems(new int[]{54, 65, 75, 74});

        monkey2.addMonkeys(monkey1, monkey3);
        monkey2.addItems(new int[]{79, 60, 97});

        monkey3.addMonkeys(monkey0, monkey1);
        monkey3.addItems(new int[]{74});

        testMonkeys.add(monkey0);
        testMonkeys.add(monkey1);
        testMonkeys.add(monkey2);
        testMonkeys.add(monkey3);

        long commonReductionFactor = 1L;
        for (Monkey monkey : testMonkeys) {
            commonReductionFactor *= monkey.divisible;
        }
        for (Monkey monkey : testMonkeys) {
            monkey.changeCommonReductionFactor(commonReductionFactor);
        }

        return testMonkeys;
    }

    static class Monkey {
        LinkedList<Long> items;
        long divisible;
        Operation operation;
        long operator;
        Monkey monkeyTrue;
        Monkey monkeyFalse;

        private long numberOfInspections = 0;

        private long worryLevelReduction = 3;
        private long commonReductionFactor = 1;

        public Monkey(Operation operation, long operator, long divisible) {
            this.divisible = divisible;
            this.operation = operation;
            this.operator = operator;

            items = new LinkedList<>();
        }

        public void addMonkeys(Monkey monkeyTrue, Monkey monkeyFalse) {
            this.monkeyTrue = monkeyTrue;
            this.monkeyFalse = monkeyFalse;
        }

        public void addItems(int[] items) {
            for (int item : items) {
                addItem(item);
            }
        }

        public void addItem(long itemLevel) {
            items.addLast(itemLevel);
        }

        public void sendItems() {
            for (int i = items.size(); i > 0; i--) {
                // Updating the number of inspections
                numberOfInspections++;

                long curItem = items.removeFirst();
                long worryLevel = getNewWorryLevel(curItem);

                sendItem(worryLevel);
            }
        }

        private void sendItem(long item) {
            if (testItem(item)) {
                monkeyTrue.addItem(item);
            } else {
                monkeyFalse.addItem(item);
            }
        }

        private long getNewWorryLevel(long item) {
            long worryLevel;
            if (this.operation == Operation.ADD) {
                worryLevel = item + operator;
            } else if (this.operation == Operation.MULTIPLY) {
                worryLevel = item * operator;
            } else if (this.operation == Operation.SQUARE) {
                worryLevel = item * item;
            } else {
                throw new UnsupportedOperationException();
            }

            worryLevel %= commonReductionFactor;

            return worryLevel / worryLevelReduction;
        }

        private boolean testItem(long item) {
            return item % divisible == 0;
        }

        public long getNumberOfInspections() {
            return numberOfInspections;
        }

        public void changeWorryLevelReduction(long newWorryLevelReduction) {
            this.worryLevelReduction = newWorryLevelReduction;
        }

        public void changeCommonReductionFactor(long newCommonReductionFactor) {
            this.commonReductionFactor = newCommonReductionFactor;
        }
    }

    enum Operation {
        ADD, MULTIPLY, SQUARE
    }
}
