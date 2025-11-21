package game.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import characters.Player;
import utilities.UserInterface;

public class Event {
    private static Scanner scanner = new Scanner(System.in);

    public static void playEvent(Player player, EventTree node) {
        while (node != null) {
            player.displayInfo();
            System.out.println(node.getText());
            UserInterface.delay();
            node.performAction(player);

            List<EventTree> branches = node.getBranches();

            List<EventTree> choices = new ArrayList<>();
            for (EventTree b : branches) {
                if (b.isChoice()) choices.add(b);
            }

            if (choices.isEmpty()) {
                if (branches.size() == 1 && !branches.get(0).isChoice()) {
                    UserInterface.enterReturn(scanner);
                    node = branches.get(0);
                    continue;
                }

                UserInterface.enterReturn(scanner);
                break;
            }

            System.out.println();
            for (int i = 0; i < choices.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + choices.get(i).getText());
            }

            int choice = getUserChoice(choices.size());
            node = choices.get(choice - 1);

            // if node has single child
            List<EventTree> followups = node.getBranches();
            while (followups.size() == 1 && !followups.get(0).isChoice()) {
                node = followups.get(0);
                player.displayInfo();
                System.out.println(node.getText());
                UserInterface.delay();
                node.performAction(player);
                followups = node.getBranches();
                UserInterface.enterReturn(scanner);
            }

            if (followups.size() > 0) {
                node = followups.get(0); 
            } else {
                break;
            }
        }
    }

    private static int getUserChoice(int max) {
        while (true) {
            System.out.print("\nEnter choice: ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= max) return choice;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid input. Try again.");
        }
    }
}
