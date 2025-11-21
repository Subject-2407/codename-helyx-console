package locations.places;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import characters.*;
import game.Event.Event;
import game.Event.EventTree;
import locations.Place;
import utilities.UserInterface;

public class Forest extends Place {
    private int energyRequired;
    private Random random;
    private ArrayList<EventTree> events;

    public Forest(String name, String description, int energyRequired, ArrayList<EventTree> events) {
        super(name, description);
        this.energyRequired = energyRequired;
        this.events = events;
        this.random = new Random();
    }

    public void explore(Player player, Scanner scanner) {
        if (player.getEnergy() >= energyRequired) {
            player.setExploring(true);
            while (player.isExploring() && player.isAlive()) {
                player.displayInfo();
                if (player.getEnergy() < energyRequired) {
                    System.out.println("> You don't have enough energy to continue exploring.");
                    System.out.println("> Exiting from " + this.name + ".");
                    UserInterface.enterReturn(scanner);
                    break;
                }
                System.out.println("> Exploring through " + this.name + "..");
                UserInterface.randomDelay(5001);

                // pick random event
                EventTree event = events.get(random.nextInt(this.events.size()));
                Event.playEvent(player, event);
                player.reduceEnergy(energyRequired);
            }
            return;
        } else {
            System.out.println("> You don't have enough energy to travel here!");
            UserInterface.enterReturn(scanner);
            return;
        }
    }
}
