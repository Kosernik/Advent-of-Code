import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day12 {

    public static void main(String[] args) {
        Day12 solution = new Day12();

        List<String> input = parseInput("Day12");

        Map<String, Cave> tst = solution.createGraph(input);

        System.out.println("Number of distinct paths: " + solution.getNumberOfDistinctPaths(tst));
        System.out.println("Number of distinct paths: " + solution.getNumberOfTwiceVisits(tst));
    }

    /**
     * Counts the number of distinct paths in a graph. In each path at most 1 small cave can be visited twice.
     *
     * @param graph - a map of "NameOfACave" -> CaveObject
     * @return - the number of distinct paths.
     */
    public int getNumberOfTwiceVisits(Map<String, Cave> graph) {
        Cave startCave = graph.get("start");
        startCave.visited = true;
        Set<String> paths = new HashSet<>();
        List<String> curPath = new ArrayList<>();
        curPath.add(startCave.name);
        backtrackTwice(startCave, false, startCave, curPath, paths);

        startCave.visited = false;
        return paths.size();
    }
    private void backtrackTwice(Cave cave, boolean visitedSmall, Cave startCave, List<String> curPath, Set<String> paths) {
        if (cave.name.equals("end")) {
            String path = curPath.toString();
            paths.add(path);
            return;
        }

        for (Cave neighbour : cave.neighbours) {
            if (!neighbour.visited || neighbour.isRevisitable) {
                neighbour.visited = true;
                curPath.add(neighbour.name);
                backtrackTwice(neighbour, visitedSmall, startCave, curPath, paths);
                neighbour.visited = false;
                curPath.remove(curPath.size()-1);
            } else {
                if (!visitedSmall && !neighbour.name.equals(startCave.name)) {
                    curPath.add(neighbour.name);
                    backtrackTwice(neighbour, true, startCave, curPath, paths);
                    curPath.remove(curPath.size()-1);
                }
            }
        }
    }

    /**
     * Counts the number of distinct paths in a graph. Each small cave can be visited only once.
     *
     * @param graph - a map of "NameOfACave" -> CaveObject
     * @return - the number of distinct paths.
     */
    public int getNumberOfDistinctPaths(Map<String, Cave> graph) {
        graph.get("start").visited = true;

        int result = backtrackOnce(graph.get("start"));

        graph.get("start").visited = false;

        return result;
    }
    private int backtrackOnce(Cave cave) {
        if (cave.name.equals("end")) {
            return 1;
        }
        int paths = 0;

        for (Cave neighbour : cave.neighbours) {
            if (!neighbour.visited || neighbour.isRevisitable) {
                neighbour.visited = true;
                paths += backtrackOnce(neighbour);
                neighbour.visited = false;
            }
        }

        return paths;
    }

    /**
     * Creates a graph of caves from the input.
     *
     * @param input - a list of pairs of connected caves.
     * @return - the graph in a form of a map of "NameOfACave" -> CaveObject.
     */
    public Map<String, Cave> createGraph(List<String> input) {
        Map<String, Cave> graph = new HashMap<>();

        for (String line : input) {
            String[] nodes = line.split("-");

            if (!graph.containsKey(nodes[0])) {
                Cave cave = new Cave(nodes[0]);
                graph.put(nodes[0], cave);
            }
            if (!graph.containsKey(nodes[1])) {
                Cave cave = new Cave(nodes[1]);
                graph.put(nodes[1], cave);
            }

            Cave cave0 = graph.get(nodes[0]);
            Cave cave1 = graph.get(nodes[1]);

            cave0.addToNeighs(cave1);
            cave1.addToNeighs(cave0);
        }

        return graph;
    }

    /**
     * Reads the input file.
     * @return - a list of Strings.
     */
    private static List<String> parseInput(String filename) {
        List<String> input = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/" + filename + ".txt"))){
            String line = br.readLine();

            while (line != null) {
                input.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return input;
    }


    private static class Cave {
        final String name;
        boolean visited = false;
        final boolean isRevisitable ;
        final List<Cave> neighbours;

        public Cave(String name) {
            this.name = name;
            isRevisitable = Character.isUpperCase(name.charAt(0));
            neighbours = new ArrayList<>();
        }

        public void addToNeighs(Cave neighbour) {
            neighbours.add(neighbour);
        }

        @Override
        public String toString() {
            return "Cave{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
