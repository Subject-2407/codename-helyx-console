package locations.places;

import java.util.Scanner;

import characters.Monster;
import characters.Player;
import game.*;
import locations.Place;
import utilities.UserInterface;

import java.util.List;
import java.util.Random;

public class Dungeon extends Place {
    private int energyRequired;
    private Random random;
    private MonsterCodex monsters;

    public Dungeon(String name, String description, int energyRequired, MonsterCodex monsters) {
        super(name, description);
        this.energyRequired = energyRequired;
        this.monsters = monsters;
        this.random = new Random();
    }

    public int getEnergyRequired() { return energyRequired; }
    
    public void explore(Player player, Scanner scanner) {
        List<Monster> monstersInRange = monsters.searchBPRange(0, player.getBattlePower());
        if (monstersInRange.size() == 0) {
            player.displayInfo();
            System.out.println("> Looks like there is no monster at the moment..");
            UserInterface.enterReturn(scanner);
            return;
        }
        if (player.getEnergy() >= energyRequired) {
            boolean keepExploring = true;
            while (keepExploring && player.isAlive()) {
                player.displayInfo();
                if (player.getEnergy() < energyRequired) {
                    System.out.println("> You don't have enough energy to continue exploring.");
                    System.out.println("> Exiting from " + this.name + ".");
                    UserInterface.enterReturn(scanner);
                    break;
                }
                System.out.println("> Exploring through " + this.name + "..");
                UserInterface.randomDelay(5001);

                Monster template = monstersInRange.get(random.nextInt(monstersInRange.size()));
                Monster monster = new Monster(template);
                
                System.out.println("> You encountered " + UserInterface.WHITE + monster.getName() + UserInterface.RESET + "! " + UserInterface.PURPLE + "(Battle Power: " + monster.getBattlePower() + ")" + UserInterface.RESET + "\n");
                System.out.println("[1] Fight");
                System.out.println("[2] Run");
                System.out.println("[3] Exit dungeon (-" + energyRequired + " Energy)");

                System.out.print("\nEnter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (choice == 2) {
                    if (!PVE.attemptEscape(player, monster)) {
                        PVE.battle(player, monster);
                    }
                } else if (choice == 3) {
                    keepExploring = false;
                }
                else if (choice == 1){
                    PVE.battle(player, monster);
                }
                player.reduceEnergy(energyRequired);
            }
            return;
        } else {
            System.out.println("You don't have enough energy to travel here!");
            UserInterface.enterReturn(scanner);
            return;
        }
    }
}
