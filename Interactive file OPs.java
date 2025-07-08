import java.io.*;
import java.util.Scanner;

public class InteractiveFileOps {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter file name (with .txt): ");
        String fileName = sc.nextLine();

        System.out.println("Enter text to write to the file (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).equals("END")) {
            content.append(line).append(System.lineSeparator());
        }

        // Write user input to file
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(content.toString());
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }

        System.out.println("\n--- File Content ---");
        printFileContent(fileName);

        System.out.print("\nEnter the exact line you want to replace: ");
        String oldLine = sc.nextLine();
        System.out.print("Enter the new line: ");
        String newLine = sc.nextLine();

        boolean modified = modifyLineInFile(fileName, oldLine, newLine);
        if (modified) {
            System.out.println("Line modified successfully.");
        } else {
            System.out.println("Line not found or file modification failed.");
        }

        System.out.println("\n--- Final File Content ---");
        printFileContent(fileName);

        sc.close();
    }

    // Reads and prints the contents of the file
    private static void printFileContent(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from the file: " + e.getMessage());
        }
    }

    // Replaces a specific line in the file with a new one
    private static boolean modifyLineInFile(String fileName, String oldLine, String newLine) {
        File input = new File(fileName);
        File temp = new File("temp_" + fileName);
        boolean modified = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(input));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(oldLine)) {
                    bw.write(newLine);
                    modified = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error modifying the file: " + e.getMessage());
            return false;
        }

        if (input.delete() && temp.renameTo(input)) {
            return modified;
        } else {
            return false;
        }
    }
}
