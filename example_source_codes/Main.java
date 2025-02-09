import java.io.File;
import java.util.Scanner;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class Main {
    private static int totalSum = 0;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <input.xml>");
            return;
        }

        String xmlFile = args[0];
        try {
            int[] initialValues = readInitialValues(xmlFile);
            totalSum = initialValues[0] + initialValues[1];
        } catch (Exception e) {
            System.out.println("Error reading initial values: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit")) {
                break;
            } else if (userInput.equalsIgnoreCase("print sum")) {
                System.out.println(totalSum);
            } else if (userInput.startsWith("add ")) {
                try {
                    int number = Integer.parseInt(userInput.split(" ")[1]);
                    totalSum += number;
                } catch (Exception e) {
                    System.out.println("Invalid command. Use 'add <number>' to add a number.");
                }
            } else {
                System.out.println("Unknown command. Use 'add <number>' to add a number or 'print sum' to print the sum or 'exit' to exit.");
            }
        }
        scanner.close();
    }

    private static int[] readInitialValues(String xmlFile) throws Exception {
        File file = new File(xmlFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        int num1 = Integer.parseInt(doc.getElementsByTagName("Number1").item(0).getTextContent());
        int num2 = Integer.parseInt(doc.getElementsByTagName("Number2").item(0).getTextContent());

        return new int[]{num1, num2};
    }
}