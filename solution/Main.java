import java.io.File;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

class Ship {
    int id, strength, timeLimit;
    boolean destroyed = false;
    int timeTaken = 0;

    public Ship(int id, int strength, int timeLimit) {
        this.id = id;
        this.strength = strength;
        this.timeLimit = timeLimit;
    }
}

class Troop {
    String name;
    int rateOfDamage;
    boolean available = true; // Troop availability status

    public Troop(String name, int rateOfDamage) {
        this.name = name;
        this.rateOfDamage = rateOfDamage;
    }
}

public class Main {
    private static List<Troop> troops = new ArrayList<>();
    private static List<Ship> ships = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <input.xml>");
            return;
        }

        String xmlFile = args[0];
        try {
            parseXML(xmlFile);
            scheduleAttacks();
        } catch (Exception e) {
            System.out.println("Error reading XML: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            } else if (command.equalsIgnoreCase("print destroyed ships")) {
                printDestroyedShips();
            } else if (command.equalsIgnoreCase("print remaining ships")) {
                printRemainingShips();
            } else {
                System.out.println("Unknown command. Use:");
                System.out.println("- 'print destroyed ships' to show destroyed ships");
                System.out.println("- 'print remaining ships' to show surviving ships");
                System.out.println("- 'exit' to quit");
            }
        }
        scanner.close();
    }

    private static void parseXML(String xmlFile) throws Exception {
        File file = new File(xmlFile);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        // Parse Troops
        NodeList troopNodes = doc.getElementsByTagName("Troop");
        for (int i = 0; i < troopNodes.getLength(); i++) {
            Element troopElement = (Element) troopNodes.item(i);
            String name = troopElement.getElementsByTagName("Name").item(0).getTextContent();
            int rateOfDamage = Integer.parseInt(troopElement.getElementsByTagName("RateOfDamage").item(0).getTextContent());
            troops.add(new Troop(name, rateOfDamage));
        }

        // Parse Ships
        NodeList shipNodes = doc.getElementsByTagName("Ship");
        for (int i = 0; i < shipNodes.getLength(); i++) {
            Element shipElement = (Element) shipNodes.item(i);
            int id = Integer.parseInt(shipElement.getAttribute("id"));
            int strength = Integer.parseInt(shipElement.getElementsByTagName("Strength").item(0).getTextContent());
            int timeLimit = Integer.parseInt(shipElement.getElementsByTagName("TimeLimit").item(0).getTextContent());
            ships.add(new Ship(id, strength, timeLimit));
        }

        // Sort ships by TimeLimit (ascending) → Prioritize urgent targets
        ships.sort(Comparator.comparingInt(s -> s.timeLimit));
    }

    private static void scheduleAttacks() {
        for (Ship ship : ships) {
            Troop assignedTroop = null;
            for (Troop troop : troops) {
                if (troop.available) {
                    assignedTroop = troop;
                    troop.available = false; // Troop is now engaged
                    break;
                }
            }
            if (assignedTroop == null) break; // No available troops

            int timeToDestroy = (int) Math.ceil((double) ship.strength / assignedTroop.rateOfDamage);

            if (timeToDestroy <= ship.timeLimit) {
                ship.destroyed = true;
                ship.timeTaken = timeToDestroy;
                assignedTroop.available = true; // Free the troop after attack
            } else {
                ship.strength -= assignedTroop.rateOfDamage * ship.timeLimit;
            }
        }

        // Re-sort ships by ID after scheduling
        ships.sort(Comparator.comparingInt(s -> s.id));
    }

    private static void printDestroyedShips() {
        List<String> output = new ArrayList<>();
        int count = 0;
        for (Ship ship : ships) {
            if (ship.destroyed) {
                output.add("(" + ship.id + ", " + ship.timeTaken + ")");
                count++;
            }
        }
        System.out.println(count + ", " + output);
    }

    private static void printRemainingShips() {
        List<String> output = new ArrayList<>();
        int count = 0;
        for (Ship ship : ships) {
            if (!ship.destroyed) {
                output.add("(" + ship.id + ", " + ship.strength + ")");
                count++;
            }
        }
        System.out.println(count + ", " + output);
    }
}
