package locations;

import java.util.ArrayList;
import java.util.Scanner;

import characters.Player;
import locations.places.Dungeon;

public class Area {
    private String name;
    private String description;
    private ArrayList<Place> places;

    public Area(String name, String description) {
        this.name = name;
        this.description = description;
        this.places = null;
    }

    public Area(String name, String description, ArrayList<Place> places) {
        this.name = name;
        this.description = description;
        this.places = places;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void explore(Player player, Scanner scanner) {
        boolean explore = true;
        while (explore && player.isAlive()) {
            player.displayInfo();
            System.out.println("========== " + name + " ==========");
            System.out.println(description + "\n");

            if (places == null) {
                System.out.println("There is nothing to look out here.");
            } else {
                for (int i = 0; i < places.size(); i++) {
                    String energyRequired = "";
                    if (places.get(i) instanceof Dungeon) {
                        Dungeon dungeonPlace = (Dungeon) places.get(i);
                        energyRequired = " (" + dungeonPlace.getEnergyRequired() + " Energy per explore)";
                    }
                    System.out.println("[" + (i + 1) + "] " + places.get(i).getName() + energyRequired + "\n  - " + places.get(i).getDescription());
                }
            }

            System.out.println("\n[0] Exit Area");

            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (places != null) {
                if (choice > 0 && choice <= places.size()) {
                    places.get(choice - 1).explore(player, scanner);
                } else if (choice == 0) {
                    explore = false;
                } else {
                    System.out.println("Invalid choice! Try again.");
                }
            } else explore = false;
            
        }
    }
    
}
