package year2022;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFileUtil {

    public static List<String> readFile(String path) {
        List<String> parsedFile = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLine()) {
                parsedFile.add(scanner.nextLine());
            }

            scanner.close();
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parsedFile;
    }
}
