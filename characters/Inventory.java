package characters;

import items.*;
import utilities.UserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventory {
    private List<Item> items;
    private List<Item> sortedItems;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        sortInventory();
        System.out.println("> " + item.getName() + " has been added to your inventory.");
    }

    private void sortInventory() {
        List<Item> equipments = new ArrayList<>();
        List<Item> usables = new ArrayList<>();
    
        for (Item item : items) {
            if (item instanceof Equipment) {
                equipments.add(item);
            } else {
                usables.add(item);
            }
        }
    
        sortedItems = new ArrayList<>();
        sortedItems.addAll(equipments);
        sortedItems.addAll(usables);
    }
    
    public void showMenu(Player player) {
        Scanner scanner = player.getScanner();
        final int ITEMS_PER_PAGE = 7;
        int totalPages = Math.max(1, (int) Math.ceil((double) sortedItems.size() / ITEMS_PER_PAGE));
        int currentPage = 1;
    
        while (true) {
            player.displayInfo();
            sortInventory();
            totalPages = Math.max(1, (int) Math.ceil((double) sortedItems.size() / ITEMS_PER_PAGE));
            System.out.println("========== Inventory (Page " + currentPage + "/" + totalPages + ") ==========\n");

            int start = (currentPage - 1) * ITEMS_PER_PAGE;
            int end = Math.min(start + ITEMS_PER_PAGE, sortedItems.size());
    
            if (sortedItems.isEmpty()) {
                System.out.println("Your inventory is empty.");
            } else {
                for (int i = start; i < end; i++) {
                    Item item = sortedItems.get(i);
                    int displayIndex = (i - start) + 1;
                    String equippedIndicator = "";
                    String equipmentStats = "";
    
                    if (item instanceof Equipment) {
                        Equipment equip = (Equipment) item;
                        equipmentStats = " [ATK: " + equip.getAttackBonus() + ", DEF: " + equip.getDefenseBonus() + "]";
                        if (equip.isEquipped()) {
                            equippedIndicator = " (EQUIPPED)";
                        }
                    }
    
                    System.out.println("[" + displayIndex + "] " + item.getName() + " - " + item.getDescription() + equipmentStats + equippedIndicator);
                }
            }
    
            System.out.println();
            if (currentPage > 1) {
                System.out.println("[P] Previous Page");
            }
            if (currentPage < totalPages) {
                System.out.println("[N] Next Page");
            }
            System.out.println("[0] Return to Main Menu");
    
            System.out.print("\nEnter your choice: ");
            String input = scanner.nextLine().trim().toLowerCase();
    
            if (input.equals("0")) {
                break;
            } else if (input.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (input.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else {
                try {
                    int itemChoice = Integer.parseInt(input);
                    int selectedIndex = start + (itemChoice - 1);
    
                    if (selectedIndex >= start && selectedIndex < end) {
                        useItem(selectedIndex, player);
                    } else {
                        System.out.println("Invalid choice! Please pick an available item.");
                        UserInterface.enterReturn(scanner);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Enter a number or use P/N for pagination.");
                    UserInterface.enterReturn(scanner);
                }
                
            }
        }
    }    

    private void useItem(int index, Player player) {
        Scanner scanner = player.getScanner();
        sortInventory();

        if (index >= 0 && index < sortedItems.size()) {
            Item item = sortedItems.get(index);
            System.out.println("\n> " + item.getName());
            String equipOrUse = "";
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                equipOrUse = equipment.isEquipped() ? "Unequip" : "Equip";
            } else {
                equipOrUse = "Use";
            }
            System.out.println("[1] " + equipOrUse + " | [2] Remove | [3] Cancel\n");

            System.out.print("Enter your choice: ");
            String raw = scanner.nextLine().trim().toLowerCase();

            int action = -1;
            try {
                action = Integer.parseInt(raw);
            } catch (NumberFormatException ignored) {
                if (raw.equals("1") || raw.equals("use") || raw.equals("equip") || raw.equals("unequip")) action = 1;
                else if (raw.equals("2") || raw.equals("remove") || raw.equals("delete")) action = 2;
                else if (raw.equals("3") || raw.equals("cancel") || raw.equals("back")) action = 3;
            }

            if (action == 1) {
                item.use(player);
                if (item instanceof Usable) {
                    items.remove(item);
                }
            } else if (action == 2) {
                if (item instanceof Equipment) {
                    Equipment equipment = (Equipment) item;
                    if (equipment.isEquipped()) {
                        player.equipItem(equipment);
                    }
                }
                removeItem(index);
            } else {
                // cancel or invalid input -> do nothing
            }
        } else {
            System.out.println("Invalid choice!");
        }
        UserInterface.enterReturn(scanner);
    }

    public void removeItem(int index) {
        sortInventory();
        if (index >= 0 && index < sortedItems.size()) {
            Item item = sortedItems.get(index);
            items.remove(item);
            System.out.println("> " + item.getName() + " has been removed from your inventory.");
        } else {
            System.out.println("Invalid index! Choose a valid item number.");
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
