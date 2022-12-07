package year2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day07 {

    private static final MyDirectory root = new MyDirectory("ROOT");

    private static final List<MyDirectory> validFolders = new ArrayList<>();
    public static void main(String[] args) {
        List<String> inputData = ReadFileUtil.readFile("src/year2022/Day07.txt");

        buildDirectories(inputData);
        updateSizes(root);
        System.out.println("Done building the tree");
        System.out.println("Total spase: " + root.totalSize);
        getSumOfSizes(100000L);

        nameOfTheSmallestBigFolder(70000000, 30000000);
    }


    private static void nameOfTheSmallestBigFolder(long totalDiscSpace, long freeSpaceNeeded) {
        long needToDelete = root.totalSize - (totalDiscSpace - freeSpaceNeeded);

        getValidFolders(needToDelete);

        long bestSize = Long.MAX_VALUE;
        String name = "noname";

        for (MyDirectory directory : validFolders) {
            if (directory.totalSize < bestSize) {
                bestSize = directory.totalSize;
                name = directory.name;
            }
        }

        System.out.println("Task 2: " + name + " " + bestSize);
    }

    private static void getValidFolders(long needToDelete) {
        treeTraversalBigSize(root, needToDelete);
    }

    private static void treeTraversalBigSize(MyDirectory node, long needToDelete) {
        if (node == null || node.totalSize < needToDelete) return;

        validFolders.add(node);

        for (MyDirectory children : node.folders.values()) {
            treeTraversalBigSize(children, needToDelete);
        }
    }

    private static void getSumOfSizes(long maxSize) {
        long sum = treeTraversalSmallSizes(root, maxSize);

        System.out.println("Task 1: " + sum);
    }

    private static long treeTraversalSmallSizes(MyDirectory node, long maxSize) {
        if (node == null) return 0L;

        long sum = 0L;
        if (node.totalSize <= maxSize) {
            sum = node.totalSize;
        }

        for (MyDirectory children : node.folders.values()) {
            sum += treeTraversalSmallSizes(children, maxSize);
        }

        return sum;
    }

    private static void buildDirectories(List<String> inputData) {
        MyDirectory currentFolder = root;

        for (String line : inputData) {
            if (line.startsWith("$ cd")) {
                String targetDirectory = line.substring(5);
                if (targetDirectory.equals("/")) {
                    currentFolder = root;
                } else if (targetDirectory.equals("..")) {
                    currentFolder = currentFolder.parent;
                } else {
                    if (currentFolder.folders.containsKey(targetDirectory)) {
                        currentFolder = currentFolder.folders.get(targetDirectory);
                    }
                }
            } else if (line.startsWith("$ ls")){
                continue;
            } else {
                if (line.startsWith("dir")) {
                    String dirName = line.substring(4);

                    if (!currentFolder.folders.containsKey(dirName)) {
                        MyDirectory child = new MyDirectory(dirName);
                        child.parent = currentFolder;

                        currentFolder.folders.put(dirName, child);
                    }
                } else {
                    String[] sizeAndName = line.split(" ");
                    long size = Long.parseLong(sizeAndName[0]);
                    currentFolder.addFile(sizeAndName[1], size);
                }
            }
        }
    }

    private static void updateSizes(MyDirectory node) {
        if (node == null) return;

        for (MyDirectory children : node.folders.values()) {
            updateSizes(children);
        }

        for (MyDirectory children : node.folders.values()) {
            node.totalSize += children.totalSize;
        }
    }


    static class MyDirectory {
        String name;
        MyDirectory parent = null;
        Map<String, MyDirectory> folders;
        Map<String, Long> files;
        long totalSize = 0;

        MyDirectory(String name) {
            this.name = name;
            folders = new HashMap<>();
            files = new HashMap<>();
        }

        void addFile(String fileName, long fileSize) {
            files.put(fileName, fileSize);
            totalSize += fileSize;
        }
    }
}
