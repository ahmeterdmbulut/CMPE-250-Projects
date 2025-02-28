import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileCompare {
    public static void main(String[] args) {

        String file1 = "src/2nd-400-400-output.txt";
        String file2 = "src/output.txt";

        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {

            String line1, line2;
            int lineNumber = 1;
            boolean differencesFound = false;

            while ((line1 = reader1.readLine()) != null | (line2 = reader2.readLine()) != null) {
                if (line1 == null || line2 == null || !line1.equals(line2)) {
                    System.out.println("Difference found at line " + lineNumber + ":");
                    System.out.println("File 1: " + (line1 != null ? line1 : "<EOF>"));
                    System.out.println("File 2: " + (line2 != null ? line2 : "<EOF>"));
                    differencesFound = true;
                }
                lineNumber++;
            }

            if (!differencesFound) {
                System.out.println("The files are identical.");
            } else {
                System.out.println("not same");
            }
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        }
    }
}


