import java.util.ArrayList;
import java.util.Scanner;

import characters.*;
import characters.Class;
import characters.npcs.Unknown;
import registry.AreaRegistry;
import registry.ClassRegistry;
import game.Event.Event;
import locations.Area;
import utilities.UserInterface;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserInterface.clearConsole();
        System.out.println(UserInterface.TAN + "===================== Helyx ======================");
        System.out.println(UserInterface.RESET + "     A simple turn-based, rogue-like RPG game.");
        System.out.println(UserInterface.TAN + "==================================================" + UserInterface.RESET);
        System.out.printf("\n> Enter your name: ");
        String playerName = scanner.nextLine();

        Unknown unknown = new Unknown();
        Player player = new Player(playerName, ClassRegistry.noviceClass(), scanner);
        Inventory inventory = player.getInventory();

        Event.playEvent(player, unknown.firstDialogue());

        // render area
        ArrayList<Area> beginnerAreas = AreaRegistry.beginnerAreas();

        while (player.isAlive()) {
            player.displayInfo();
            System.out.println("[1] Travel");
            System.out.println("[2] Inventory");
            System.out.println("[3] Stats");
            System.out.println("[4] Class Info");
            System.out.println("[5] Achievements");
            System.out.println("\n[0] Exit Game");
            System.out.print("\nEnter your choice: ");
            String mainChoice = scanner.nextLine();
            
            switch (mainChoice) {
                case "2":
                    inventory.showMenu(player);
                    break;
                case "1":
                    travelMenu(beginnerAreas, player, scanner);
                    break;
                case "3":
                    player.displayStatsMenu(scanner);
                    break;
                case "4":
                    Class.classMenu(player);
                    break;
                case "5":
                    player.showAchievements(scanner);
                    break;
                case "0":
                    System.out.println("Exiting the game...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
                    UserInterface.enterReturn(scanner);
            }
        }
        
        System.out.println("Game Over..");
        System.out.println(UserInterface.TAN + "Total Score: " + player.getTotalScore() + UserInterface.RESET + "\n");
        scanner.close();
    }

    private static void travelMenu(ArrayList<Area> areas, Player player, Scanner scanner) {
        boolean inTravelMenu = true;
        while (inTravelMenu) {
            player.displayInfo();
            if (!player.isAlive()) { break; }
            System.out.println("========== Travel to Area ==========\n");

            for (int i = 0; i < areas.size(); i++) {
                Area area = areas.get(i);
                System.out.println("[" + (i + 1) + "] " + area.getName());
            }
            System.out.println("\n[0] Return to Main Menu");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            
            if (choice > 0 && choice <= areas.size()) {
                areas.get(choice - 1).explore(player, scanner);
            } else if (choice == 0) {
                inTravelMenu = false;
            } else {
                System.out.println("Invalid choice!");
                UserInterface.enterReturn(scanner);
            }
        }
    }
}
