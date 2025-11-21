package locations.places;

import java.util.ArrayList;
import java.util.Scanner;

import characters.Player;
import items.Literature;
import locations.Place;
import utilities.UserInterface;

public class Library extends Place{
    private ArrayList<Literature> books;
    public Library(String name, String description, ArrayList<Literature> books) {
        super(name, description);
        this.books = books;
    }

    @Override
    public void explore(Player player, Scanner scanner) {
        boolean keepExploring = true;
        
        while (keepExploring && player.isAlive()) {
            player.displayInfo();
            ArrayList<Literature> booksDuplicate = new ArrayList<>();
            for (Literature b : books) {
                booksDuplicate.add(new Literature(b));
            }
            System.out.println("========== " + name + " Library ==========\n");
            for (int i = 0; i < booksDuplicate.size(); i++) {
                Literature book = booksDuplicate.get(i);
                System.out.println("[" + (i + 1) + "] " + book.getName() + "\n  - " + book.getDescription());
            }

            System.out.println("\n[0] Exit");

            System.out.print("\nEnter book number to read: ");
            int choice = scanner.nextInt();

            if (choice > 0 && choice <= booksDuplicate.size()) {
                booksDuplicate.get(choice - 1).readContent(player);
            } else if (choice == 0) {
                keepExploring = false;
            } else {
                System.out.println("Invalid choice!");
                UserInterface.enterReturn(scanner);
            }
        }
    }
    
}
