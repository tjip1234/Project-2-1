package Game.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CreateCSV {
    public void CreateCsv(String filename, List<String[]> array) throws IOException {
        File csv = new File(filename);
        try{
            csv.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (PrintWriter pw = new PrintWriter(csv)) {
            array.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
    public String convertToCSV(String[] data) {
        return String.join(",", data);
    }
}
